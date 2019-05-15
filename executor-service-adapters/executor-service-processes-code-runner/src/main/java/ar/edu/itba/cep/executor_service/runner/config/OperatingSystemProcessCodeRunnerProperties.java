package ar.edu.itba.cep.executor_service.runner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * Configuration properties for the {@link ar.edu.itba.cep.executor_service.runner.OperatingSystemProcessCodeRunner}.
 */
@ConfigurationProperties(prefix = OperatingSystemProcessCodeRunnerProperties.PREFIX)
public class OperatingSystemProcessCodeRunnerProperties {

    /**
     * Prefix for these properties.
     */
    public static final String PREFIX = "code-runner";

    /**
     * Pre-run properties.
     */
    @NestedConfigurationProperty
    private PreRunRunningProperties preRun;
    /**
     * Runner properties.
     */
    @NestedConfigurationProperty
    private RunnerRunningProperties runner;
    /**
     * The base working directory.
     */
    private String baseWorkingDirectory = "/tmp";


    /**
     * Getter for the pre-run properties.
     *
     * @return Pre-run properties.
     */
    public PreRunRunningProperties getPreRun() {
        return preRun;
    }

    /**
     * Getter for runner properties.
     *
     * @return Runner properties.
     */
    public RunnerRunningProperties getRunner() {
        return runner;
    }

    /**
     * Getter for the base working directory.
     *
     * @return The base working directory.
     */
    public String getBaseWorkingDirectory() {
        return baseWorkingDirectory;
    }


    /**
     * Setter for pre-run properties.
     *
     * @param preRun The pre-run properties.
     */
    public void setPreRun(final PreRunRunningProperties preRun) {
        this.preRun = preRun;
    }

    /**
     * Setter for runner properties.
     *
     * @param runner The runner properties.
     */
    public void setRunner(final RunnerRunningProperties runner) {
        this.runner = runner;
    }

    /**
     * Setter for the base working directory.
     *
     * @param baseWorkingDirectory The base working directory.
     */
    public void setBaseWorkingDirectory(final String baseWorkingDirectory) {
        this.baseWorkingDirectory = baseWorkingDirectory;
    }


    /**
     * Nested properties for the pre-run.
     */
    public static class PreRunRunningProperties extends RunningProperties {

        /**
         * The timeout.
         */
        private long timeout = 0;
        /**
         * Indicates whether execution must continue when the pre-run process exit code is non zero.
         */
        private boolean continueOnNonZeroExitCode = false;


        /**
         * Getter for the timeout.
         *
         * @return The timeout.
         */
        public long getTimeout() {
            return timeout;
        }

        /**
         * Getter for the non-zero exit code continuation flag.
         *
         * @return A flag that indicates whether execution must continue when the pre-run process is non zero.
         */
        public boolean isContinueOnNonZeroExitCode() {
            return continueOnNonZeroExitCode;
        }


        /**
         * Setter for the timeout.
         *
         * @param timeout The timeout.
         */
        public void setTimeout(final long timeout) {
            this.timeout = timeout;
        }

        /**
         * Setter for the non-zero exit code continuation flag.
         *
         * @param continueOnNonZeroExitCode A flag that indicates whether execution must continue when the pre-run process is non zero.
         */
        public void setContinueOnNonZeroExitCode(final boolean continueOnNonZeroExitCode) {
            this.continueOnNonZeroExitCode = continueOnNonZeroExitCode;
        }
    }

    /**
     * Nested properties for the runner.
     */
    public static class RunnerRunningProperties extends RunningProperties {

        /**
         * The default timeout.
         */
        private long defaultTimeout = 0;

        /**
         * Getter for the default timeout.
         *
         * @return The default timeout.
         */
        public long getDefaultTimeout() {
            return defaultTimeout;
        }

        /**
         * Setter for the default timeout.
         *
         * @param defaultTimeout The default timeout.
         */
        public void setDefaultTimeout(final long defaultTimeout) {
            this.defaultTimeout = defaultTimeout;
        }


    }

    /**
     * Base class for runners.
     */
    private static abstract class RunningProperties {

        /**
         * The command to be run.
         */
        private String command = "";
        /**
         * Arguments for the command.
         */
        private List<String> args = new LinkedList<>();


        /**
         * Getter for the command to be run.
         *
         * @return The command to be run.
         */
        public String getCommand() {
            return command;
        }

        /**
         * Getter for the arguments for the command.
         *
         * @return The arguments for the command.
         */
        public List<String> getArgs() {
            return args;
        }

        /**
         * Setter for the command to be run.
         *
         * @@param command  The command to be run.
         */
        public void setCommand(final String command) {
            this.command = command;
        }

        /**
         * Setter for the arguments for the command.
         *
         * @param args The arguments for the command.
         */
        public void setArgs(final List<String> args) {
            this.args = args;
        }
    }
}
