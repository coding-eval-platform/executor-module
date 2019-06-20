package ar.edu.itba.cep.executor_service.models;

import java.util.List;

/**
 * Represents an {@link ExecutionResult} that has finished (i.e has exit code, stdout and stderr).
 */
public class FinishedExecutionResult extends BasicOutputExecutionResult {


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
    public FinishedExecutionResult(
            final int exitCode,
            final List<String> stdout,
            final List<String> stderr,
            final ExecutionRequest executionRequest) throws IllegalArgumentException {
        super(exitCode, stdout, stderr, executionRequest);
    }
}
