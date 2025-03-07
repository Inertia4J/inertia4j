package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.jackson.JacksonPageObjectSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

/*
 * Implements a Facade for the InertiaRenderer in order to simplify spring controller logic.
 */
public class Inertia {
    private static final InertiaSpringRenderer renderer = new InertiaSpringRenderer(
        new JacksonPageObjectSerializer(),
        "templates/app.html"
    );

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @params component name of the component to render in the client
     * @param props regular response data
     */
    public static ResponseEntity<String> render(String component, Object props) {
        return renderer.render(component, props);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param url value of the URL field in response
     * @params component name of the component to render in the client
     * @param props regular response data
     */
    public static ResponseEntity<String> render(String url, String component, Object props) {
        return renderer.render(url, component, props);
    }

    /*
     * Calls the render method for the current InertiaSpringRenderer instance.
     *
     * @param WebRequest HTTP request
     * @param url value of the URL field in response
     * @param component name of the component to render in the client
     * @param props regular response data
     */
    public static ResponseEntity<String> render(
        WebRequest request,
        String url,
        String component,
        Object props
    ) {
        return renderer.render(request, url, component, props);
    }

    /*
     * Sets encryptHistory to true for the next request.
     */
    public static void encryptHistory() {
        renderer.setEncryptHistory(true);
    }

    /*
     * Sets encryptHistory to the provided value for the next request.
     * 
     * @params encryptHistory value to set the flag to
     */
    public static void encryptHistory(boolean encryptHistory) {
        renderer.setEncryptHistory(encryptHistory);
    }

    /*
     * Sets clearHistory to true for the next request.
     */
    public static void clearHistory() {
        renderer.setClearHistory(true);
    }
}
