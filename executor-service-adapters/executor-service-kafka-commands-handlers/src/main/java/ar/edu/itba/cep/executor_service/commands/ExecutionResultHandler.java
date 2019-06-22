package ar.edu.itba.cep.executor_service.commands;

import ar.edu.itba.cep.executor_service.commands.config.ExecutionResultHandlerProperties;
import ar.edu.itba.cep.executor_service.commands.dto.ExecutionResultDto;
import ar.edu.itba.cep.executor_service.models.ExecutionResult;
import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.producer.MessageBuilderFactory;
import com.bellotapps.the_messenger.producer.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component in charge of returning a response with an {@link ExecutionResult}
 * to the one that send an {@link ar.edu.itba.cep.executor_service.models.ExecutionRequest}.
 */
@Component
public class ExecutionResultHandler {

    /**
     * The {@link MessageProducer} in charge of sending the {@link Message}.
     */
    private final MessageProducer messageProducer;

    /**
     * A {@link MessageBuilderFactory} of {@link ExecutionResultDto} that creates the
     * {@link com.bellotapps.the_messenger.producer.MessageBuilder} that can create the response {@link Message}s.
     */
    private final MessageBuilderFactory<ExecutionResultDto> executionResultMessageBuilderFactory;

    /**
     * The default reply channel (i.e used in case the requested did not include a reply channel header).
     */
    private final String defaultReplyChannel;


    /**
     * @param messageProducer                      The {@link MessageProducer} in charge of sending the {@link Message}.
     * @param executionResultMessageBuilderFactory A {@link MessageBuilderFactory} of {@link ExecutionResultDto}
     *                                             that creates the
     *                                             {@link com.bellotapps.the_messenger.producer.MessageBuilder}
     *                                             that can create the response {@link Message}s.
     * @param properties                           An instance of {@link ExecutionResultHandlerProperties}
     *                                             with values to configure this compoent.
     */
    @Autowired
    public ExecutionResultHandler(
            final MessageProducer messageProducer,
            final MessageBuilderFactory<ExecutionResultDto> executionResultMessageBuilderFactory,
            final ExecutionResultHandlerProperties properties) {
        this.messageProducer = messageProducer;
        this.executionResultMessageBuilderFactory = executionResultMessageBuilderFactory;
        this.defaultReplyChannel = properties.getDefaultReplyChannel();
    }


    /**
     * Sends the {@link ExecutionResult} in response to the given {@code incomingMessage}.
     *
     * @param incomingMessage The {@link Message} being responded.
     * @param executionResult The {@link ExecutionResult} to be sent to the requester.
     */
    public void sendExecutionResult(final Message incomingMessage, final ExecutionResult executionResult) {
        final var replyChannel = incomingMessage
                .headerValue(Constants.REPLY_CHANNEL_HEADER)
                .orElse(defaultReplyChannel);
        final var message = executionResultMessageBuilderFactory.replyMessage(incomingMessage)
                .withPayload(ExecutionResultDto.createFor(executionResult))
                .build();
        messageProducer.send(message, replyChannel);
    }
}
