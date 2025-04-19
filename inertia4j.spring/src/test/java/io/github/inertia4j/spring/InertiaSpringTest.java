package io.github.inertia4j.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.inertia4j.core.DefaultPageObjectSerializer;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import io.github.inertia4j.spring.Inertia.Options;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class InertiaSpringTest {

    private static final String testComponent = "TestComponent";
    private static final String testUrl = "/test-url";
    private static final Map<String, Object> testProps = Map.of("prop1", "value1", "prop2", 123);
    private MockHttpServletRequest request;

    private final VersionProvider versionProvider = () -> "1";
    private final PageObjectSerializer pageObjectSerializer = new DefaultPageObjectSerializer();
    private final TemplateRenderer templateRenderer = new FakeTemplateRenderer();
    private InertiaSpring inertiaSpring;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest("GET", testUrl);

        inertiaSpring = new InertiaSpring(
            versionProvider,
            pageObjectSerializer,
            templateRenderer,
            () -> request
        );
    }

    private static class FakeTemplateRenderer implements TemplateRenderer {
        @Override
        public String render(String pageObjectJson) {
            return "<!doctype html>\n" +
                   "<html lang=\"en\">\n" +
                   "  <body>\n" +
                   "    <div id=\"app\" data-page='" + pageObjectJson + "'></div>\n" +
                   "  </body>\n" +
                   "</html>";
        }
    }

    @Test
    void render_whenInitialRequest_returnsHtmlResponse() {
        ResponseEntity<String> response = inertiaSpring.render(testComponent, testProps);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.TEXT_HTML, response.getHeaders().getContentType());
        assertNull(response.getHeaders().get("X-Inertia"));
        assertEquals(getExpectedHtmlBody(false, false), response.getBody());
    }

    @Test
    void render_whenPartialRequest_returnsPartialJsonResponse() {
        request.addHeader("X-Inertia", "true");
        request.addHeader("X-Inertia-Partial-Component", testComponent);
        request.addHeader("X-Inertia-Partial-Data", "prop1"); // Request only prop1

        ResponseEntity<String> response = inertiaSpring.render(testComponent, testProps);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("true", response.getHeaders().getFirst("X-Inertia"));
        assertEquals(getExpectedJsonBody(false, false, Map.of("prop1", "value1")), response.getBody());
    }

    @Test
    void render_withMismatchingVersion_returnsConflictResponse() {
        request.addHeader("X-Inertia", "true");
        request.addHeader("X-Inertia-Version", "stale-version");

        ResponseEntity<String> response = inertiaSpring.render(testComponent, testProps);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(Collections.singletonList("/test-url"), response.getHeaders().get("X-Inertia-Location"));
        assertNull(response.getHeaders().get("X-Inertia"));
        assertNull(response.getBody());
    }

    @Test
    void redirect_returnsSeeOtherResponse() {
        request.setMethod("PUT");
        request.addHeader("X-Inertia", "true");

        ResponseEntity<String> response = inertiaSpring.redirect("/target");

        assertEquals(HttpStatus.SEE_OTHER, response.getStatusCode());
        assertNull(response.getHeaders().get("X-Inertia"));
        assertEquals(Collections.singletonList("/target"), response.getHeaders().get(HttpHeaders.LOCATION));
        assertNull(response.getBody());
    }

    @Test
    void redirect_returnsFoundResponse() {
        request.setMethod("GET");
        ResponseEntity<String> response = inertiaSpring.redirect("/target");

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNull(response.getHeaders().get("X-Inertia"));
        assertEquals("/target", response.getHeaders().getFirst(HttpHeaders.LOCATION));
        assertNull(response.getBody());
    }

    @Test
    void location_returnsConflictResponseWithLocationHeader() {
        request.addHeader("X-Inertia", "true");

        ResponseEntity<String> response = inertiaSpring.location("https://external.example.com");

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getHeaders().get("X-Inertia"));
        assertEquals("https://external.example.com", response.getHeaders().getFirst("X-Inertia-Location"));
        assertNull(response.getBody());
    }

    @Test
    void render_whenInitialRequestWithEncryptHistory_returnsHtmlResponse() {
        ResponseEntity<String> response = inertiaSpring.render(testComponent, testProps, Options.encryptHistory());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.TEXT_HTML, response.getHeaders().getContentType());
        assertNull(response.getHeaders().get("X-Inertia"));
        assertEquals(getExpectedHtmlBody(true, false), response.getBody());
    }

    @Test
    void render_withEncryptHistory_returnsJsonResponse() {
        request.addHeader("X-Inertia", "true");
        request.addHeader("X-Inertia-Version", "1");
        ResponseEntity<String> response = inertiaSpring.render(testComponent, testProps, Options.encryptHistory());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("true", response.getHeaders().getFirst("X-Inertia"));
        assertEquals(getExpectedJsonBody(true, false), response.getBody());
    }

    private static String getExpectedJsonBody(boolean encryptHistory, boolean clearHistory) {
        return getExpectedJsonBody(encryptHistory, clearHistory, testProps);
    }

    private static String getExpectedJsonBody(boolean encryptHistory, boolean clearHistory, Map<String, Object> props) {
        String propsJson;
        try {
            propsJson = objectMapper.writeValueAsString(props);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "{\"component\":\"" + testComponent + "\",\"props\":" + propsJson + ",\"url\":\"" + testUrl + "\",\"version\":\"1\",\"encryptHistory\":" + encryptHistory + ",\"clearHistory\":" + clearHistory + "}";
    }

    private static String getExpectedHtmlBody(boolean encryptHistory, boolean clearHistory) {
        String expectedPageJson = getExpectedJsonBody(encryptHistory, clearHistory);
        return "<!doctype html>\n" +
               "<html lang=\"en\">\n" +
               "  <body>\n" +
               "    <div id=\"app\" data-page='" + expectedPageJson + "'></div>\n" +
               "  </body>\n" +
               "</html>";
    }

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
}
