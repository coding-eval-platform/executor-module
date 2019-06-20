package ar.edu.itba.cep.executor_service.models;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Represents an {@link ExecutionResult} for an execution of a code that did not compiled
 * (i.e only for compiled languages).
 */
@Getter
@ToString(doNotUseGetters = true, callSuper = true)
public class CompileErrorExecutionResult extends BasicOutputExecutionResult {


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
    public CompileErrorExecutionResult(
            final int exitCode,
            final List<String> stdout,
            final List<String> stderr,
            final ExecutionRequest executionRequest) throws IllegalArgumentException {
        super(exitCode, stdout, stderr, executionRequest);
        assertLanguage(executionRequest.getLanguage());
    }


    // ================================
    // Assertions
    // ================================

    /**
     * Asserts that the given {@code language} is valid.
     *
     * @param language The {@link Language} to be checked.
     * @throws IllegalArgumentException If the {@link Language} is not valid.
     */
    private static void assertLanguage(final Language language) throws IllegalArgumentException {
        Assert.isTrue(language.isCompiled(), "The language must be compiled");
    }
}
