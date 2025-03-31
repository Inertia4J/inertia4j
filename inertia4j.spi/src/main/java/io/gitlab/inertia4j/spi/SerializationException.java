package io.gitlab.inertia4j.spi;

/*
 * Exception to be thrown by serializers.
 */
// TODO: make this extend InertiaException
public class SerializationException extends Exception {
    public SerializationException(Throwable cause) {
        super(cause);
    }
}
