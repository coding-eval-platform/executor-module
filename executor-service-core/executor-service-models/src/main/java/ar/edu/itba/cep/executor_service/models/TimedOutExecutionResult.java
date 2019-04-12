package ar.edu.itba.cep.executor_service.models;

/**
 * Represents an execution result (i.e exit code, stdout and stderr).
 */
public class TimedOutExecutionResult extends ExecutionResult {


    /**
     * Constructor.
     *
     * @param executionRequest The {@link ExecutionRequest} to which this execution result belongs to.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    public TimedOutExecutionResult(final ExecutionRequest executionRequest) throws IllegalArgumentException {
        super(executionRequest);
    }


    // ================================
    // toString
    // ================================

    @Override
    public String toString() {
        return "TimedOutExecutionResult{} " + super.toString();
    }
}
