package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.DefaultPageObjectSerializer;
import io.gitlab.inertia4j.core.SimpleTemplateRenderer;
import io.gitlab.inertia4j.core.TemplateRenderingException;
import io.gitlab.inertia4j.spi.PageObjectSerializer;
import io.gitlab.inertia4j.spi.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InertiaSpringAutoconfiguration {
    @Autowired
    InertiaConfigurationProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public InertiaSpring inertia(
        VersionProvider versionProvider,
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer
    ) {
        return new InertiaSpring(versionProvider, pageObjectSerializer, templateRenderer);
    }

    @Bean
    @ConditionalOnMissingBean
    public VersionProvider versionProvider() {
        return () -> "1";
    }

    @Bean
    @ConditionalOnMissingBean
    public PageObjectSerializer pageObjectSerializer() {
        return new DefaultPageObjectSerializer();
    }

    @Bean
    @ConditionalOnMissingBean
    public TemplateRenderer templateRenderer() throws TemplateRenderingException {
        return new SimpleTemplateRenderer(properties.templatePath);
    }
}
