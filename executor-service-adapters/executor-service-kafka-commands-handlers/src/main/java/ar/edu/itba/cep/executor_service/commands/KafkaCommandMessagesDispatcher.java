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
    private final MessageHandler messageHandler;

    @Autowired
    public KafkaCommandMessagesDispatcher(final ExecutorServiceAdapter executorServiceAdapter) {
        this.messageHandler = BuiltInMessageHandler.Builder.create()
                .configureTypedMessageHandlers()
                .configureCommandMessageHandlers()
                .handleCommandWith("requestExecution", executorServiceAdapter::handleExecutionRequest)
                .continueWithParentBuilder()
                .continueWithParentBuilder()
                .build();
    }

    /**
     * Receives a {@link Message}s and delegates it handling to the {@code messageHandler}.
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
        this.messageHandler.handle(message);
    }
}
