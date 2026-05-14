package io.github.inertia4j.springboot4;

import io.github.inertia4j.core.DefaultPageObjectSerializer;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import io.github.inertia4j.springshared.AbstractInertia;
import io.github.inertia4j.springshared.AbstractInertiaSpringAutoconfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot 4 implementation of {@link AbstractInertiaSpringAutoconfiguration}.
 */
@Configuration
public class InertiaSpringAutoconfiguration extends AbstractInertiaSpringAutoconfiguration {
    @Override
    @Bean
    @ConditionalOnMissingBean
    public VersionProvider versionProvider() {
        return super.versionProvider()::get;
    }

    @Bean
    @ConditionalOnMissingBean
    public Inertia inertia(
        VersionProvider versionProvider,
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer
    ) {
        return new Inertia(versionProvider, pageObjectSerializer, templateRenderer);
    }

    @Override
    @Bean
    @ConditionalOnMissingBean
    public PageObjectSerializer pageObjectSerializer() {
        try {
            Class.forName("tools.jackson.databind.ObjectMapper");
            return new Jackson3PageObjectSerializer();
        } catch (ClassNotFoundException e) {
            return new DefaultPageObjectSerializer();
        }
    }
}
