package io.gitlab.inertia4j.core;

/*
 * Internal representation of an Inertia Page Object.
 * This object is serialized and included in the server responses.
 * 
 * @see <a href="https://inertiajs.com/the-protocol#the-page-object">Inertia Page Object spec</a>
 */
public class PageObject {
    private final String component;
    private final Object props;
    private final String url;
    private final Object version;
    private final boolean encryptHistory;
    private final boolean clearHistory;

    /*
     * PageObject constructor.
     * 
     * @params component component to be rendered by the client
     * @params props data to be served to client
     * @params url value of the URL field in response
     * @params version asset version to be compared with current client asset version
     * @params encryptHistory flag set to encrypt previous browsing activity
     * @params clearHistory flag set to clear previous browsing activity
     */
    public PageObject(
        String component,
        Object props,
        String url,
        Object version,
        boolean encryptHistory,
        boolean clearHistory
    ) {
        this.component = component;
        this.props = props;
        this.url = url;
        this.version = version;
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
    }

    /*
     * Gets the name of the component to be rendered by the client.
     * 
     * @returns component name
     */
    public String getComponent() {
        return component;
    }

    /*
     * Gets the data to be served to client.
     * 
     * @returns props data
     */
    public Object getProps() {
        return props;
    }

    /*
     * Gets the value of the URL field.
     * 
     * @returns URL
     */
    public String getUrl() {
        return url;
    }

    /*
     * Gets the current version of the project assets.
     * 
     * @returns version
     */
    public Object getVersion() {
        return version;
    }

    /*
     * Gets the current value of the encryptHistory flag.
     * 
     * @returns value of the encryptHistory flag
     * @see <a href="https://inertiajs.com/history-encryption">Inertia encryptHistory flag</a>
     */
    public boolean isEncryptHistory() {
        return encryptHistory;
    }

    /*
     * Gets the current value of the clearHistory flag.
     * 
     * @returns value of the clearHistory flag
     * @see <a href="https://inertiajs.com/history-encryption#clearing-history">Inertia clearHistory flag</a>
     */
    public boolean isClearHistory() {
        return clearHistory;
    }

    public static PageObject fromOptions(InertiaRenderingOptions options) {
        return new PageObject(
            options.componentName,
            options.props,
            options.url,
            options.version,
            options.encryptHistory,
            options.clearHistory
        );
    }
}
