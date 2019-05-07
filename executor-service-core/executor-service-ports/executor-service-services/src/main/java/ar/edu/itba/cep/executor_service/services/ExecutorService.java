package ar.edu.itba.cep.executor_service.services;

import ar.edu.itba.cep.executor_service.models.ExecutionRequest;
import ar.edu.itba.cep.executor_service.models.ExecutionResult;

/**
 * A port into the application that allows requesting an execution.
 */
public interface ExecutorService {

    /**
     * Processes the given {@code executionRequest}.
     *
     * @param executionRequest The {@link ExecutionRequest} to be processed.
     * @return The {@link ExecutionResult} that came up of after processing the given {@code executionRequest}.
     * @throws IllegalArgumentException If the given {@code executionRequest} is invalid.
     */
    ExecutionResult processExecutionRequest(final ExecutionRequest executionRequest) throws IllegalArgumentException;
}
