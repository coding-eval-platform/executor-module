package ar.edu.itba.cep.executor_service.commands.config;

import ar.edu.itba.cep.executor.dtos.ExecutionRequestDto;
import ar.edu.itba.cep.executor.dtos.ExecutionResponseDto;
import com.bellotapps.the_messenger.commons.Message;
import com.bellotapps.the_messenger.commons.payload.PayloadDeserializer;
import com.bellotapps.the_messenger.commons.payload.PayloadSerializer;
import com.bellotapps.the_messenger.json.JacksonJsonPayloadDeserializer;
import com.bellotapps.the_messenger.json.JacksonJsonPayloadSerializer;
import com.bellotapps.the_messenger.producer.BiConsumerMessageProducer;
import com.bellotapps.the_messenger.producer.MessageBuilderFactory;
import com.bellotapps.the_messenger.producer.MessageProducer;
import com.bellotapps.the_messenger.producer.basic_factories.GenericMessageBuilderFactory;
import com.bellotapps.the_messenger.transport.json.jackson.JacksonMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * Configuration class for the Kafka command message handling module.
 */
@Configuration
@ComponentScan(basePackages = {
        "ar.edu.itba.cep.executor_service.commands"
})
@EnableConfigurationProperties({
        ExecutionResponseHandlerProperties.class,
})
public class KafkaCommandsHandlersConfig {

    /**
     * Prefix for properties of the Kafka Command Handlers module.
     */
    /* package */ static final String PREFIX = "command-handler";


    /**
     * Creates a bean of {@link PayloadDeserializer} of {@link ExecutionRequestDto}.
     *
     * @return A bean of {@link JacksonJsonPayloadDeserializer} of {@link ExecutionRequestDto}.
     */
    @Bean
    public PayloadDeserializer<ExecutionRequestDto> executionRequestDtoPayloadDeserializer() {
        return new JacksonJsonPayloadDeserializer<>(new ObjectMapper(), ExecutionRequestDto.class);
    }

    /**
     * Creates a bean of {@link PayloadSerializer} of {@link ExecutionRequestDto}.
     *
     * @return A bean of {@link JacksonJsonPayloadSerializer} of {@link ExecutionResponseDto}.
     */
    @Bean
    public PayloadSerializer<ExecutionResponseDto> executionResponseDtoPayloadSerializer() {
        return new JacksonJsonPayloadSerializer<>(new ObjectMapper(), ExecutionResponseDto.class);
    }

    /**
     * Creates a bean of {@link BiConsumerMessageProducer} that allows sending messages.
     *
     * @param kafkaTemplate The underlying {@link KafkaTemplate} used by the {@link MessageProducer}.
     * @return A bean of {@link MessageProducer}.
     */
    @Bean
    public MessageProducer messageProducer(final KafkaTemplate kafkaTemplate) {
        @SuppressWarnings("unchecked") final var template = (KafkaTemplate<String, Message>) kafkaTemplate;
        return new BiConsumerMessageProducer((message, channel) -> template.send(channel, message));
    }

    /**
     * Creates a bean of {@link GenericMessageBuilderFactory} of {@link ExecutionResponseDto}.
     *
     * @param executionResponseDtoPayloadSerializer A {@link PayloadSerializer} of {@link ExecutionResponseDto}.
     * @return A bean of {@link MessageBuilderFactory} of {@link ExecutionResponseDto}.
     */
    @Bean
    public MessageBuilderFactory<ExecutionResponseDto> executionResponseMessageBuilderFactory(
            final PayloadSerializer<ExecutionResponseDto> executionResponseDtoPayloadSerializer) {
        return new GenericMessageBuilderFactory<>(
                "ExecutorService",
                executionResponseDtoPayloadSerializer,
                JacksonMessage::new
        );
    }
}
