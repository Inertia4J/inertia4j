import io.github.inertia4j.core.*;
import io.github.inertia4j.spi.PageObjectSerializer;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class InertiaRendererTest {
    private final PageObjectSerializer pageObjectSerializer = new DefaultPageObjectSerializer();
    private Supplier<String> versionProvider = () -> "1";

    @Test
    void render_whenVersionConflicts_returns409AndLocation() {
        versionProvider = () -> "old";

        var httpRequest = new FakeHttpRequest("GET", Map.of("X-Inertia-Version", "new"));
        var options = new InertiaRenderingOptions(false, false, "/page", "Component", null);

        HttpResponse response = render(httpRequest, options);

        assertEquals(409, response.getCode());
        assertEquals(Collections.singletonList("/page"), response.getHeaders().get("X-Inertia-Location"));
    }

    @Test
    void render_whenVersionConflicts_whenNonGet_returns200() {
        versionProvider = () -> "old";

        var httpRequest = new FakeHttpRequest("POST", Map.of("X-Inertia-Version", "new")); // Non-GET
        var options = new InertiaRenderingOptions(false, false, "/page", "Component", null);

        HttpResponse response = render(httpRequest, options);

        assertEquals(200, response.getCode());
        assertEquals(Collections.singletonList("text/html"), response.getHeaders().get("Content-Type"));
        assertFalse(response.getHeaders().containsKey("X-Inertia"));

        var expectedBody = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "  <body>\n" +
                "    <div id=\"app\" data-page=\"{&quot;component&quot;:&quot;Component&quot;,&quot;props&quot;:null,&quot;url&quot;:&quot;/page&quot;,&quot;version&quot;:&quot;old&quot;,&quot;encryptHistory&quot;:false,&quot;clearHistory&quot;:false}\"></div>\n" +
                "  </body>\n" +
                "</html>".trim();
        assertEquals(normalizeHtml(expectedBody), normalizeHtml(response.getBody()));
    }

    @Test
    void render_whenNoVersionHeader_returns200WithHtml() {
        var httpRequest = new FakeHttpRequest("GET", Map.of());
        var options = new InertiaRenderingOptions(
            false,
            false,
            "/page",
            "Component",
            Map.of("name", "\"An album\"", "genre", "Drum n' Bass")
        );

        HttpResponse response = render(httpRequest, options);

        assertEquals(200, response.getCode());
        assertEquals(Collections.singletonList("text/html"), response.getHeaders().get("Content-Type"));
        assertFalse(response.getHeaders().containsKey("X-Inertia"));

        var expectedBody = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "  <body>\n" +
                "    <div id=\"app\" data-page=\"{&quot;component&quot;:&quot;Component&quot;,&quot;props&quot;:{&quot;genre&quot;:&quot;Drum n&apos; Bass&quot;,&quot;name&quot;:&quot;\\&quot;An album\\&quot;&quot;},&quot;url&quot;:&quot;/page&quot;,&quot;version&quot;:&quot;1&quot;,&quot;encryptHistory&quot;:false,&quot;clearHistory&quot;:false}\"></div>\n" +
                "  </body>\n" +
                "</html>".trim();

        assertEquals(normalizeHtml(expectedBody), normalizeHtml(response.getBody()));
    }

    @Test
    void render_whenSameVersion_whenInitialRequest_returns200WithHtml() {
        var httpRequest = new FakeHttpRequest("GET", Map.of("X-Inertia-Version", versionProvider.get()));
        var options = new InertiaRenderingOptions(false, false, "/page", "Component", null);

        HttpResponse response = render(httpRequest, options);

        assertEquals(200, response.getCode());
        assertEquals(Collections.singletonList("text/html"), response.getHeaders().get("Content-Type"));
        assertFalse(response.getHeaders().containsKey("X-Inertia"));

        var expectedBody = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "  <body>\n" +
                "    <div id=\"app\" data-page=\"{&quot;component&quot;:&quot;Component&quot;,&quot;props&quot;:null,&quot;url&quot;:&quot;/page&quot;,&quot;version&quot;:&quot;1&quot;,&quot;encryptHistory&quot;:false,&quot;clearHistory&quot;:false}\"></div>\n" +
                "  </body>\n" +
                "</html>".trim();

        assertEquals(normalizeHtml(expectedBody), normalizeHtml(response.getBody()));
    }

    @Test
    void render_whenSameVersion_whenInertiaRequest_returns200WithJson() {
        var httpRequest = new FakeHttpRequest("GET", Map.of(
            "X-Inertia-Version", versionProvider.get(),
            "X-Inertia", "true"
        ));
        Map<String, Object> props = Map.of("user", "test", "status", 1);
        var options = new InertiaRenderingOptions(false, false, "/page", "Component", props);

        HttpResponse response = render(httpRequest, options);

        assertEquals(200, response.getCode());
        assertEquals(Collections.singletonList("application/json"), response.getHeaders().get("Content-Type"));
        assertEquals(Collections.singletonList("true"), response.getHeaders().get("X-Inertia"));

        var expectedJson = "{\"component\":\"Component\",\"props\":{\"status\":1,\"user\":\"test\"},\"url\":\"/page\",\"version\":\"1\",\"encryptHistory\":false,\"clearHistory\":false}".trim();
        assertEquals(expectedJson, response.getBody());
    }

    @Test
    void render_whenSameVersion_whenPartialInertiaRequest_returns200WithJson() {
        var httpRequest = new FakeHttpRequest("GET", Map.of(
            "X-Inertia-Version", versionProvider.get(),
            "X-Inertia", "true",
            "X-Inertia-Partial-Component", "Component",
            "X-Inertia-Partial-Data", "user, status"
        ));
        Map<String, Object> props = Map.of("user", "test", "status", 1, "ignored", "abc");
        var options = new InertiaRenderingOptions(false, false, "/page", "Component", props);

        HttpResponse response = render(httpRequest, options);

        assertEquals(200, response.getCode());
        assertEquals(Collections.singletonList("application/json"), response.getHeaders().get("Content-Type"));
        assertEquals(Collections.singletonList("true"), response.getHeaders().get("X-Inertia"));

        var expectedJson = "{\"component\":\"Component\",\"props\":{\"status\":1,\"user\":\"test\"},\"url\":\"/page\",\"version\":\"1\",\"encryptHistory\":false,\"clearHistory\":false}".trim();
        assertEquals(expectedJson, response.getBody());
    }

    private HttpResponse render(HttpRequest request, InertiaRenderingOptions options) {
        return new InertiaRenderer(
            pageObjectSerializer,
            versionProvider,
            "template.html"
        ).render(request, options);
    }

    private static String normalizeHtml(String html) {
        return html
            .replaceAll("\\s+", "")
            .replaceAll(">\\s+<", "><")
            .trim();
    }
}
