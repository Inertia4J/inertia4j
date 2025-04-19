package io.github.inertia4j.spi;

/**
 * Base exception for Inertia4j related errors.
 */
public class InertiaException extends RuntimeException {
    /**
     * Constructs a new Inertia exception with the specified detail message.
     * @param message the detail message.
     */
    public InertiaException(String message) {
        super(message);
    }

    /**
     * Constructs a new Inertia exception with the specified detail message and cause.
     * @param message the detail message.
     * @param cause the cause.
     */
    public InertiaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new Inertia exception with the specified cause.
     * @param cause the cause.
     */
    public InertiaException(Throwable cause) {
        super(cause);
    }
}
