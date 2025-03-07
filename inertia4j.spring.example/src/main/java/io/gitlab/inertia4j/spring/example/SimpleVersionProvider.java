package io.gitlab.inertia4j.spring.example;

import io.gitlab.inertia4j.spring.VersionProvider;
import org.springframework.stereotype.Component;

@Component
public class SimpleVersionProvider implements VersionProvider {
    @Override
    public String get() {
        return "latest";
    }
}
