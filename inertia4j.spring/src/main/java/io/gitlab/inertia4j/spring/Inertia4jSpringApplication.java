package io.gitlab.inertia4j.spring;

import io.gitlab.inertia4j.jackson.JacksonPageObjectSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Set;

@RestController
@SpringBootApplication
public class Inertia4jSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(Inertia4jSpringApplication.class, args);
	}

	@GetMapping("/")
	public ResponseEntity<String> index(WebRequest request) {
		SpringInertiaRenderer renderer = new SpringInertiaRenderer(
			new JacksonPageObjectSerializer(),
			"templates/app.html"
		);

		RecordRepository recordRepository = new RecordRepository();
		Set<Record> records = recordRepository.getAllRecords();

		return renderer.render("/", "App/Index", records);
	}

}
