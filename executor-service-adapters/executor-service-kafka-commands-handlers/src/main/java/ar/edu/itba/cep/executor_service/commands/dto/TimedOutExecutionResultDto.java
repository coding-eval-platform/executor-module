package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor_service.models.TimedOutExecutionResult;

/**
 * An {@link ExecutionRequestDto} for {@link TimedOutExecutionResult}s.
 */
/* package */ class TimedOutExecutionResultDto extends ExecutionResultDto<TimedOutExecutionResult> {

    /**
     * Constructor.
     *
     * @param timedOutExecutionResult The {@link TimedOutExecutionResult} being wrapped by this DTO.
     * @throws IllegalArgumentException If the given {@code timedOutExecutionResult} is {@code null}.
     */
    /* package */ TimedOutExecutionResultDto(final TimedOutExecutionResult timedOutExecutionResult) {
        super(timedOutExecutionResult);
    }
}
