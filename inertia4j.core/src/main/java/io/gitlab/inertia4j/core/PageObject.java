package io.gitlab.inertia4j.core;

public class PageObject {
    private final String component;
    private final Object props;
    private final String url;
    private final String version;
    private final boolean encryptHistory;
    private final boolean clearHistory;

    public PageObject(String component, Object props, String url, String version, boolean encryptHistory, boolean clearHistory) {
        this.component = component;
        this.props = props;
        this.url = url;
        this.version = version;
        this.encryptHistory = encryptHistory;
        this.clearHistory = clearHistory;
    }

    public String getComponent() {
        return component;
    }

    public Object getProps() {
        return props;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public boolean isEncryptHistory() {
        return encryptHistory;
    }

    public boolean isClearHistory() {
        return clearHistory;
    }

    public static class Builder {
        private String component;
        private Object props;
        private String url;
        private String version;
        private boolean encryptHistory;
        private boolean clearHistory;

        public Builder() {
            this.component = "";
            this.props = null;
            this.url = "";
            this.version = "HASH"; // TODO
            this.encryptHistory = false;
            this.clearHistory = false;
        }

        public Builder setComponent(String component) {
            this.component = component;
            return this;
        }

        public Builder setProps(Object props) {
            this.props = props;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        // First implementation only supports version as a String
        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setEncryptHistory(boolean encryptHistory) {
            this.encryptHistory = encryptHistory;
            return this;
        }

        public Builder setClearHistory(boolean clearHistory) {
            this.clearHistory = clearHistory;
            return this;
        }

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
