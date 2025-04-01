package io.gitlab.inertia4j.spi;

/*
 * Exception to be thrown by serializers.
 */
public class SerializationException extends InertiaException {
    public SerializationException(Throwable cause) {
        super(cause);
    }
}
