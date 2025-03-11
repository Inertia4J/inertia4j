package io.gitlab.inertia4j.core;

import java.util.Map;

/*
 * Internal representation of an Inertia Page Object.
 * This object is serialized and included in the server responses.
 * 
 * @see <a href="https://inertiajs.com/the-protocol#the-page-object">Inertia Page Object spec</a>
 */
public class PageObject {
    private final String component;
    private final Map<String, Object> props;
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
        Map<String, Object> props,
        String url,
        boolean encryptHistory,
        boolean clearHistory,
        Object version
    ) {
        this.component = component;
        this.props = props;
        this.url = url;
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
        this.version = version;
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
    public Map<String, Object> getProps() {
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

    public static PageObject fromOptions(InertiaRenderingOptions options, Object version) {
        return new PageObject(
            options.componentName,
            options.props,
            options.url,
            options.encryptHistory,
            options.clearHistory,
            version
        );
    }
}
