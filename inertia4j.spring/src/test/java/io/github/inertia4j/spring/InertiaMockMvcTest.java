package io.github.inertia4j.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {
    Inertia4jSpringApplicationTests.FakeApplication.class,
    Inertia4jSpringApplicationTests.FakeController.class
})
@AutoConfigureMockMvc
class Inertia4jSpringApplicationTests {

    @Autowired
    MockMvc mvc;

    @SpringBootApplication
    static class FakeApplication { }

    @RestController
    static class FakeController {
        @Autowired
        Inertia inertia;

        @GetMapping("/")
        ResponseEntity<String> index() {
            return inertia.render("records/Index", Map.of("records", List.of()));
        }
    }

    @Test
    void indexPageHtmlRendering() throws Exception {
        String expectedHtml = """
            <!doctype html>
            <html lang="en">
              <head>
                <meta charset="UTF-8" />
                <link rel="icon" type="image/svg+xml" href="/vite.svg" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <title>App</title>
              </head>
              <body>
                <div id="app" data-page='{&quot;component&quot;:&quot;records/Index&quot;,&quot;props&quot;:{&quot;records&quot;:[]},&quot;url&quot;:&quot;/&quot;,&quot;version&quot;:&quot;1&quot;,&quot;encryptHistory&quot;:false,&quot;clearHistory&quot;:false}'></div>
                <script type="module" src="/src/main.tsx"></script>
              </body>
            </html>
            """;

        mvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(result -> {
                String actual = result.getResponse().getContentAsString()
                    .replaceAll("\\r\\n", "\n")
                    .trim();
                String expected = expectedHtml
                    .replaceAll("\\r\\n", "\n")
                    .trim();
                assertEquals(expected, actual);
            });
    }
}
