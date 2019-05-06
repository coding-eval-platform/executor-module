package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor_service.models.Language;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data transfer object for receiving execution requests.
 */
public class ExecutionRequestDto {

    /**
     * The code to be run.
     */
    private final String code;

    /**
     * The input arguments to be passed to the execution.
     */
    private final List<String> inputs;

    /**
     * The time given to execute, in milliseconds..
     */
    private final Long timeout;

    /**
     * The programming language in which the {@link #code} is written.
     */
    private final Language language;


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
        this.code = code;
        this.inputs = inputs;
        this.timeout = timeout;
        this.language = language;
    }


    /**
     * @return The code to be run.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return The input arguments to be passed to the execution.
     */
    public List<String> getInputs() {
        return inputs;
    }

    /**
     * @return The time given to execute, in milliseconds.
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * @return The programming language in which the {@link #getCode()} is written.
     */
    public Language getLanguage() {
        return language;
    }
}
