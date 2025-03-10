package io.gitlab.inertia4j.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Class responsible for transforming regular responses into Inertia responses.
 */
public class InertiaRenderer {
    private final PageObjectSerializer pageObjectSerializer;
    private final TemplateRenderer templateRenderer;

    /*
     * Constructor for InertiaRenderer.
     *
     * @param serializer PageObjectSerializer implementation used to serialize PageObject
     * @param templateRenderer renderer for HTML responses
     */
    public InertiaRenderer(
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer
    ) {
        this.pageObjectSerializer = pageObjectSerializer;
        this.templateRenderer = templateRenderer;
    }

    /*
     * Constructor for InertiaRenderer, uses SimpleTemplateRenderer as renderer by default.
     *
     * @param serializer PageObjectSerializer implementation used to serialize PageObject
     * @param templatePath path to the HTML template to be served
     */
    public InertiaRenderer(
        PageObjectSerializer pageObjectSerializer,
        String templatePath
    ) throws TemplateRenderingException {
        this(pageObjectSerializer, new SimpleTemplateRenderer(templatePath));
    }

    /*
     * Formats the server response to the Inertia response format.
     *
     * @param headerGetter request header getter
     * @param response object to which headers and body will be output
     * @param url value of the URL field in response
     * @param componentName name of the component to render in the client
     * @param props regular response data
     * @param options rendering options
     */
    public void render(
        RequestHeaderGetter headerGetter,
        HttpResponse response,
        InertiaRenderingOptions options
    ) throws SerializationException {
        response.setHeader("X-Inertia", "true");

        PageObject pageObject = pageObjectFromOptions(headerGetter, options);
        String serializedPageObject = serializePageObject(headerGetter, pageObject);

        String inertiaHeader = headerGetter.get("X-Inertia");
        if (inertiaHeader != null && inertiaHeader.equalsIgnoreCase("true")) {
            response.setHeader("Content-Type", "application/json");
            response.writeBody(serializedPageObject);
        } else {
            response.setHeader("Content-Type", "text/html");
            response.writeBody(templateRenderer.render(serializedPageObject));
        }
    }

    private PageObject pageObjectFromOptions(RequestHeaderGetter headerGetter, InertiaRenderingOptions options) {
        String partialComponentHeader = headerGetter.get("X-Inertia-Partial-Component");
        if (partialComponentHeader != null) {
            options = options.withPartialComponent(partialComponentHeader);
        }

        return PageObject.fromOptions(options);
    }

    private String serializePageObject(RequestHeaderGetter headerGetter, PageObject pageObject) throws SerializationException {
        String partialDataHeader = headerGetter.get("X-Inertia-Partial-Data");

        List<String> partialDataProps = null;
        if (partialDataHeader != null) {
            partialDataProps = Arrays.stream(partialDataHeader.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        return pageObjectSerializer.serialize(pageObject, partialDataProps);
    }
}
