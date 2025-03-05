package io.gitlab.inertia4j.spring.example;

import io.gitlab.inertia4j.jackson.JacksonPageObjectSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

public class Inertia {
    private static final InertiaSpringRenderer renderer = new InertiaSpringRenderer(
        new JacksonPageObjectSerializer(),
        "templates/app.html"
    );

    public static ResponseEntity<String> render(String component, Object props) {
        return renderer.render(component, props);
    }

    public static ResponseEntity<String> render(String url, String component, Object props) {
        return renderer.render(url, component, props);
    }

    public static ResponseEntity<String> render(
        WebRequest request,
        String url,
        String component,
        Object props
    ) {
        return renderer.render(request, url, component, props);
    }
}
