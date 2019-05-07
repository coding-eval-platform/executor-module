package ar.edu.itba.cep.executor_service.commands.config;

import ar.edu.itba.cep.executor_service.commands.dto.ExecutionRequestDto;
import ar.edu.itba.cep.executor_service.commands.dto.ExecutionResultDto;
import com.bellotapps.the_messenger.commons.data_transfer.Deserializer;
import com.bellotapps.the_messenger.commons.data_transfer.Serializer;
import com.bellotapps.the_messenger.json.JacksonJsonDeserializer;
import com.bellotapps.the_messenger.json.JacksonJsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Kafka command message handling module.
 */
@Configuration
@ComponentScan(basePackages = {
        "ar.edu.itba.cep.executor_service.commands"
})
public class KafkaConfig {

    /**
     * Creates a bean of {@link Deserializer} of {@link ExecutionRequestDto}.
     *
     * @return A {@link JacksonJsonDeserializer} of {@link ExecutionRequestDto}.
     */
    @Bean
    public Deserializer<ExecutionRequestDto> executionRequestDtoDeserializer() {
        return new JacksonJsonDeserializer<>(new ObjectMapper(), ExecutionRequestDto.class);
    }

    /**
     * Creates a bean of {@link Serializer} of {@link ExecutionRequestDto}.
     *
     * @return A {@link JacksonJsonSerializer} of {@link ExecutionResultDto}.
     */
    @Bean
    public Serializer<ExecutionResultDto> executionResultDtoSerializer() {
        return new JacksonJsonSerializer<>(new ObjectMapper(), ExecutionResultDto.class);
    }
}
