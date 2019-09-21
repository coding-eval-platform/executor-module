package ar.edu.itba.cep.executor_service.runner;

import ar.edu.itba.cep.executor.models.ExecutionRequest;
import ar.edu.itba.cep.executor.models.ExecutionResponse;

/**
 * A port out of the application that allows processing an {@link ExecutionRequest} (i.e run code).
 */
public interface CodeRunner {

    /**
     * Processes the given {@code executionRequest}.
     *
     * @param executionRequest The {@link ExecutionRequest} to be processed.
     * @return The {@link ExecutionResponse}.
     * @throws IllegalArgumentException if the given {@code executionRequest} is {@code null}.
     */
    ExecutionResponse processExecutionRequest(final ExecutionRequest executionRequest)
            throws IllegalArgumentException;
}
