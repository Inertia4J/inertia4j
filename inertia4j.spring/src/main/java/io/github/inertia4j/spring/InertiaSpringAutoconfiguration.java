package io.github.inertia4j.spring;

import io.github.inertia4j.core.DefaultPageObjectSerializer;
import io.github.inertia4j.core.SimpleTemplateRenderer;
import io.github.inertia4j.core.TemplateRenderingException;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * Spring Boot auto-configuration for Inertia4j.
 * Sets up default beans for {@link Inertia}, {@link VersionProvider},
 * {@link PageObjectSerializer}, and {@link TemplateRenderer} if they are not
 * already present in the application context.
 */
public class InertiaSpringAutoconfiguration {
    @Autowired
    InertiaConfigurationProperties properties;

    /**
     * Creates the main {@link Inertia} bean if one doesn't already exist.
     * 
     * @param versionProvider      The configured or default VersionProvider.
     * @param pageObjectSerializer The configured or default PageObjectSerializer.
     * @param templateRenderer     The configured or default TemplateRenderer.
     * @return The Inertia bean instance.
     */
    @Bean
    @ConditionalOnMissingBean
    public Inertia inertia(
        VersionProvider versionProvider,
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer
    ) {
        return new Inertia(versionProvider, pageObjectSerializer, templateRenderer);
    }

    /**
     * Creates a default {@link VersionProvider} bean that returns "1" if one doesn't already exist.
     * 
     * @return A default VersionProvider bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public VersionProvider versionProvider() {
        return () -> "1";
    }

    /**
     * Creates a default {@link PageObjectSerializer} bean using {@link DefaultPageObjectSerializer}
     * (which requires Jackson) if one doesn't already exist.
     * 
     * @return A default PageObjectSerializer bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public PageObjectSerializer pageObjectSerializer() {
        return new DefaultPageObjectSerializer();
    }

    /**
     * Creates a default {@link TemplateRenderer} bean using {@link SimpleTemplateRenderer}
     * and the template path from {@link InertiaConfigurationProperties} if one doesn't already exist.
     * 
     * @return A default TemplateRenderer bean.
     * @throws TemplateRenderingException if the template file cannot be loaded.
     */
    @Bean
    @ConditionalOnMissingBean
    public TemplateRenderer templateRenderer() throws TemplateRenderingException {
        return new SimpleTemplateRenderer(properties.templatePath);
    }
}
