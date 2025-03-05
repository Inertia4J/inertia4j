package io.gitlab.inertia4j.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PageObject<TData> {
    private String component;
    private TData props;
    private String url;
    private String version;
    private boolean encryptHistory;
    private boolean clearHistory;

    public PageObject(String component, TData props, String url, String version, boolean encryptHistory, boolean clearHistory) {
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

    public TData getProps() {
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

    public String toString() {
        try { // This approach uses jackson, should become swappable
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";  // Return empty object if loading the file fails
        }
    }

    public static class PageObjectBuilder<TData> {
        private String component;
        private TData props;
        private String url;
        private String version;
        private boolean encryptHistory;
        private boolean clearHistory;

        public PageObjectBuilder(String component, TData props) {
            this.component = component;
            this.props = props;
            this.url = "";
            this.version = "";
            this.encryptHistory = false;
            this.clearHistory = false;
        }

        public PageObjectBuilder setComponent(String component) {
            this.component = component;
            return this;
        }

        public PageObjectBuilder setProps(TData props) {
            this.props = props;
            return this;
        }

        public PageObjectBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        // First implementation only supports version as a String
        public PageObjectBuilder setVersion(String version) {
            this.version = version;
            return this;
        }

        public PageObjectBuilder setEncryptHistory(boolean encryptHistory) {
            this.encryptHistory = encryptHistory;
            return this;
        }

        public PageObjectBuilder setClearHistory(boolean clearHistory) {
            this.clearHistory = clearHistory;
            return this;
        }

        public PageObject<TData> build() {
            return new PageObject<TData>(this.component, this.props, this.url, this.version, this.encryptHistory, this.clearHistory);
        }
    }
}
