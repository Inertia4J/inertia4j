package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.*;
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
