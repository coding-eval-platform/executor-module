package ar.edu.itba.cep.executor_service.commands.dto;

import ar.edu.itba.cep.executor_service.models.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.util.Assert;

/**
 * Abstract class for {@link ExecutionResult} data transfer object.
 * Includes subtyping metadata that indicates how to serialize objects that implement this interface into JSONs.
 *
 * @param <R> Concrete subtype of {@link ExecutionResult} to be wrapped by this DTO.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = FinishedExecutionResultDto.class,
                name = ExecutionResultDto.FINISHED_STRING_VALUE
        ),
        @JsonSubTypes.Type(
                value = TimedOutExecutionResultDto.class,
                name = ExecutionResultDto.TIMED_OUT_STRING_VALUE
        ),
        @JsonSubTypes.Type(
                value = CompileErrorExecutionResult.class,
                name = ExecutionResultDto.COMPILED_ERROR_STRING_VALUE
        ),
        @JsonSubTypes.Type(
                value = InitializationErrorExecutionResult.class,
                name = ExecutionResultDto.INITIALIZATION_ERROR_STRING_VALUE
        ),
        @JsonSubTypes.Type(
                value = UnknownErrorExecutionResult.class,
                name = ExecutionResultDto.UNKNOWN_ERROR_STRING_VALUE
        )
})
public abstract class ExecutionResultDto<R extends ExecutionResult> {

    /**
     * Value that will be included in JSONs created from a {@link FinishedExecutionResultDto}.
     */
    /* package */ static final String FINISHED_STRING_VALUE = "FINISHED";
    /**
     * Value that will be included in JSONs created from a {@link TimedOutExecutionResult}.
     */
    /* package */ static final String TIMED_OUT_STRING_VALUE = "TIMED_OUT";
    /**
     * Value that will be included in JSONs created from a {@link FinishedExecutionResultDto}.
     */
    /* package */ static final String COMPILED_ERROR_STRING_VALUE = "COMPILE_ERROR";
    /**
     * Value that will be included in JSONs created from a {@link FinishedExecutionResultDto}.
     */
    /* package */ static final String INITIALIZATION_ERROR_STRING_VALUE = "INITIALIZATION_ERROR";
    /**
     * Value that will be included in JSONs created from a {@link FinishedExecutionResultDto}.
     */
    /* package */ static final String UNKNOWN_ERROR_STRING_VALUE = "UNKNOWN_ERROR";


    /**
     * The {@link ExecutionResult} being wrapped by this DTO.
     */
    private final R executionResult;

    /**
     * Constructor.
     *
     * @param executionResult The {@link ExecutionResult} being wrapped by this DTO.
     * @throws IllegalArgumentException If the given {@code executionResult} is {@code null}.
     */
    /* package */ ExecutionResultDto(final R executionResult) {
        Assert.notNull(executionResult, "The given execution result must not be null!");
        this.executionResult = executionResult;
    }

    /**
     * @return The {@link ExecutionResult} being wrapped by this DTO.
     */
    /* package */ R getExecutionResult() {
        return executionResult;
    }


    /**
     * Factory method that creates an {@link ExecutionRequestDto} from an {@link ExecutionResult}.
     *
     * @param executionResult The {@link ExecutionResult} from which the created {@link ExecutionRequestDto} will
     *                        be created.
     * @return The created {@link ExecutionRequestDto}.
     * @throws IllegalArgumentException If the given {@code executionResult} is {@code null}.
     */
    public static ExecutionResultDto createFor(final ExecutionResult executionResult) throws IllegalArgumentException {
        Assert.notNull(executionResult, "The execution result must not be null");
        if (executionResult instanceof FinishedExecutionResult) {
            return new FinishedExecutionResultDto((FinishedExecutionResult) executionResult);
        }
        if (executionResult instanceof TimedOutExecutionResult) {
            return new TimedOutExecutionResultDto((TimedOutExecutionResult) executionResult);
        }
        if (executionResult instanceof CompileErrorExecutionResult) {
            return new CompileErrorExecutionResultDto((CompileErrorExecutionResult) executionResult);
        }
        if (executionResult instanceof InitializationErrorExecutionResult) {
            return new InitializationErrorExecutionResultDto((InitializationErrorExecutionResult) executionResult);
        }
        if (executionResult instanceof UnknownErrorExecutionResult) {
            return new UnknownErrorExecutionResultDto((UnknownErrorExecutionResult) executionResult);
        }
        throw new IllegalArgumentException("Unknown subtype");
    }
}
