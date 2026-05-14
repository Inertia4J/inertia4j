package io.github.inertia4j.spring;

import io.github.inertia4j.core.DefaultPageObjectSerializer;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import io.github.inertia4j.springshared.AbstractInertiaSpringAutoconfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot 3 auto-configuration for Inertia4j.
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
        return new DefaultPageObjectSerializer();
    }
}
