package ar.edu.itba.cep.executor_service.models;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * Represents an {@link ExecutionResult} for an execution of a code that did not compiled
 * (i.e only for compiled languages).
 */
@Getter
@ToString(doNotUseGetters = true, callSuper = true)
public class CompileErrorExecutionResult extends ExecutionResult {

    /**
     * A {@link List} of {@link String}s that were reported by the compiler on failure.
     */
    private final List<String> compilerErrors;


    /**
     * Constructor.
     *
     * @param compilerErrors   A {@link List} of {@link String}s that were reported by the compiler on failure.
     * @param executionRequest The {@link ExecutionRequest} to which this execution result belongs to.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    public CompileErrorExecutionResult(
            final List<String> compilerErrors,
            final ExecutionRequest executionRequest) throws IllegalArgumentException {
        super(executionRequest);
        assertCompilerErrors(compilerErrors);
        assertLanguage(executionRequest.getLanguage());

        this.compilerErrors = compilerErrors;
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

    /**
     * Asserts that the given {@code compilerErrors} {@link List} is valid.
     *
     * @param compilerErrors The {@link List} with compiler errors to be validated.
     * @throws IllegalArgumentException If the {@link List} is not valid.
     */
    private static void assertCompilerErrors(final List<String> compilerErrors) throws IllegalArgumentException {
        Assert.notNull(compilerErrors, "The compiler errors list must not be null");
        Assert.isTrue(
                compilerErrors.stream().noneMatch(Objects::isNull),
                "The compiler error list must not contain nulls."
        );
    }
}
