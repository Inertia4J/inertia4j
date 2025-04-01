package io.gitlab.inertia4j.spi;

public class InertiaException extends RuntimeException {
    public InertiaException(String message) {
        super(message);
    }

    public InertiaException(String message, Throwable cause) {
        super(message, cause);
    }

    public InertiaException(Throwable cause) {
        super(cause);
    }
}
