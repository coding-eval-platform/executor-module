package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor.models.ExecutionRequest;
import ar.edu.itba.cep.executor.models.Language;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * Data transfer object for receiving {@link ExecutionRequest}s.
 */
@Getter
public class ExecutionRequestDto {

    /**
     * The {@link ExecutionRequest} that is created from the received JSON.
     */
    private final ExecutionRequest executionRequest;


    /**
     * Constructor.
     *
     * @param code             The code to be run.
     * @param programArguments The program arguments to be passed to the execution.
     * @param stdin            The standard input to be passed to the execution.
     * @param timeout          The time given to execute, in milliseconds.
     * @param language         The programming language in which the {@code code} is written.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    @JsonCreator
    public ExecutionRequestDto(
            @JsonProperty(value = "code", access = WRITE_ONLY) final String code,
            @JsonProperty(value = "programArguments", access = WRITE_ONLY) final List<String> programArguments,
            @JsonProperty(value = "stdin", access = WRITE_ONLY) final List<String> stdin,
            @JsonProperty(value = "timeout", access = WRITE_ONLY) final Long timeout,
            @JsonProperty(value = "language", access = WRITE_ONLY) final Language language) {
        this.executionRequest = new ExecutionRequest(code, programArguments, stdin, timeout, language);
    }
}
