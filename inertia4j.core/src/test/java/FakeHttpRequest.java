import io.gitlab.inertia4j.core.HttpRequest;

import java.util.Map;

public class FakeHttpRequest implements HttpRequest {
    private final Map<String, String> headers;
    private final String method;

    public FakeHttpRequest(String method, Map<String, String> headers) {
        this.method = method;
        this.headers = headers;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String getMethod() {
        return method;
    }
}
