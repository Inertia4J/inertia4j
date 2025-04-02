package io.gitlab.inertia4j.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Inertia4j integration with Spring Boot.
 * Allows setting the template path and default history encryption behavior via application properties.
 * Properties are prefixed with `inertia`.
 * <p>
 * Example `application.properties`:
 * <pre>
 * inertia.template-path=templates/my-app.html
 * inertia.encrypt-history=true
 * </pre>
 */
@Configuration
@ConfigurationProperties(prefix = "inertia")
public class InertiaConfigurationProperties {
    private static final String defaultTemplatePath = "templates/app.html";
    private static final boolean defaultEncryptHistory = false;

    /**
     * The classpath path to the main HTML template file used by the default {@link io.gitlab.inertia4j.core.SimpleTemplateRenderer}.
     * Corresponds to the `inertia.template-path` property.
     */
    final String templatePath;
    /**
     * Default value for the encryptHistory flag, determining whether browser history state should be encrypted.
     * Corresponds to the `inertia.encrypt-history` property.
     * @see <a href="https://inertiajs.com/history-encryption">Inertia History Encryption</a>
     */
    final boolean encryptHistory;

    /**
     * Constructor used by Spring Boot for property binding.
     * @param templatePath Value of `inertia.template-path`.
     * @param encryptHistory Value of `inertia.encrypt-history`.
     */
    @ConstructorBinding
    public InertiaConfigurationProperties(String templatePath, boolean encryptHistory) {
        this.templatePath = templatePath;
        this.encryptHistory = encryptHistory;
    }

    /**
     * Constructor using default `encryptHistory`.
     * @param templatePath The template path.
     */
    public InertiaConfigurationProperties(String templatePath) {
        this(templatePath, defaultEncryptHistory);
    }

    /**
     * Constructor using default `templatePath`.
     * @param encryptHistory The encryptHistory flag value.
     */
    public InertiaConfigurationProperties(boolean encryptHistory) {
        this(defaultTemplatePath, encryptHistory);
    }

    /**
     * Constructor using default values for both `templatePath` and `encryptHistory`.
     */
    public InertiaConfigurationProperties() {
        this(defaultTemplatePath, defaultEncryptHistory);
    }
}
