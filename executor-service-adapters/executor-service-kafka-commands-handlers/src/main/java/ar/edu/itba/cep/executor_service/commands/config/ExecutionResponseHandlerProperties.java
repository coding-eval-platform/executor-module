package ar.edu.itba.cep.executor_service.commands.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the {@link ar.edu.itba.cep.executor_service.commands.ExecutionResponseHandler}.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = ExecutionResponseHandlerProperties.PREFIX)
public class ExecutionResponseHandlerProperties {

    /**
     * Prefix for the properties of the response handler.
     */
    /* package */ static final String PREFIX = KafkaCommandsHandlersConfig.PREFIX + "." + "response-handler";


    /**
     * The base working directory.
     */
    private String defaultReplyChannel = "ExecutorService-Execution-Responses";
}
