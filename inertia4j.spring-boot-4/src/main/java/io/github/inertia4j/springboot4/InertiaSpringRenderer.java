package io.github.inertia4j.springboot4;

import io.github.inertia4j.core.HttpResponse;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import io.github.inertia4j.springshared.AbstractInertiaSpringRenderer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

/**
 * Spring Boot 4 implementation of {@link AbstractInertiaSpringRenderer}.
 */
class InertiaSpringRenderer extends AbstractInertiaSpringRenderer {
    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        VersionProvider versionProvider,
        TemplateRenderer templateRenderer
    ) {
        super(serializer, versionProvider, templateRenderer);
    }

    @Override
    protected ResponseEntity<String> convertToResponseEntity(HttpResponse response) {
        HttpHeaders responseHeaders = new HttpHeaders(
            CollectionUtils.toMultiValueMap(response.getHeaders())
        );
        return new ResponseEntity<>(response.getBody(), responseHeaders, response.getCode());
    }
}
