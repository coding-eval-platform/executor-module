package ar.edu.itba.cep.executor_service.models;

import lombok.ToString;

/**
 * Represents an {@link ExecutionResult} that has timed-out.
 */
@ToString(doNotUseGetters = true, callSuper = true)
public class UnknownErrorExecutionResult extends ExecutionResult {


    /**
     * Constructor.
     *
     * @param executionRequest The {@link ExecutionRequest} to which this execution result belongs to.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    public UnknownErrorExecutionResult(final ExecutionRequest executionRequest) throws IllegalArgumentException {
        super(executionRequest);
    }
}
