package ar.edu.itba.cep.executor_service.runner.config;

import ar.edu.itba.cep.executor_service.models.Language;
import ar.edu.itba.cep.executor_service.runner.OperatingSystemProcessCodeRunner;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties for the {@link OperatingSystemProcessCodeRunner}.
 */
@ConfigurationProperties(prefix = OperatingSystemProcessCodeRunnerProperties.PREFIX)
@Getter
@Setter
class OperatingSystemProcessCodeRunnerProperties {

    /**
     * Prefix for these properties.
     */
    /* package */ static final String PREFIX = "code-runner";


    /**
     * Base working directory for the runner.
     * New directories will be created here where each execution will be performed.
     * Default value: /tmp/.
     */
    private String baseWorkingDirectory = "/tmp/";

    /**
     * Timeout to be given to the runner command process in case it hangs out.
     * This is different than the execution timeout, which is used to evaluate efficiency and performance of code.
     * Default value: one hour.
     */
    private long processTimeout = 3600000; // One hour in milliseconds

    /**
     * A {@link Map} containing the commands to be used for each {@link Language}.
     * This commands can be OS shell native commands, shell script files, executable files, custom programs, etc.
     * The only requirement is that a {@link Process} can be started using the values of the {@link Map}.
     * See the execve <a href=http://man7.org/linux/man-pages/man2/execve.2.html>System Call Manual</a>,
     * or the <a href=https://docs.microsoft.com/en-us/cpp/c-runtime-library/spawn-wspawn-functions>Spawn functions
     * documentation</a> for more information on this.
     */
    private Map<Language, String> commands = new HashMap<>();
}
