package io.gitlab.inertia4j.core;

import io.gitlab.inertia4j.spi.PageObject;
import io.gitlab.inertia4j.spi.PageObjectSerializer;
import io.gitlab.inertia4j.spi.SerializationException;
import io.gitlab.inertia4j.spi.TemplateRenderer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/*

 * Class responsible for transforming regular responses into Inertia responses.
 */
public class InertiaRenderer {
    private final PageObjectSerializer pageObjectSerializer;
    private final TemplateRenderer templateRenderer;
    private final Supplier<String> versionProvider;

    /*
     * Constructor for InertiaRenderer.
     *
     * @param serializer PageObjectSerializer implementation used to serialize PageObject
     * @param versionProvider provider for the current Inertia asset version
     * @param templateRenderer renderer for HTML responses
     */
    public InertiaRenderer(
        PageObjectSerializer pageObjectSerializer,
        Supplier<String> versionProvider,
        TemplateRenderer templateRenderer
    ) {
        this.pageObjectSerializer = pageObjectSerializer;
        this.templateRenderer = templateRenderer;
        this.versionProvider = versionProvider;
    }

    /*
     * Constructor for InertiaRenderer, uses SimpleTemplateRenderer as renderer by default.
     * 
     * @param serializer PageObjectSerializer implementation used to serialize PageObject
     * @param versionProvider provider for the current Inertia asset version
     * @param templatePath path to the HTML template to be served
     */
    public InertiaRenderer(
        PageObjectSerializer pageObjectSerializer,
        Supplier<String> versionProvider,
        String templatePath
    ) throws TemplateRenderingException {
        this(pageObjectSerializer, versionProvider, new SimpleTemplateRenderer(templatePath));
    }

    /*
     * Formats the server response to the Inertia response format.
     *
     * @param request HTTP request
     * @param response object to which headers and body will be output
     * @param options rendering options
     */
    public HttpResponse render(
        HttpRequest request,
        InertiaRenderingOptions options
    ) throws SerializationException {
        if (isVersionConflict(request)) {
            return handleVersionConflictResponse(request, options);
        }
        return handleSuccessResponse(request, options);
    }

    /*
     * Formats the proper redirect response to the specified location.
     *
     * @param request HTTP request
     * @param response object to which headers and redirect code will be output
     * @param location URL to redirect to
     */
    public HttpResponse redirect(
        HttpRequest request,
        String location
    ) {
        return new HttpResponse()
            .setCode(isPutPatchDelete(request) ? 303 : 302)
            .setHeader("Location", location);
    }

    /*
     * Redirects to an external or non-Inertia URL.
     *
     * @param response object to which headers and redirect code will be output
     * @param location external URL to redirect to
     */
    public HttpResponse location(String url) {
        return new HttpResponse()
            .setCode(409)
            .setHeader("X-Inertia-Location", url);
    }

    private boolean isVersionConflict(HttpRequest request) {
        if (!request.getMethod().equalsIgnoreCase("GET")) return false;

        String versionHeader = request.getHeader("X-Inertia-Version");

        return versionHeader != null && !versionHeader.equals(versionProvider.get());
    }

    private HttpResponse handleVersionConflictResponse(
        HttpRequest request,
        InertiaRenderingOptions options
    ) {
        return new HttpResponse()
            .setCode(409)
            .setHeader("X-Inertia-Location", options.url);
    }

    private HttpResponse handleSuccessResponse(
        HttpRequest request,
        InertiaRenderingOptions options
    ) throws SerializationException {
        var response = new HttpResponse();

        PageObject pageObject = pageObjectFromOptions(request, options);
        String serializedPageObject = serializePageObject(request, pageObject);

        String inertiaHeader = request.getHeader("X-Inertia");
        if (inertiaHeader != null && inertiaHeader.equalsIgnoreCase("true")) {
            response
                .setHeader("Content-Type", "application/json")
                .setHeader("X-Inertia", "true")
                .setBody(serializedPageObject);
        } else {
            response
                .setHeader("Content-Type", "text/html")
                .setBody(templateRenderer.render(serializedPageObject));
        }

        return response.setCode(200);
    }

    private PageObject pageObjectFromOptions(HttpRequest request, InertiaRenderingOptions options) {
        String partialComponentHeader = request.getHeader("X-Inertia-Partial-Component");
        if (partialComponentHeader != null) {
            options = options.withPartialComponent(partialComponentHeader);
        }
        return new PageObject(
            options.componentName,
            options.props,
            options.url,
            options.encryptHistory,
            options.clearHistory,
            versionProvider.get()
        );
    }

    private String serializePageObject(HttpRequest request, PageObject pageObject) throws SerializationException {
        String partialDataHeader = request.getHeader("X-Inertia-Partial-Data");

        List<String> partialDataProps = null;
        if (partialDataHeader != null) {
            partialDataProps = Arrays.stream(partialDataHeader.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        }

        return pageObjectSerializer.serialize(pageObject, partialDataProps);
    }

    private boolean isPutPatchDelete(HttpRequest request) {
        String requestMethod = request.getMethod();
        return (requestMethod.equalsIgnoreCase("PUT")
            || requestMethod.equalsIgnoreCase("PATCH")
            || requestMethod.equalsIgnoreCase("DELETE"));
    }
}
