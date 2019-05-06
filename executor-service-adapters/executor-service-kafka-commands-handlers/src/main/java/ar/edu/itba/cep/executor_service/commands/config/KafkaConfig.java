package ar.edu.itba.cep.executor_service.commands.config;

import ar.edu.itba.cep.executor_service.commands.dto.ExecutionRequestDto;
import com.bellotapps.the_messenger.commons.data_transfer.Deserializer;
import com.bellotapps.the_messenger.json.JacksonJsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the Kafka command handling module.
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
    public Deserializer<ExecutionRequestDto> executionRequestDeserializer() {
        return new JacksonJsonDeserializer<>(new ObjectMapper(), ExecutionRequestDto.class);
    }
}
