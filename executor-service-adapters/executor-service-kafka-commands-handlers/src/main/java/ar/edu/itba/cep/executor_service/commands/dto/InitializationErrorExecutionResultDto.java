package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor_service.models.InitializationErrorExecutionResult;

/**
 * An {@link ExecutionRequestDto} for {@link InitializationErrorExecutionResult}s.
 */
/* package */ class InitializationErrorExecutionResultDto extends ExecutionResultDto<InitializationErrorExecutionResult> {

    /**
     * Constructor.
     *
     * @param initializationErrorExecutionResult The {@link InitializationErrorExecutionResult}
     *                                           being wrapped by this DTO.
     * @throws IllegalArgumentException If the given {@code initializationErrorExecutionResult} is {@code null}.
     */
    /* package */ InitializationErrorExecutionResultDto(
            final InitializationErrorExecutionResult initializationErrorExecutionResult) {
        super(initializationErrorExecutionResult);
    }
}
