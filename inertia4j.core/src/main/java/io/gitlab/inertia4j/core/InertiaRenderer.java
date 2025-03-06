package io.gitlab.inertia4j.core;

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
        this.pageObjectSerializer = pageObjectSerializer;
        this.templateRenderer = new SimpleTemplateRenderer(templatePath);
    }

    /*
     * Formats the server response to the Inertia response format.
     * 
     * @param headerGetter request header getter
     * @param response response to be formatted
     * @param url value of the URL field in response
     * @param componentName name of the component to render in the client
     * @param props regular response data
     */
    public void render(
        RequestHeaderGetter headerGetter,
        HttpResponse response,
        String url,
        String componentName,
        Object props
    ) throws SerializationException {
        response.setHeader("X-Inertia", "true");

        PageObject pageObject = buildPageObject(url, componentName, props);
        String serializedPageObject = pageObjectSerializer.serialize(pageObject);

        String inertiaHeader = headerGetter.get("X-Inertia");
        if (inertiaHeader != null && inertiaHeader.equalsIgnoreCase("true")) {
            response.setHeader("Content-Type", "application/json");
            response.writeBody(serializedPageObject);
        } else {
            response.setHeader("Content-Type", "text/html");
            response.writeBody(templateRenderer.render(serializedPageObject));
        }
    }

    /*
     * Builds a PageObject with the information provided, to be used by the render method.
     * 
     * @param url value of the URL field in response
     * @param componentName name of the component to render in the client
     * @param props regular response data
     * @returns PageObject instance
     */
    private PageObject buildPageObject(String url, String componentName, Object props) {
        return new PageObject.Builder()
            .setUrl(url)
            .setComponent(componentName)
            .setProps(props)
            .build();
    }
}
