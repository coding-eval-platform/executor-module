package ar.edu.itba.cep.executor_service.runner;

import ar.edu.itba.cep.executor_service.models.ExecutionRequest;
import ar.edu.itba.cep.executor_service.models.ExecutionResult;

/**
 * A port out of the application that allows processing an {@link ExecutionRequest} (i.e run code).
 */
public interface CodeRunner {

    /**
     * Processes the given {@code executionRequest}.
     *
     * @param executionRequest The {@link ExecutionRequest} to be processed.
     * @return The {@link ExecutionResult}.
     * @throws IllegalArgumentException if the given {@code executionRequest} is {@code null}.
     */
    ExecutionResult processExecutionRequest(final ExecutionRequest executionRequest)
            throws IllegalArgumentException;
}
