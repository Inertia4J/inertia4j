package io.gitlab.inertia4j.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@SpringBootApplication
public class Inertia4jSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(Inertia4jSpringApplication.class, args);
	}

	@GetMapping("/")
	public ResponseEntity<String> index() {
		InertiaRenderer renderer = new InertiaRenderer();

		RecordRepository recordRepository = new RecordRepository();
		Set<Record> records = recordRepository.getAllRecords();

		return renderer.render("App/Index", records);
	}

}
