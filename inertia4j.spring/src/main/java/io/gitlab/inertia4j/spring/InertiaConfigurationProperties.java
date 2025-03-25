package io.gitlab.inertia4j.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "inertia")
public class InertiaConfigurationProperties {
    private static final String defaultTemplatePath = "templates/app.html";
    private static final boolean defaultEncryptHistory = false;

    final String templatePath;
    final boolean encryptHistory;

    @ConstructorBinding
    public InertiaConfigurationProperties(String templatePath, boolean encryptHistory) {
        this.templatePath = templatePath;
        this.encryptHistory = encryptHistory;
    }

    public InertiaConfigurationProperties(String templatePath) {
        this(templatePath, false);
    }

    public InertiaConfigurationProperties(boolean encryptHistory) {
        this(defaultTemplatePath, encryptHistory);
    }

    public InertiaConfigurationProperties() {
        this(defaultTemplatePath, defaultEncryptHistory);
    }
}
