package ar.edu.itba.cep.executor_service.runner.config;

import ar.edu.itba.cep.executor_service.runner.CodeRunner;
import ar.edu.itba.cep.executor_service.runner.OperatingSystemProcessCodeRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the processes code runner module.
 */
@Configuration
@ComponentScan(basePackages = {
        "ar.edu.itba.cep.executor_service.runner"
})
@EnableConfigurationProperties({
        OperatingSystemProcessCodeRunnerProperties.class,
})
public class RunnerConfig {

    /**
     * Creates a bean of the {@link OperatingSystemProcessCodeRunner}.
     *
     * @param properties The {@link OperatingSystemProcessCodeRunnerProperties} to be used.
     * @return The created {@link CodeRunner}.
     */
    @Bean
    public CodeRunner codeRunner(final OperatingSystemProcessCodeRunnerProperties properties) {
        return new OperatingSystemProcessCodeRunner(
                properties.getPreRun().getCommand(),
                properties.getPreRun().getArgs(),
                properties.getPreRun().getTimeout(),
                properties.getPreRun().isContinueOnNonZeroExitCode(),
                properties.getRunner().getCommand(),
                properties.getRunner().getArgs(),
                properties.getRunner().getDefaultTimeout(),
                properties.getBaseWorkingDirectory()
        );
    }
}
