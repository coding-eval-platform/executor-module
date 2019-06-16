package ar.edu.itba.cep.executor_service.runner;

import ar.edu.itba.cep.executor_service.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Component in charge of running code.
 * A {@link CodeRunner} that uses the underlying OS to run code,
 * using the <a href=https://en.wikipedia.org/wiki/Fork%E2%80%93exec>fork-exec</a> technique.
 * See also <a href=https://en.wikipedia.org/wiki/Spawn_(computing)>Spawning</a>.
 */
public class OperatingSystemProcessCodeRunner implements CodeRunner, InitializingBean {

    /**
     * The {@link Logger}.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(OperatingSystemProcessCodeRunner.class);

    /**
     * Base working directory for the runner.
     * New directories will be created here where each execution will be performed.
     */
    private final File baseWorkingDir;

    /**
     * Timeout to be given to the runner command process in case it hangs out.
     * This is different than the execution timeout, which is used to evaluate efficiency and performance of code.
     */
    private final long processTimeout;

    /**
     * A {@link Map} containing the commands to be used for each {@link Language}.
     * This commands can be OS shell native commands, shell script files, executable files, custom programs, etc.
     * The only requirement is that a {@link Process} can be started using the values of the {@link Map}.
     * See the execve <a href=http://man7.org/linux/man-pages/man2/execve.2.html>System Call Manual</a>,
     * or the <a href=https://docs.microsoft.com/en-us/cpp/c-runtime-library/spawn-wspawn-functions>Spawn functions
     * documentation</a> for more information on this.
     */
    private final Map<Language, String> commands;


    /**
     * @param baseWorkingDir Base working directory for the runner.
     *                       New directories will be created here where each execution will be performed.
     * @param processTimeout Timeout to be given to the runner command process in case it hangs out.
     *                       This is different than the execution timeout,
     *                       which is used to evaluate efficiency and performance of code.
     * @param commands       A {@link Map} containing the commands to be used for each {@link Language}.
     *                       This commands can be OS shell native commands, shell script files, executable files,
     *                       custom programs, etc.
     *                       The only requirement is that a {@link Process} can be started using the values
     *                       of the {@link Map}.
     *                       See the execve <a href=http://man7.org/linux/man-pages/man2/execve.2.html>System Call
     *                       Manual</a>, or the
     *                       <a href=https://docs.microsoft.com/en-us/cpp/c-runtime-library/spawn-wspawn-functions>Spawn
     *                       functions documentation</a> for more information on this.
     */
    public OperatingSystemProcessCodeRunner(
            final String baseWorkingDir,
            final long processTimeout,
            final Map<Language, String> commands) {
        this.baseWorkingDir = new File(baseWorkingDir);
        this.processTimeout = processTimeout;
        this.commands = Collections.unmodifiableMap(commands);
    }

    @Override
    public void afterPropertiesSet() {
        if (baseWorkingDir.exists() && !baseWorkingDir.isDirectory()) {
            throw new IllegalStateException("The base working directory file does not reference a directory");
        }
    }

    /**
     * Processes the given {@code executionRequest}.
     *
     * @param executionRequest The {@link ExecutionRequest} to be processed.
     * @return The {@link ExecutionResult}.
     * @throws IllegalArgumentException if the given {@code executionRequest} is {@code null}.
     */
    @Override
    public ExecutionResult processExecutionRequest(final ExecutionRequest executionRequest)
            throws IllegalArgumentException {
        Assert.notNull(executionRequest, "The execution request must not be null");
        final var workingDirectory = createWorkingDirectory(); // TODO: should we lock the working directory?
        return runCode(executionRequest, workingDirectory);
        // TODO: should we delete the working directory?
    }

    /**
     * Creates the working directory where the runner process will execute.
     * It creates all the directories that are needed.
     *
     * @return The {@link File} representing the working directory.
     */
    private File createWorkingDirectory() {
        if (!baseWorkingDir.exists() && !baseWorkingDir.mkdirs()) {
            throw new WorkingDirectoryException("The base working directory does not exist and could not be created");
        }
        final var workingDirectory = new File(baseWorkingDir, UUID.randomUUID().toString());
        if (workingDirectory.exists()) {
            throw new WorkingDirectoryException("The working directory to be created already exists"); // TODO: retry?
        }
        if (workingDirectory.mkdir()) {
            return workingDirectory;
        }
        throw new WorkingDirectoryException("Could not create the working directory"); // TODO: retry?
    }

    /**
     * Runs the code, using the inputs, language and timeout in the given {@link ExecutionRequest}
     * and returns the corresponding {@link ExecutionResult}.
     *
     * @param executionRequest The {@link ExecutionRequest} to be processed.
     * @param workingDirectory The {@link File} representing the working directory in which the process will run.
     * @return The {@link ExecutionResult} that comes up from the execution.
     */
    private ExecutionResult runCode(final ExecutionRequest executionRequest, final File workingDirectory) {
        final var language = executionRequest.getLanguage();
        final var program = Optional
                .ofNullable(commands.get(language))
                .orElseThrow(() -> new RuntimeException("No command for language " + language));

        final List<String> command = new LinkedList<>();
        command.add(program);
        command.addAll(executionRequest.getInputs());

        final var processBuilder = new ProcessBuilder()
                .directory(workingDirectory)
                .command(command);
        final var environment = processBuilder.environment();
        environment.put("CODE", executionRequest.getCode());
        environment.put("TIMEOUT", Double.toString(executionRequest.getTimeout() / 1000d)); // TODO: BigDecimal?

        try {
            // Start the process.
            final var process = processBuilder.start();
            if (executionHasCompleted(process, processTimeout)) {
                // TODO: check compile error
                final var stdout = readLines(process.getInputStream());
                final var stderr = readLines(process.getErrorStream());
                final var exitCode = process.exitValue(); // TODO: 124 is timeout also
                return new FinishedExecutionResult(exitCode, stdout, stderr, executionRequest);
            }
            return new TimedOutExecutionResult(executionRequest);
        } catch (final IOException e) {
            throw new ExecutionFailedException("The execution failed unexpectedly", e); // TODO: retry?
        }
    }


    // ================================================================================================================
    // Helpers
    // ================================================================================================================

    /**
     * Indicates whether the given {@code process} finishes before the given {@code timeout} elapses.
     *
     * @param process The {@link Process} to be checked.
     * @param timeout The time given to the process to complete.
     * @return {@code true} if the process finishes before the timeout elapses, or {@code false} otherwise.
     */
    private static boolean executionHasCompleted(final Process process, final long timeout) {
        try {
            // Wait till it finishes, or times out.
            return process.waitFor(timeout, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException e) {
            return false; // If interrupted, return false.
        }
    }

    /**
     * Charset to be used to convert an {@link InputStream} into {@link String}s.
     */
    private static final Charset INPUT_STREAM_CHARSET = Charset.forName("UTF-8"); // TODO: make it a param?

    /**
     * Converts the given {@code inputStream} into a {@link List} of {@link String},
     * where each element of the {@link List} is a line in the {@link InputStream}.
     *
     * @param inputStream The {@link InputStream} to be read.
     * @return A {@link List} with the read lines.
     * @throws UncheckedIOException If any {@link IOException} occurs while reading the {@link InputStream}.
     */
    private static List<String> readLines(final InputStream inputStream) throws UncheckedIOException {
        try (final var bufferedReader = new BufferedReader(new InputStreamReader(inputStream, INPUT_STREAM_CHARSET))) {
            return bufferedReader.lines().collect(Collectors.toList());
        } catch (final IOException e) {
            throw new UncheckedIOException(e); // TODO: define proper exception
        }
    }
}
