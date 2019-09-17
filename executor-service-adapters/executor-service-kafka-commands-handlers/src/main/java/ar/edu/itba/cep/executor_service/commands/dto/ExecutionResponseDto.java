package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor.models.ExecutionResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * Data transfer object for receiving {@link ExecutionResponse}s.
 */
@AllArgsConstructor(staticName = "build")
public class ExecutionResponseDto {

    /**
     * The {@link ExecutionResponse} being wrapped by this DTO.
     */
    private final ExecutionResponse executionResponse;


    /**
     * @return The execution's result.
     */
    @JsonProperty(value = "resul", access = JsonProperty.Access.READ_ONLY)
    public ExecutionResponse.ExecutionResult getResult() {
        return executionResponse.getResult();
    }

    /**
     * @return The execution's exit code.
     */
    @JsonProperty(value = "exitCode", access = JsonProperty.Access.READ_ONLY)
    public int getExitCode() {
        return executionResponse.getExitCode();
    }

    /**
     * @return A {@link List} of {@link String}s that were sent to standard output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard output.
     */
    @JsonProperty(value = "stdout", access = JsonProperty.Access.READ_ONLY)
    public List<String> getStdout() {
        return executionResponse.getStdout();
    }

    /**
     * @return A {@link List} of {@link String}s that were sent to standard error output by the program being executed.
     * Each {@link String} in the {@link List} is a line that was printed in standard error output.
     */
    @JsonProperty(value = "stderr", access = JsonProperty.Access.READ_ONLY)
    public List<String> getStderr() {
        return executionResponse.getStderr();
    }
}
