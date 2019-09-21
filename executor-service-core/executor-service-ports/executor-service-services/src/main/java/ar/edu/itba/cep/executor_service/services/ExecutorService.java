package ar.edu.itba.cep.executor_service.services;

import ar.edu.itba.cep.executor.models.ExecutionRequest;
import ar.edu.itba.cep.executor.models.ExecutionResponse;

/**
 * A port into the application that allows requesting an execution.
 */
public interface ExecutorService {

    /**
     * Processes the given {@code executionRequest}.
     *
     * @param executionRequest The {@link ExecutionRequest} to be processed.
     * @return The {@link ExecutionResponse} that came up of after processing the given {@code executionRequest}.
     * @throws IllegalArgumentException If the given {@code executionRequest} is invalid.
     */
    ExecutionResponse processExecutionRequest(final ExecutionRequest executionRequest) throws IllegalArgumentException;
}
