package ar.edu.itba.cep.executor_service.runner;

/**
 * Base exception to be thrown when something goes wrong with the {@link OperatingSystemProcessCodeRunner}.
 */
public class OSCodeRunnerException extends RuntimeException {

    /**
     * Default constructor.
     */
    public OSCodeRunnerException() {
        super();
    }

    /**
     * Constructor that can set a {@code message} to be retrieved by the {@link #getMessage()} method.
     *
     * @param message The exception message.
     */
    public OSCodeRunnerException(final String message) {
        super(message);
    }

    /**
     * Constructor that can set a {@code cause} to be retrieved by the {@link #getCause()} method.
     *
     * @param cause The {@link Throwable} that caused this exception to be thrown.
     */
    public OSCodeRunnerException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor that can set a {@code message} and a {@code cause}
     * to be retrieved by the {@link #getMessage()} and the {@link #getCause()} methods respectively.
     *
     * @param message The exception message.
     * @param cause   The {@link Throwable} that caused this exception to be thrown.
     */
    public OSCodeRunnerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
