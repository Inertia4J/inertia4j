package io.github.inertia4j.springshared;

import io.github.inertia4j.core.SimpleTemplateRenderer;
import io.github.inertia4j.core.TemplateRenderingException;
import io.github.inertia4j.spi.PageObjectSerializer;
import io.github.inertia4j.spi.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot auto-configuration for Inertia4j.
 * Sets up default beans for {@link AbstractInertia}, {@link VersionProvider},
 * {@link PageObjectSerializer}, and {@link TemplateRenderer} if they are not
 * already present in the application context.
 */
public abstract class AbstractInertiaSpringAutoconfiguration {
    @Autowired
    protected InertiaConfigurationProperties properties;

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
     * Creates a default {@link PageObjectSerializer} bean if one doesn't already exist.
     *
     * @return A default PageObjectSerializer bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public abstract PageObjectSerializer pageObjectSerializer();

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
