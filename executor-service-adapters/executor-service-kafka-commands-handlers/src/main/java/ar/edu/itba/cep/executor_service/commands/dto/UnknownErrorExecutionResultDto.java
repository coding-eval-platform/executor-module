package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor_service.models.UnknownErrorExecutionResult;

/**
 * An {@link ExecutionRequestDto} for {@link UnknownErrorExecutionResult}s.
 */
/* package */ class UnknownErrorExecutionResultDto extends ExecutionResultDto<UnknownErrorExecutionResult> {

    /**
     * Constructor.
     *
     * @param unknownErrorExecutionResult The {@link UnknownErrorExecutionResult} being wrapped by this DTO.
     * @throws IllegalArgumentException If the given {@code unknownErrorExecutionResult} is {@code null}.
     */
    /* package */ UnknownErrorExecutionResultDto(final UnknownErrorExecutionResult unknownErrorExecutionResult) {
        super(unknownErrorExecutionResult);
    }
}
