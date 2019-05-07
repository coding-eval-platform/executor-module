package ar.edu.itba.cep.executor_service.models;

import org.springframework.util.Assert;

import java.util.Objects;

/**
 * An abstract execution result.
 */
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


    /**
     * @return The {@link ExecutionRequest} to which this execution result belongs to.
     */
    public ExecutionRequest getExecutionRequest() {
        return executionRequest;
    }


    // ================================
    // equals, hashcode and toString
    // ================================

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExecutionResult)) {
            return false;
        }
        final var that = (ExecutionResult) o;
        return executionRequest.equals(that.executionRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionRequest);
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "executionRequest=" + executionRequest +
                '}';
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
