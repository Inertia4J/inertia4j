package io.github.inertia4j.spring;

import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import io.github.inertia4j.springshared.AbstractInertia;
import jakarta.servlet.http.HttpServletRequest;

import java.util.function.Supplier;

/**
 * Spring Boot 3 implementation of {@link AbstractInertia}.
 */
public class Inertia extends AbstractInertia {
    Inertia(
        VersionProvider versionProvider,
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer,
        Supplier<HttpServletRequest> requestSupplier
    ) {
        super(new InertiaSpringRenderer(pageObjectSerializer, versionProvider, templateRenderer), requestSupplier);
    }

    public Inertia(
        VersionProvider versionProvider,
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer
    ) {
        super(new InertiaSpringRenderer(pageObjectSerializer, versionProvider, templateRenderer));
    }

    public static class Options extends io.github.inertia4j.springshared.AbstractInertia.Options {}
}
