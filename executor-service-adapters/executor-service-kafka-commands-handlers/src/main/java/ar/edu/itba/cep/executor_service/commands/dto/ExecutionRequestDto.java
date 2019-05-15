package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor_service.models.ExecutionRequest;
import ar.edu.itba.cep.executor_service.models.Language;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data transfer object for receiving {@link ExecutionRequest}s.
 */
public class ExecutionRequestDto {

    /**
     * The {@link ExecutionRequest} that is created from the received JSON.
     */
    private final ExecutionRequest executionRequest;


    /**
     * Constructor.
     *
     * @param code     The code to be run.
     * @param inputs   The input arguments to be passed to the execution.
     * @param timeout  The time given to execute, in milliseconds.
     * @param language The programming language in which the {@code code} is written.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    @JsonCreator
    public ExecutionRequestDto(
            @JsonProperty(value = "code", access = JsonProperty.Access.WRITE_ONLY) final String code,
            @JsonProperty(value = "inputs", access = JsonProperty.Access.WRITE_ONLY) final List<String> inputs,
            @JsonProperty(value = "timeout", access = JsonProperty.Access.WRITE_ONLY) final Long timeout,
            @JsonProperty(value = "language", access = JsonProperty.Access.WRITE_ONLY) final Language language) {
        this.executionRequest = new ExecutionRequest(code, inputs, timeout, language);
    }

    /**
     * @return The {@link ExecutionRequest} that is created from the received JSON.
     */
    public ExecutionRequest getExecutionRequest() {
        return executionRequest;
    }
}
