package ar.edu.itba.cep.executor_service.commands;

import ar.edu.itba.cep.executor_service.commands.dto.ExecutionRequestDto;
import ar.edu.itba.cep.executor_service.services.ExecutorService;
import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.data_transfer.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Adapts an {@link ExecutorService} into the Kafka Command handlers infrastructure.
 */
@Component
public class ExecutorServiceAdapter {

    /**
     * The {@link ExecutorService} being adapted.
     */
    private final ExecutorService executorService;

    /**
     * A {@link Deserializer} of {@link ExecutionRequestDto}.
     */
    private final Deserializer<ExecutionRequestDto> executionRequestDtoDeserializer;


    /**
     * Constructor.
     *
     * @param executorService                 The {@link ExecutorService} being adapted.
     * @param executionRequestDtoDeserializer A {@link Deserializer} of {@link ExecutionRequestDto}.
     */
    @Autowired
    public ExecutorServiceAdapter(
            final ExecutorService executorService,
            final Deserializer<ExecutionRequestDto> executionRequestDtoDeserializer) {
        this.executorService = executorService;
        this.executionRequestDtoDeserializer = executionRequestDtoDeserializer;
    }


    /**
     * Handles the given {@code message}, extracting an {@link ExecutionRequestDto} from it.
     *
     * @param message The {@link Message} to be handled.
     */
    /* package */ void handleExecutionRequest(final Message message) {
        System.out.println(message.getPayload());
        // TODO: try-catch in case another thing is received.
        final var executionRequestDto = executionRequestDtoDeserializer.deserialize(message.getPayload());

        // First handle the request.
        final var executionResult = this.executorService.runCode(
                executionRequestDto.getCode(),
                executionRequestDto.getInputs(),
                executionRequestDto.getTimeout(),
                executionRequestDto.getLanguage()
        );
        System.out.println(executionResult); // TODO: send reply
    }
}
