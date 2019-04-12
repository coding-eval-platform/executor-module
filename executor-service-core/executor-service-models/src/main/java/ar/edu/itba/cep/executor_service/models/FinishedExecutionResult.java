package ar.edu.itba.cep.executor_service.models;

import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Represents an execution result (i.e exit code, stdout and stderr).
 */
public class FinishedExecutionResult extends ExecutionResult {

    /**
     * The execution's exit code.
     */
    private final int exitCode;

    /**
     * A {@link List} of {@link String}s that were sent to standard output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard output.
     */
    private final List<String> stdOut;

    /**
     * A {@link List} of {@link String}s that were sent to standard error output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard error output.
     */
    private final List<String> stdErr;


    /**
     * Constructor.
     *
     * @param exitCode         The execution's exit code.
     * @param stdOut           A {@link List} of {@link String}s
     *                         that were sent to standard output by the program being executed.
     *                         Each {@link String} in the {@link List} is a line that was printed in standard output.
     * @param stdErr           A {@link List} of {@link String}s
     *                         that were sent to standard error output by the program being executed.
     *                         Each {@link String} in the {@link List}
     *                         is a line that was printed in standard error output.
     * @param executionRequest The {@link ExecutionRequest} to which this execution result belongs to.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    public FinishedExecutionResult(
            final int exitCode,
            final List<String> stdOut,
            final List<String> stdErr,
            final ExecutionRequest executionRequest) throws IllegalArgumentException {
        super(executionRequest);
        assertStdOutList(stdOut);
        assertStdErrList(stdErr);
        this.exitCode = exitCode;
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }


    /**
     * @return The execution's exit code.
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * @return A {@link List} of {@link String}s that were sent to standard output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard output.
     */
    public List<String> getStdOut() {
        return stdOut;
    }

    /**
     * @return A {@link List} of {@link String}s that were sent to standard error output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard error output.
     */
    public List<String> getStdErr() {
        return stdErr;
    }


    // ================================
    // toString
    // ================================

    @Override
    public String toString() {
        return "FinishedExecutionResult{" +
                "exitCode=" + exitCode +
                ", stdOut=" + stdOut +
                ", stdErr=" + stdErr +
                "} " + super.toString();
    }


    // ================================
    // Assertions
    // ================================

    /**
     * Asserts that the given {@code stdOut} {@link List} is valid.
     *
     * @param stdOut The {@link List} with standard output to be validated.
     * @throws IllegalArgumentException If the {@link List} is not valid.
     */
    private static void assertStdOutList(final List<String> stdOut) throws IllegalArgumentException {
        Assert.notNull(stdOut, "The stdout list must not be null");
        Assert.isTrue(stdOut.stream().noneMatch(Objects::isNull), "The stdout list must not contain nulls.");
    }

    /**
     * Asserts that the given {@code stdErr} {@link List} is valid.
     *
     * @param stdErr The {@link List} with standard error output to be validated.
     * @throws IllegalArgumentException If the {@link List} is not valid.
     */
    private static void assertStdErrList(final List<String> stdErr) throws IllegalArgumentException {
        Assert.notNull(stdErr, "The stderr list must not be null");
        Assert.isTrue(stdErr.stream().noneMatch(Objects::isNull), "The stderr list must not contain nulls.");
    }
}
