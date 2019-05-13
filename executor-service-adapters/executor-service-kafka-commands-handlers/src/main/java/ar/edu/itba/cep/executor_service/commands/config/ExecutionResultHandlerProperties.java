package ar.edu.itba.cep.executor_service.commands.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the {@link ar.edu.itba.cep.executor_service.commands.ExecutionResultHandler}.
 */
@ConfigurationProperties(prefix = ExecutionResultHandlerProperties.PREFIX)
public class ExecutionResultHandlerProperties {

    /**
     * Prefix for the properties of the Result handler.
     */
    /* package */ static final String PREFIX = KafkaCommandsHandlersConfig.PREFIX + "." + "result-handler";


    /**
     * The base working directory.
     */
    private String defaultReplyChannel = "ExecutorService-Execution-Results";


    /**
     * Setter for the default reply channel property.
     *
     * @return The default reply channel property.
     */
    public String getDefaultReplyChannel() {
        return defaultReplyChannel;
    }

    /**
     * Setter for the default reply channel property.
     *
     * @param defaultReplyChannel the default reply channel.
     */
    public void setDefaultReplyChannel(final String defaultReplyChannel) {
        this.defaultReplyChannel = defaultReplyChannel;
    }
}
