package ar.edu.itba.cep.executor_service.runner;

/**
 * Exception to be thrown when something goes wrong when actually performing the execution.
 */
public class ExecutionFailedException extends OSCodeRunnerException {

    /**
     * Default constructor.
     */
    public ExecutionFailedException() {
        super();
    }

    /**
     * Constructor that can set a {@code message} to be retrieved by the {@link #getMessage()} method.
     *
     * @param message The exception message.
     */
    public ExecutionFailedException(final String message) {
        super(message);
    }

    /**
     * Constructor that can set a {@code cause} to be retrieved by the {@link #getCause()} method.
     *
     * @param cause The {@link Throwable} that caused this exception to be thrown.
     */
    public ExecutionFailedException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor that can set a {@code message} and a {@code cause}
     * to be retrieved by the {@link #getMessage()} and the {@link #getCause()} methods respectively.
     *
     * @param message The exception message.
     * @param cause   The {@link Throwable} that caused this exception to be thrown.
     */
    public ExecutionFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
