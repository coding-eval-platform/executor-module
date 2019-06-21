package ar.edu.itba.cep.executor_service.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * An abstract execution result.
 */
@Getter
@ToString(doNotUseGetters = true, callSuper = true)
@EqualsAndHashCode(of = "executionRequest")
public abstract class ExecutionResult {

    /**
     * The {@link ExecutionRequest} to which this execution result belongs to.
     */
    private final ExecutionRequest executionRequest;


    /**
     * Constructor.
     *
     * @param executionRequest The {@link ExecutionRequest} to which this execution result belongs to.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    protected ExecutionResult(final ExecutionRequest executionRequest) throws IllegalArgumentException {
        assertExecutionRequest(executionRequest);
        this.executionRequest = executionRequest;
    }


    // ================================
    // Assertions
    // ================================

    /**
     * Asserts that the given {@code executionRequest} is valid.
     *
     * @param executionRequest The {@link ExecutionRequest} to be validated.
     * @throws IllegalArgumentException If the {@link ExecutionRequest} is not valid.
     */
    private static void assertExecutionRequest(final ExecutionRequest executionRequest)
            throws IllegalArgumentException {
        Assert.notNull(executionRequest, "The execution request must not be null");
    }
}
