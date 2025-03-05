package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.core.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * Bridges Spring-specific rendering with the core InertiaRenderer
 */
class InertiaSpringRenderer {
    private final InertiaRenderer renderer;

    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        String templatePath
    ) throws SpringInertiaException {
        try {
            this.renderer = new InertiaRenderer(serializer, templatePath);
        } catch (TemplateRenderingException e) {
            throw new SpringInertiaException(e);
        }
    }

    public InertiaSpringRenderer(
        PageObjectSerializer serializer,
        TemplateRenderer templateRenderer
    ) throws SpringInertiaException {
        this.renderer = new InertiaRenderer(serializer, templateRenderer);
    }

    public <TData> ResponseEntity<String> render(String component, TData props) {
        HttpServletRequest request = getCurrentRequest();
        String url = request.getRequestURI();

        return render(getCurrentRequest()::getHeader, url, component, props);
    }

    public <TData> ResponseEntity<String> render(
        String url,
        String component,
        TData props
    ) {
        return render(getCurrentRequest()::getHeader, url, component, props);
    }

    public <TData> ResponseEntity<String> render(
        WebRequest request,
        String url,
        String component,
        TData props
    ) {
        return render(request::getHeader, url, component, props);
    }

    private <TData> ResponseEntity<String> render(
        HttpRequest request,
        String url,
        String component,
        TData props
    ) {
        SpringHttpResponse inertiaSpringResponse = new SpringHttpResponse();

        try {
            renderer.render(
                    request,
                    inertiaSpringResponse,
                    url,
                    component,
                    props
            );
        } catch (SerializationException e) {
            throw new SpringInertiaException(e);
        }

        return inertiaSpringResponse.toResponseEntity();
    }

    private HttpServletRequest getCurrentRequest() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(
            requestAttributes != null,
            "Could not find current request via RequestContextHolder"
        );
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
}
