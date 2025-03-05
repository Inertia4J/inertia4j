package io.gitlab.inertia4j.spring;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
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

    public <TData> ResponseEntity<String> render(WebRequest request, String component, TData props) {
        return inertiaPageResponse(request, component, props);
    }

    private <TData> ResponseEntity<String> inertiaPageResponse(WebRequest request, String component, TData props) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Inertia", "true");
        headers.setContentType(MediaType.APPLICATION_JSON);

        String pageObjectString = new PageObject.PageObjectBuilder(component, props)
                .setUrl(request.getDescription(false))
                .setVersion("HASH") // TODO: Implement digesting
                .build()
                .toString();

        // Check header
        if ("true".equalsIgnoreCase(request.getHeader("X-Inertia"))) {
            return new ResponseEntity<>(pageObjectString, headers, HttpStatus.OK);
        }

        return inertiaPageWithHtml(pageObjectString);
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
