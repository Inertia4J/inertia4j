package io.gitlab.inertia4j.core;

/*
 * Exception to be thrown by serializers.
 */
public class SerializationException extends Exception {
    public SerializationException(Throwable cause) {
        super(cause);
    }
}
