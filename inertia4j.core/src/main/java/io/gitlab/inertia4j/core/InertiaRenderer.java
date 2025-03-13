package io.gitlab.inertia4j.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Supplier;

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
     * @param request object to obtain headers and method
     * @param response object to which headers and body will be output
     * @param options rendering options
     */
    public void render(
        HttpRequest request,
        HttpResponse response,
        InertiaRenderingOptions options
    ) throws SerializationException {
        if (isVersionConflict(request)) {
            handleVersionConflictResponse(request, response, options);
            return;
        }
        handleSuccessResponse(request, response, options);
    }

    /*
     * Formats the proper redirect response to the specified location.
     *
     * @param request object to obtain headers and method
     * @param response object to which headers and redirect code will be output
     * @param location URL to redirect to
     */
    public void redirect(
        HttpRequest request,
        HttpResponse response,
        String location
    ) {
        response.setCode(isPutPatchDelete(request) ? 303 : 302);
        response.setHeader("Location", location);
    }

    private boolean isVersionConflict(HttpRequest request) {
        if (!request.getMethod().equalsIgnoreCase("GET")) return false;

        String versionHeader = request.getHeader("X-Inertia-Version");

        return versionHeader != null && !versionHeader.equals(versionProvider.get());
    }

    private void handleVersionConflictResponse(
        HttpRequest request,
        HttpResponse response,
        InertiaRenderingOptions options
    ) {
        response.setCode(409);
        response.setHeader("X-Inertia-Location", options.url);
    }

    private void handleSuccessResponse(
        HttpRequest request,
        HttpResponse response,
        InertiaRenderingOptions options
    ) throws SerializationException {
        response.setHeader("X-Inertia", "true");

        PageObject pageObject = pageObjectFromOptions(request, options);
        String serializedPageObject = serializePageObject(request, pageObject);

        String inertiaHeader = request.getHeader("X-Inertia");
        if (inertiaHeader != null && inertiaHeader.equalsIgnoreCase("true")) {
            response.setHeader("Content-Type", "application/json");
            response.writeBody(serializedPageObject);
        } else {
            response.setHeader("Content-Type", "text/html");
            response.writeBody(templateRenderer.render(serializedPageObject));
        }

        response.setCode(200);
    }

    private PageObject pageObjectFromOptions(HttpRequest request, InertiaRenderingOptions options) {
        String partialComponentHeader = request.getHeader("X-Inertia-Partial-Component");
        if (partialComponentHeader != null) {
            options = options.withPartialComponent(partialComponentHeader);
        }

        return PageObject.fromOptions(options, versionProvider.get());
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
