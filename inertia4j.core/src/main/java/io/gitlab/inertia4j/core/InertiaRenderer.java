package io.gitlab.inertia4j.core;

/*
 * Class responsible for transforming regular responses into Inertia responses.
 */
public class InertiaRenderer {
    private final PageObjectSerializer pageObjectSerializer;
    private final TemplateRenderer templateRenderer;

    private boolean inertiaHistoryEncryptDefault = false; // Usually set by adapter
    private boolean encryptHistory = false;
    private boolean clearHistory = false;

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
        resetHistoryFlags();
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
        resetHistoryFlags();
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

        resetHistoryFlags();

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
     * Sets the default encryptHistory value. Should be called by adapter to provide a default if there is any.
     * This also sets the next request's encryptHistory flag to the new default provided.
     *
     * @params inertiaHistoryEncryptDefault default value for the clearHistory flag
     */
    public void setInertiaHistoryEncryptDefault(boolean inertiaHistoryEncryptDefault) {
        this.inertiaHistoryEncryptDefault = inertiaHistoryEncryptDefault;
        setEncryptHistory(inertiaHistoryEncryptDefault);
    }

    /*
     * Sets the encryptHistory flag value for the next response.
     * 
     * @params encryptHistory flag value
     */
    public void setEncryptHistory(boolean encryptHistory) {
        this.encryptHistory = encryptHistory;
    }

    /*
     * Sets the clearHistory flag value for the next response.
     * 
     * @params clearHistory flag value
     */
    public void setClearHistory(boolean clearHistory) {
        this.clearHistory = clearHistory;
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
            .setEncryptHistory(this.encryptHistory)
            .setClearHistory(this.clearHistory)
            .build();
    }

    /*
     * Method used to revert the encryptHistory flag and clearHistory flags.
     * The encryptHistory flag is set to the default value (false if a default was not provided).
     *
     * @see <a href="https://inertiajs.com/history-encryption">Inertia history encryption and clearing</a>
     * @see <a href="https://inertiajs.com/history-encryption#global-encryption">Inertia global encryption configuration</a>
     */
    private void resetHistoryFlags() {
        setEncryptHistory(this.inertiaHistoryEncryptDefault);
        setClearHistory(false);
    }
}
