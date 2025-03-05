package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.HttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SpringHttpResponse implements HttpResponse {
    private String body;
    private final HttpHeaders headers = new HttpHeaders();

    @Override
    public void setHeader(String name, String value) {
        headers.add(name, value);
    }

    @Override
    public void writeBody(String content) {
        body = content;
    }

    public ResponseEntity<String> toResponseEntity() {
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
