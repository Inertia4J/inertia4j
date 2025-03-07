package io.gitlab.inertia4j.spring.example;

import io.gitlab.inertia4j.spring.Inertia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class RecordController {

    @Autowired
    private RecordRepository recordRepository;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        Set<Record> records = recordRepository.getAllRecords();

        return Inertia.render("App/Index", records);
    }

    // View to test flags, can be deleted later.
    @GetMapping("/encryptHistory")
    public ResponseEntity<String> indexEncryptHistory() {
        Set<Record> records = recordRepository.getAllRecords();

        Inertia.encryptHistory();
        return Inertia.render("App/Index", records);
    }

    // View to test flags, can be deleted later.
    @GetMapping("/clearHistory")
    public ResponseEntity<String> indexClearHistory() {
        Set<Record> records = recordRepository.getAllRecords();

        Inertia.clearHistory();
        return Inertia.render("App/Index", records);
    }

}
