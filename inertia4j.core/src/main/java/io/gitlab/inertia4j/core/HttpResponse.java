package io.gitlab.inertia4j.core;

public interface HttpResponse {
    void setHeader(String name, String value);

    void writeBody(String content);
}
