package ar.edu.itba.cep.executor_service.commands;

import ar.edu.itba.cep.executor_service.commands.dto.ExecutionResultDto;
import ar.edu.itba.cep.executor_service.models.ExecutionResult;
import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.data_transfer.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

// TODO: finish with this later!
/**
 *
 */
@Component
public class ExecutionResultHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final Serializer<ExecutionResultDto> executionResultDtoSerializer;

    @Autowired
    @SuppressWarnings("unchecked")
    public ExecutionResultHandler(
            final KafkaTemplate kafkaTemplate,
            final Serializer<ExecutionResultDto> executionResultDtoSerializer) {
        this.kafkaTemplate = (KafkaTemplate<String, Object>) kafkaTemplate;
        this.executionResultDtoSerializer = executionResultDtoSerializer;
    }

    public void sendExecutionResult(final Message incomingMessage, final ExecutionResult executionResult) {
        final var dto = ExecutionResultDto.createFor(executionResult);

        final var copyHeadersValues = incomingMessage.copyHeadersValues();


        final var messageBuilder = Message.Builder.replyMessage(incomingMessage.getId())
                .from("ExecutorService")
                .withPayload(executionResultDtoSerializer.serialize(dto)) // TODO: check how to do this
                .json();
        copyHeadersValues.forEach(messageBuilder::withHeader);
        kafkaTemplate.send("EvaluationsService-Command-Replies", messageBuilder.build());
    }
}
