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
    private final String version;
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
    public PageObject(String component, Object props, String url, String version, boolean encryptHistory, boolean clearHistory) {
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
    public String getVersion() {
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

    /*
     * Builder for PageObject.
     */
    public static class Builder {
        private String component;
        private Object props;
        private String url;
        private String version;
        private boolean encryptHistory;
        private boolean clearHistory;

        /*
         * Instances the builder with the default attributes.
         */
        public Builder() {
            this.component = "";
            this.props = null;
            this.url = "";
            this.version = "HASH"; // TODO
            this.encryptHistory = false;
            this.clearHistory = false;
        }

        /*
        * Sets the name of the component for the PageObject.
        * 
        * @params component name of the component
        * @returns builder
        */
        public Builder setComponent(String component) {
            this.component = component;
            return this;
        }

        /*
        * Sets the data props for the PageObject.
        * 
        * @params props data props
        * @returns builder
        */
        public Builder setProps(Object props) {
            this.props = props;
            return this;
        }

        /*
        * Sets the URL for the PageObject.
        * 
        * @params url URL
        * @returns builder
        */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /*
        * Sets the asset version for the PageObject.
        * 
        * @params version version
        * @returns builder
        */
        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        /*
        * Sets value of the encryptHistory flag for the PageObject.
        * 
        * @params encryptHistory value of the encryptHistory flag
        * @returns builder
        * @see <a href="https://inertiajs.com/history-encryption">Inertia encryptHistory flag</a>
        */
        public Builder setEncryptHistory(boolean encryptHistory) {
            this.encryptHistory = encryptHistory;
            return this;
        }

        /*
        * Sets value of the clearHistory flag for the PageObject.
        * 
        * @params clearHistory value of the clearHistory flag
        * @returns builder
        * @see <a href="https://inertiajs.com/history-encryption#clearing-history">Inertia clearHistory flag</a>
        */
        public Builder setClearHistory(boolean clearHistory) {
            this.clearHistory = clearHistory;
            return this;
        }

        /*
        * Builds a PageObject based on provided builder data.
        * 
        * @returns a PageObject instance
        */
        public PageObject build() {
            return new PageObject(
                this.component,
                this.props,
                this.url,
                this.version,
                this.encryptHistory,
                this.clearHistory
            );
        }
    }
}
