package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor_service.models.CompileErrorExecutionResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * An {@link ExecutionRequestDto} for {@link CompileErrorExecutionResult}s.
 */
/* package */ class CompileErrorExecutionResultDto extends ExecutionResultDto<CompileErrorExecutionResult> {

    /**
     * Constructor.
     *
     * @param compileErrorExecutionResult The {@link CompileErrorExecutionResult} being wrapped by this DTO.
     * @throws IllegalArgumentException If the given {@code compileErrorExecutionResult} is {@code null}.
     */
    /* package */ CompileErrorExecutionResultDto(final CompileErrorExecutionResult compileErrorExecutionResult)
            throws IllegalArgumentException {
        super(compileErrorExecutionResult);
    }


    /**
     * @return A {@link List} of {@link String}s that were reported by the compiler on failure.
     */
    @JsonProperty(value = "compilerErrors", access = JsonProperty.Access.READ_ONLY)
    public List<String> getCompilerErrors() {
        return getExecutionResult().getCompilerErrors();
    }
}
