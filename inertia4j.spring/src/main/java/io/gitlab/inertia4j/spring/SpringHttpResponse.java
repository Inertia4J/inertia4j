package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.HttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/*
 * Spring implementation of the HttpResponse interface.
 */
public class SpringHttpResponse implements HttpResponse {
    private String body;
    private HttpStatusCode status;
    private final HttpHeaders headers = new HttpHeaders();

    /*
     * Sets the HTTP Header on the Response to the value provided.
     * 
     * @param name name of the header to set value for
     * @param value value of the header
     */
    @Override
    public SpringHttpResponse setHeader(String name, String value) {
        headers.add(name, value);
        return this;
    }

    @Override
    public SpringHttpResponse setCode(Integer code) {
        this.status = HttpStatus.resolve(code);
        return this;
    }

    /*
     * Writes the body content of the HTTP response.
     * 
     * @param content the content of the HTTP response
     */
    @Override
    public SpringHttpResponse writeBody(String content) {
        body = content;
        return this;
    }

    /*
     * Converts the response to a ResponseEntity<String>.
     * 
     * @returns an OK ResponseEntity<String> instance
     */
    public ResponseEntity<String> toResponseEntity() {
        return new ResponseEntity<>(body, headers, status);
    }
}
