package ar.edu.itba.cep.executor_service.commands;

import ar.edu.itba.cep.executor_service.commands.dto.ExecutionRequestDto;
import ar.edu.itba.cep.executor_service.models.Language;
import ar.edu.itba.cep.executor_service.services.ExecutorService;
import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.data_transfer.Deserializer;
import com.bellotapps.the_messenger.consumer.DeserializerMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adapts an {@link ExecutorService} into the Kafka Command handlers infrastructure.
 * Implemented as a {@link DeserializerMessageHandler} of {@link ExecutionRequestDto} that takes data from the
 * request dto and calls the {@link ExecutorService#runCode(String, List, Long, Language)} method.
 */
@Component
public class ExecutionRequestHandler extends DeserializerMessageHandler<ExecutionRequestDto> {

    /**
     * The {@link ExecutorService} being adapted.
     */
    private final ExecutorService executorService;

    /**
     * An {@link ExecutionRequestHandler} in charge of returning results to the execution requester.
     */
    private final ExecutionResultHandler executionResultHandler;


    /**
     * Constructor.
     *
     * @param executionRequestDtoDeserializer A {@link Deserializer} of {@link ExecutionRequestDto}.
     * @param executorService                 The {@link ExecutorService} being adapted.
     * @param executionResultHandler          An {@link ExecutionRequestHandler}
     *                                        in charge of returning results to the execution requester.
     */
    @Autowired
    public ExecutionRequestHandler(
            final Deserializer<ExecutionRequestDto> executionRequestDtoDeserializer,
            final ExecutorService executorService,
            final ExecutionResultHandler executionResultHandler) {
        super(executionRequestDtoDeserializer);
        this.executorService = executorService;
        this.executionResultHandler = executionResultHandler;

    }


    @Override
    protected void andThen(final ExecutionRequestDto executionRequestDto, final Message message) {
        final var executionResult = executorService.processExecutionRequest(executionRequestDto.getExecutionRequest());
        executionResultHandler.sendExecutionResult(message, executionResult);
    }
}
