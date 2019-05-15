package ar.edu.itba.cep.executor_service.runner;

import ar.edu.itba.cep.executor_service.models.ExecutionRequest;
import ar.edu.itba.cep.executor_service.models.ExecutionResult;
import ar.edu.itba.cep.executor_service.models.FinishedExecutionResult;
import ar.edu.itba.cep.executor_service.models.TimedOutExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Component in charge of running code.
 */
public class OperatingSystemProcessCodeRunner implements CodeRunner, InitializingBean {

    /**
     * The {@link Logger}.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(OperatingSystemProcessCodeRunner.class);

    /**
     * The command executed before sending code to run.
     * Can be used to create sources in the working directory, compile, etc.
     */
    private final String preRunCommand;
    /**
     * Arguments passed to the pre-run command
     */
    private final List<String> preRunArgs;
    /**
     * Timeout given to the pre-run command.
     */
    private final long preRunCommandTimeout;
    /**
     * A flag indicating whether the running process should be performed
     * when the pre-run command's exit value is not zero.
     */
    private final boolean continueOnPreRunNonZeroExitCode;
    /**
     * The command used to run the code (e.g bash, javac && java, jshell, irb, python, custom script, etc.).
     */
    private final String runnerCommand;
    /**
     * Arguments for the code execution command.
     */
    private final List<String> runnerArgs;
    /**
     * Default timeout given to the runner command.
     */
    private final long defaultRunnerTimeout;
    /**
     * Base working directory for the runner.
     */
    private final File baseWorkingDir;


    /**
     * @param preRunCommand                   The command executed before sending code to run.
     *                                        Can be used to create sources in the working directory, compile, etc.
     * @param preRunArgs                      Arguments passed to the pre-run command
     * @param preRunCommandTimeout            Timeout given to the pre-run command.
     * @param continueOnPreRunNonZeroExitCode A flag indicating whether the running process should be performed
     *                                        when the pre-run command's exit value is not zero.
     * @param runnerCommand                   The command used to run the code
     *                                        (e.g bash, javac && java, jshell, irb, python, custom script, etc.).
     * @param runnerArgs                      Arguments for the code execution command.
     * @param defaultRunnerTimeout            Default timeout given to the runner command.
     * @param baseWorkingDir                  Base working directory for the runner.
     */
    public OperatingSystemProcessCodeRunner(
            final String preRunCommand,
            final List<String> preRunArgs,
            final long preRunCommandTimeout,
            final boolean continueOnPreRunNonZeroExitCode,
            final String runnerCommand,
            final List<String> runnerArgs,
            final long defaultRunnerTimeout,
            final String baseWorkingDir) {
        Assert.hasText(preRunCommand, "The pre-run command must be set.");
        Assert.isTrue(preRunArgs.stream().allMatch(StringUtils::hasText), "All the arguments must have text.");
        Assert.isTrue(preRunCommandTimeout > 0, "The timeout for the pre-run command must be positive");
        Assert.hasText(runnerCommand, "The runner command must be set.");
        Assert.isTrue(runnerArgs.stream().allMatch(StringUtils::hasText), "All the arguments must have text.");
        Assert.isTrue(defaultRunnerTimeout > 0, "The default timeout for the runner command must be positive");
        Assert.hasText(baseWorkingDir, "The base working directory must be set.");
        this.preRunCommand = preRunCommand;
        this.preRunArgs = preRunArgs;
        this.preRunCommandTimeout = preRunCommandTimeout;
        this.continueOnPreRunNonZeroExitCode = continueOnPreRunNonZeroExitCode;
        this.defaultRunnerTimeout = defaultRunnerTimeout;
        this.runnerCommand = runnerCommand;
        this.runnerArgs = runnerArgs;
        this.baseWorkingDir = new File(baseWorkingDir);
    }

    @Override
    public void afterPropertiesSet() {
        if (!baseWorkingDir.isDirectory()) {
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
        Assert.notNull(executionRequest, "The execution request mut not be null");
        final var workingDirectory = createWorkingDirectory(); // TODO: should we lock the directory?
        preRun(workingDirectory, executionRequest.getCode()); // TODO: if compiled and failed, then return the corresponding ExecutionResult
        final var timeout = Optional.ofNullable(executionRequest.getTimeout()).orElse(defaultRunnerTimeout);
        return runCode(workingDirectory, executionRequest.getInputs(), timeout).apply(executionRequest);
        // TODO: should we delete the working directory?
    }

    /**
     * Creates the working directory where the code will be run. It creates all the directories that are needed.
     *
     * @return The {@link File} representing the working directory.
     */
    private File createWorkingDirectory() {
        Assert.state(baseWorkingDir.exists() || baseWorkingDir.mkdirs(),
                "The base working directory does not exist and could not be created");
        // TODO: should we lock the base working directory?
        final var workingDirectory = new File(baseWorkingDir, UUID.randomUUID().toString());
        if (workingDirectory.exists()) {
            throw new RuntimeException("The working directory to be created already exists"); // TODO: improve this!
        }
        final var created = workingDirectory.mkdir();
        if (created) {
            return workingDirectory;
        }
        throw new RuntimeException("Could not create the working directory"); // TODO: improve this!
    }


    /**
     * Executes the pre-run command.
     *
     * @param workingDirectory The {@link File} representing the working directory.
     * @param code             The code to be run.
     */
    private void preRun(final File workingDirectory, final String code) {
        // The command is the pre-run command, together with the arguments for it, together with the code to be run.
        final var command = Stream
                .of(
                        Stream.of(preRunCommand),
                        preRunArgs.stream().filter(StringUtils::hasText),
                        Stream.of(code)
                )
                .flatMap(Function.identity())
                .collect(Collectors.toList());

        // Once the command is created, create the process.
        final var processBuilder = new ProcessBuilder()
                .directory(workingDirectory)
                .command(command);

        try {
            // Start the process.
            final var process = processBuilder.start();
            try {
                // Wait till it finishes, or times out.
                process.waitFor(preRunCommandTimeout, TimeUnit.MILLISECONDS);
            } catch (final InterruptedException e) {
                // In case it timed out, then throw an Exception as normal execution cannot be continued.
                throw new RuntimeException("Pre-run process could not be completed!"); // TODO: improve this!
            }
            // If reached here, the process has finished.
            if (process.exitValue() != 0) {
                if (continueOnPreRunNonZeroExitCode) {
                    LOGGER.warn("The pre-run process returned a non-zero exit value.");
                } else {
                    throw new RuntimeException("Pre-run process did not terminate with a zero exit value"); // TODO: improve this!
                }
            }
        } catch (final IOException e) {
            throw new UncheckedIOException(e); // TODO: improve this!
        }
    }

    /**
     * Runs the code and returns a {@link Function} that given an {@link ExecutionRequest},
     * returns an {@link ExecutionResult}.
     *
     * @param workingDirectory The {@link File} representing the working directory.
     * @param args             The inputs {@link List} to be passed to the code to be run.
     * @param timeout          The timeout for the execution.
     * @return a {@link Function} that given an {@link ExecutionRequest}, returns an {@link ExecutionResult}.
     * This {@link Function} may contain data about the execution that is performed within this method.
     * Such data is used to create the {@link ExecutionResult}
     */
    private Function<ExecutionRequest, ? extends ExecutionResult> runCode(
            final File workingDirectory,
            final List<String> args,
            final long timeout) {
        // The command is the runner command, together with the arguments for it, together with the inputs
        final var command = Stream
                .of(
                        Stream.of(runnerCommand),
                        runnerArgs.stream().filter(StringUtils::hasText),
                        args.stream()
                )
                .flatMap(Function.identity())
                .collect(Collectors.toList());

        // Once the command is created, create the process.
        final var processBuilder = new ProcessBuilder()
                .directory(workingDirectory)
                .command(command);

        try {
            // Start the process.
            final var process = processBuilder.start();
            if (executionHasCompleted(process, timeout)) {
                final var stdout = readLines(process.getInputStream());
                final var stderr = readLines(process.getErrorStream());
                final var exitCode = process.exitValue();
                return req -> new FinishedExecutionResult(exitCode, stdout, stderr, req);
            }
            return TimedOutExecutionResult::new;
        } catch (final IOException e) {
            throw new UncheckedIOException(e); // TODO: improve this!
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
