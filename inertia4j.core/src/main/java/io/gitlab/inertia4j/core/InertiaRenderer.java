package io.gitlab.inertia4j.core;

public class InertiaRenderer {
    private final PageObjectSerializer pageObjectSerializer;
    private final TemplateRenderer templateRenderer;

    public InertiaRenderer(
        PageObjectSerializer pageObjectSerializer,
        TemplateRenderer templateRenderer
    ) {
        this.pageObjectSerializer = pageObjectSerializer;
        this.templateRenderer = templateRenderer;
    }

    public InertiaRenderer(
        PageObjectSerializer pageObjectSerializer,
        String templatePath
    ) throws TemplateRenderingException {
        this.pageObjectSerializer = pageObjectSerializer;
        this.templateRenderer = new SimpleTemplateRenderer(templatePath);
    }

    public void render(
        HttpRequest request,
        HttpResponse response,
        String url,
        String componentName,
        Object props
    ) throws SerializationException {
        response.setHeader("X-Inertia", "true");

        PageObject pageObject = buildPageObject(url, componentName, props);
        String serializedPageObject = pageObjectSerializer.serialize(pageObject);

        String inertiaHeader = request.getHeader("X-Inertia");
        if (inertiaHeader != null && inertiaHeader.equalsIgnoreCase("true")) {
            response.setHeader("Content-Type", "application/json");
            response.writeBody(serializedPageObject);
        } else {
            response.setHeader("Content-Type", "text/html");
            response.writeBody(templateRenderer.render(serializedPageObject));
        }
    }

    private PageObject buildPageObject(String url, String componentName, Object props) {
        return new PageObject.Builder()
            .setUrl(url)
            .setComponent(componentName)
            .setProps(props)
            .build();
    }
}
