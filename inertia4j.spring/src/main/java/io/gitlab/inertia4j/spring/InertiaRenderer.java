package io.gitlab.inertia4j.spring;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.http.HttpHeaders;

class InertiaRenderer {
    private String templatePath;

    public InertiaRenderer() {
        this.templatePath = "templates/app.html";
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplatePath() {
        return this.templatePath;
    }

    public <TData> ResponseEntity<String> render(String component, TData props) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Inertia", "true");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpServletRequest request = getCurrentRequest();

        String pageObjectString = new PageObject.PageObjectBuilder(component, props)
                .setUrl(request.getRequestURI())
                .setVersion("HASH") // TODO: Implement digesting
                .build()
                .toString();

        // Check header
        if ("true".equalsIgnoreCase(request.getHeader("X-Inertia"))) {
            return new ResponseEntity<>(pageObjectString, headers, HttpStatus.OK);
        }

        return inertiaPageWithHtml(pageObjectString);
    }

    private HttpServletRequest getCurrentRequest() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
        Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    private ResponseEntity<String> inertiaPageWithHtml(String pageObjectData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        String htmlString = this.htmlTemplate(pageObjectData);

        return new ResponseEntity<>(htmlString, headers, HttpStatus.OK);
    }

    private String htmlTemplate(String pageObjectData) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream(this.templatePath);

        if (inputStream == null) {
            return ""; // Should throw exception
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        return reader.lines()
                .collect(Collectors.joining(System.lineSeparator()))
                .replaceAll("\\$\\{pageObject}", pageObjectData);
    }
}
