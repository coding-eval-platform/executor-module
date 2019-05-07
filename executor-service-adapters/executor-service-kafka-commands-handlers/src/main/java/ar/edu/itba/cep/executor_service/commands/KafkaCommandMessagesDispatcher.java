package ar.edu.itba.cep.executor_service.commands;

import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.consumer.BuiltInMessageHandler;
import com.bellotapps.the_messenger.consumer.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka command messages dispatcher.
 */
@Component
public class KafkaCommandMessagesDispatcher {

    /**
     * The {@link MessageHandler} in charge of dispatching actions based on received messages.
     */
    private final MessageHandler dispatcherMessageHandler;

    /**
     * @param executionRequestHandler The {@link MessageHandler}
     *                                in charge of handling execution request command messages.
     */
    @Autowired
    public KafkaCommandMessagesDispatcher(final MessageHandler executionRequestHandler) {
        this.dispatcherMessageHandler = BuiltInMessageHandler.Builder.create()
                .configureTypedMessageHandlers()
                .configureCommandMessageHandlers()
                .handleCommandWith("requestExecution", executionRequestHandler)
                .continueWithParentBuilder()
                .continueWithParentBuilder()
                .build();
    }


    /**
     * Receives a {@link Message}s and delegates its handling to the {@code dispatcherMessageHandler}.
     *
     * @param message The received {@link Message}.
     */
    @KafkaListener(
            topics = {
                    "ExecutorService-Commands",
            },
            autoStartup = "true"
    )
    public void dispatch(final Message message) {
        this.dispatcherMessageHandler.handle(message);
    }
}
