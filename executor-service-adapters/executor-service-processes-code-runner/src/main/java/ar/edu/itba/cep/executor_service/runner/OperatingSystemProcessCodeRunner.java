package ar.edu.itba.cep.executor_service.runner;

import ar.edu.itba.cep.executor.models.ExecutionRequest;
import ar.edu.itba.cep.executor.models.ExecutionResponse;
import ar.edu.itba.cep.executor.models.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
     * The name of the environment variable in which the code is set.
     */
    private final static String CODE_ENV_VARIABLE = "CODE";
    /**
     * The name of the environment variable in which the compiler flags are set.
     */
    private final static String COMPILER_FLAGS_ENV_VARIABLE = "COMPILER_FLAGS";
    /**
     * The name of the environment variable in which the timeout is set.
     */
    private final static String TIMEOUT_ENV_VARIABLE = "TIMEOUT";
    /**
     * The name of the environment variable in which the result file name is set.
     */
    private final static String RESULT_FILE_NAME_ENV_VARIABLE = "RESULT_FILE_NAME";
    /**
     * The name of the environment variable in which the main file name is set.
     */
    private final static String MAIN_FILE_NAME_ENV_VARIABLE = "MAIN_FILE_NAME";;
    /**
     * The name for the file where the execution result must be stored.
     */
    private final static String RESULT_FILE_NAME = "result"; // TODO: make this configurable?

    /**
     * Margin for the timeout to be added to the time Java will wait the sub-processes.
     * This has nothing to do with the timeout given to the program being tested, but the time this process
     * will wait any runner sub-processes till it consider the child to be timed-out.
     */
    private final static long GRACE_MARGIN = 10000; // TODO: make this configurable?

    /**
     * Charset to be used to convert an {@link InputStream} into {@link String}s.
     */
    private static final Charset INPUT_STREAM_CHARSET = StandardCharsets.UTF_8; // TODO: make it a param?


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


    @Override
    public ExecutionResponse processExecutionRequest(final ExecutionRequest executionRequest)
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
     * and returns the corresponding {@link ExecutionResponse}.
     *
     * @param request          The {@link ExecutionRequest} to be processed.
     * @param workingDirectory The {@link File} representing the working directory in which the process will run.
     * @return The {@link ExecutionResponse} that comes up from the execution.
     */
    private ExecutionResponse runCode(final ExecutionRequest request, final File workingDirectory) {
        final var language = request.getLanguage();
        final var program = Optional
                .ofNullable(commands.get(language))
                .orElseThrow(() -> new RuntimeException("No command for language " + language));


        // First initialize this in case no timeout was set
        final var executionTimeout = Optional.ofNullable(request.getTimeout()).orElse(this.processTimeout);

        final List<String> command = new LinkedList<>();
        command.add(program);
        command.addAll(request.getProgramArguments());

        final var processBuilder = new ProcessBuilder()
                .directory(workingDirectory)
                .command(command);
        final var environment = processBuilder.environment();
        environment.put(CODE_ENV_VARIABLE, request.getCode());
        environment.put(COMPILER_FLAGS_ENV_VARIABLE, Optional.ofNullable(request.getCompilerFlags()).orElse(""));
        environment.put(TIMEOUT_ENV_VARIABLE, Double.toString(executionTimeout / 1000d)); // TODO: BigDecimal?
        environment.put(RESULT_FILE_NAME_ENV_VARIABLE, RESULT_FILE_NAME);
        environment.put(
                MAIN_FILE_NAME_ENV_VARIABLE,
                Optional.ofNullable(request.getMainFileName()).filter(StringUtils::hasText).orElse("")
        );

        final var processTimeout = Math.max(executionTimeout, this.processTimeout) + GRACE_MARGIN;
        try {
            final var process = processBuilder.start(); // Start the process.
            writeLines(request.getStdin(), process.getOutputStream()); // Send stdin in the request to the process.
            final var finished = executionHasCompleted(process, processTimeout); // Wait till finish.
            // Build the corresponding response.
            return new ExecutionResponse(
                    finished ? retrieveResult(workingDirectory) : ExecutionResponse.ExecutionResult.TIMEOUT,
                    process.exitValue(),
                    readLines(process.getInputStream()),
                    readLines(process.getErrorStream())
            );
        } catch (final IOException e) {
            throw new UncheckedIOException("The execution failed unexpectedly", e); // TODO: define proper exception
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
            LOGGER.warn("The execution was unexpectedly interrupted", e);
            return false; // If interrupted, return false.
        }
    }

    /**
     * Retrieves the {@link ExecutionResponse.ExecutionResult}
     * from the given {@code workingDirectory}
     *
     * @param workingDirectory The {@link File} representing the working directory in which the process has run.
     * @return The {@link ExecutionResponse.ExecutionResult}
     * @throws IOException If any IO error occurs while retrieving the value.
     */
    private static ExecutionResponse.ExecutionResult retrieveResult(final File workingDirectory) throws IOException {
        final var resultPath = new File(workingDirectory, RESULT_FILE_NAME).toPath();
        return Files.lines(resultPath).findFirst()
                .flatMap(OperatingSystemProcessCodeRunner::fromString)
                .orElse(ExecutionResponse.ExecutionResult.UNKNOWN_ERROR);
    }

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

    /**
     * Writes the {@link String}s in the given {@code lines} {@link List} as lines in the given {@code outputStream}.
     *
     * @param lines        The {@link String}s to be written as lines in the given {@code outputStream}.
     * @param outputStream The {@link OutputStream} to which the lines will be written.
     */
    private static void writeLines(final List<String> lines, final OutputStream outputStream) {
        try (final PrintWriter printWriter = new PrintWriter(outputStream, true)) {
            // Write all lines. If the list is empty, EOF is sent.
            lines.forEach(printWriter::println);
        }
    }

    /**
     * Returns the {@link ExecutionResponse.ExecutionResult} corresponding to the given {@code string},
     * wrapped in an {@link Optional}.
     *
     * @param string The {@link String} value of the the {@link ExecutionResponse.ExecutionResult} being returned.
     * @return An {@link Optional} holding the {@link ExecutionResponse.ExecutionResult} if there is such for the
     * given {@code string}, or empty otherwise.
     */
    private static Optional<ExecutionResponse.ExecutionResult> fromString(final String string) {
        try {
            return Optional.ofNullable(ExecutionResponse.ExecutionResult.fromString(string));
        } catch (final IllegalArgumentException ignored) {
            LOGGER.warn("An unexpected value (${}) was received", string);
            return Optional.empty();
        }
    }
}
