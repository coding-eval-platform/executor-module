package ar.edu.itba.cep.executor_service.models;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * A base {@link ExecutionResult} for all results that require an exit code, stdout and stderr.
 */
@Getter
@ToString(doNotUseGetters = true, callSuper = true)
/* package */ abstract class BasicOutputExecutionResult extends ExecutionResult {

    /**
     * The execution's exit code.
     */
    private final int exitCode;
    /**
     * A {@link List} of {@link String}s that were sent to standard output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard output.
     */
    private final List<String> stdout;
    /**
     * A {@link List} of {@link String}s that were sent to standard error output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard error output.
     */
    private final List<String> stderr;


    /**
     * Constructor.
     *
     * @param exitCode         The execution's exit code.
     * @param stdout           A {@link List} of {@link String}s
     *                         that were sent to standard output by the program being executed.
     *                         Each {@link String} in the {@link List} is a line that was printed in standard output.
     * @param stderr           A {@link List} of {@link String}s
     *                         that were sent to standard error output by the program being executed.
     *                         Each {@link String} in the {@link List}
     *                         is a line that was printed in standard error output.
     * @param executionRequest The {@link ExecutionRequest} to which this execution result belongs to.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    /* package */ BasicOutputExecutionResult(
            final int exitCode,
            final List<String> stdout,
            final List<String> stderr,
            final ExecutionRequest executionRequest) throws IllegalArgumentException {
        super(executionRequest);
        assertStdOutList(stdout);
        assertStdErrList(stderr);
        this.exitCode = exitCode;
        this.stdout = stdout;
        this.stderr = stderr;
    }


    // ================================
    // Assertions
    // ================================

    /**
     * Asserts that the given {@code stdout} {@link List} is valid.
     *
     * @param stdout The {@link List} with standard output to be validated.
     * @throws IllegalArgumentException If the {@link List} is not valid.
     */
    private static void assertStdOutList(final List<String> stdout) throws IllegalArgumentException {
        Assert.notNull(stdout, "The stdout list must not be null");
        Assert.isTrue(stdout.stream().noneMatch(Objects::isNull), "The stdout list must not contain nulls.");
    }

    /**
     * Asserts that the given {@code stderr} {@link List} is valid.
     *
     * @param stderr The {@link List} with standard error output to be validated.
     * @throws IllegalArgumentException If the {@link List} is not valid.
     */
    private static void assertStdErrList(final List<String> stderr) throws IllegalArgumentException {
        Assert.notNull(stderr, "The stderr list must not be null");
        Assert.isTrue(stderr.stream().noneMatch(Objects::isNull), "The stderr list must not contain nulls.");
    }
}