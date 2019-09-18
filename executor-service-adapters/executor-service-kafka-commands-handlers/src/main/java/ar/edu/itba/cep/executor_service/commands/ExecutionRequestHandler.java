package ar.edu.itba.cep.executor_service.commands;

import ar.edu.itba.cep.executor.dtos.ExecutionRequestDto;
import ar.edu.itba.cep.executor.models.ExecutionRequest;
import ar.edu.itba.cep.executor_service.services.ExecutorService;
import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.payload.PayloadDeserializer;
import com.bellotapps.the_messenger.consumer.DeserializerMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adapts an {@link ExecutorService} into the Kafka Command handlers infrastructure.
 * Implemented as a {@link DeserializerMessageHandler} of {@link ExecutionRequestDto} that takes data from the
 * request dto and calls the {@link ExecutorService#processExecutionRequest(ExecutionRequest)} method.
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
    private final ExecutionResponseHandler executionResponseHandler;


    /**
     * Constructor.
     *
     * @param executionRequestDtoDeserializer A {@link PayloadDeserializer} of {@link ExecutionRequestDto}.
     * @param executorService                 The {@link ExecutorService} being adapted.
     * @param executionResponseHandler        An {@link ExecutionRequestHandler}
     *                                        in charge of returning results to the execution requester.
     */
    @Autowired
    public ExecutionRequestHandler(
            final PayloadDeserializer<ExecutionRequestDto> executionRequestDtoDeserializer,
            final ExecutorService executorService,
            final ExecutionResponseHandler executionResponseHandler) {
        super(executionRequestDtoDeserializer);
        this.executorService = executorService;
        this.executionResponseHandler = executionResponseHandler;

    }


    @Override
    protected void andThen(final ExecutionRequestDto executionRequestDto, final Message message) {
        final var executionResponse = executorService.processExecutionRequest(executionRequestDto.getExecutionRequest());
        executionResponseHandler.sendExecutionResponse(message, executionResponse);
    }
}
