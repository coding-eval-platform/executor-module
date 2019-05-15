package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor_service.models.FinishedExecutionResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * An {@link ExecutionRequestDto} for {@link FinishedExecutionResult}s.
 */
/* package */ class FinishedExecutionResultDto extends ExecutionResultDto<FinishedExecutionResult> {


    /**
     * Constructor.
     *
     * @param finishedExecutionResult The {@link FinishedExecutionResult} being wrapped by this DTO.
     * @throws IllegalArgumentException If the given {@code finishedExecutionResult} is {@code null}.
     */
    /* package */ FinishedExecutionResultDto(final FinishedExecutionResult finishedExecutionResult)
            throws IllegalArgumentException {
        super(finishedExecutionResult);
    }


    /**
     * @return The execution's exit code.
     */
    @JsonProperty(value = "exitCode", access = JsonProperty.Access.READ_ONLY)
    public int getExitCode() {
        return getExecutionResult().getExitCode();
    }

    /**
     * @return A {@link List} of {@link String}s that were sent to standard output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard output.
     */
    @JsonProperty(value = "stdout", access = JsonProperty.Access.READ_ONLY)
    public List<String> getStdout() {
        return getExecutionResult().getStdout();
    }

    /**
     * @return A {@link List} of {@link String}s that were sent to standard error output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard error output.
     */
    @JsonProperty(value = "stderr", access = JsonProperty.Access.READ_ONLY)
    public List<String> getStderr() {
        return getExecutionResult().getStderr();
    }
}
