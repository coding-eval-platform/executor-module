package ar.edu.itba.cep.executor_service.commands;

import ar.edu.itba.cep.executor.Constants;
import ar.edu.itba.cep.executor.dtos.ExecutionResponseDto;
import ar.edu.itba.cep.executor.models.ExecutionResponse;
import ar.edu.itba.cep.executor_service.commands.config.ExecutionResponseHandlerProperties;
import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.producer.MessageBuilderFactory;
import com.bellotapps.the_messenger.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component in charge of returning a response with an {@link ExecutionResponse}
 * to the one that send an {@link ar.edu.itba.cep.executor.models.ExecutionRequest}.
 */
@Component
public class ExecutionResponseHandler {

    /**
     * The {@link MessageProducer} in charge of sending the {@link Message}.
     */
    private final MessageProducer messageProducer;

    /**
     * A {@link MessageBuilderFactory} of {@link ExecutionResponseDto} that creates the
     * {@link com.bellotapps.the_messenger.producer.MessageBuilder} that can create the response {@link Message}s.
     */
    private final MessageBuilderFactory<ExecutionResponseDto> executionResponseDtoMessageBuilderFactory;

    /**
     * The default reply channel (i.e used in case the requested did not include a reply channel header).
     */
    private final String defaultReplyChannel;


    /**
     * @param messageProducer                           The {@link MessageProducer}
     *                                                  in charge of sending the {@link Message}.
     * @param executionResponseDtoMessageBuilderFactory A {@link MessageBuilderFactory} of {@link ExecutionResponseDto}
     *                                                  that creates the
     *                                                  {@link com.bellotapps.the_messenger.producer.MessageBuilder}
     *                                                  that can create the response {@link Message}s.
     * @param properties                                An instance of {@link ExecutionResponseHandlerProperties}
     *                                                  with values to configure this compoent.
     */
    @Autowired
    public ExecutionResponseHandler(
            final MessageProducer messageProducer,
            final MessageBuilderFactory<ExecutionResponseDto> executionResponseDtoMessageBuilderFactory,
            final ExecutionResponseHandlerProperties properties) {
        this.messageProducer = messageProducer;
        this.executionResponseDtoMessageBuilderFactory = executionResponseDtoMessageBuilderFactory;
        this.defaultReplyChannel = properties.getDefaultReplyChannel();
    }


    /**
     * Sends the {@link ExecutionResponse} in response to the given {@code incomingMessage}.
     *
     * @param incomingMessage   The {@link Message} being responded.
     * @param executionResponse The {@link ExecutionResponse} to be sent to the requester.
     */
    public void sendExecutionResponse(final Message incomingMessage, final ExecutionResponse executionResponse) {
        final var replyChannel = incomingMessage
                .headerValue(Constants.REPLY_CHANNEL_HEADER)
                .orElse(defaultReplyChannel);
        final var message = executionResponseDtoMessageBuilderFactory.replyMessage(incomingMessage)
                .withPayload(ExecutionResponseDto.buildFromResponse(executionResponse))
                .build();
        messageProducer.send(message, replyChannel);
    }
}
