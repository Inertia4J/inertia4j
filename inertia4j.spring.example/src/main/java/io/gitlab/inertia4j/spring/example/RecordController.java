package io.gitlab.inertia4j.spring.example;

import io.gitlab.inertia4j.spring.Inertia;
import io.gitlab.inertia4j.spring.Inertia.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
public class RecordController {

    @Autowired
    private RecordRepository recordRepository;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        Set<Record> records = recordRepository.getAllRecords();

        return Inertia.render("records/Index", Map.of("records", records));
    }

    @GetMapping("/records/first")
    public ResponseEntity<String> first() {
        Record record = recordRepository.getRecordById(1);

        return Inertia.render("records/Show", Map.of("record", record));
    }

    @PostMapping("/records")
    public ResponseEntity<String> create(@RequestBody Record record) {
        // TODO: replace with Inertia.redirect
        return Inertia.render("records/Show", Map.of("record", record));
    }

    @GetMapping("/encryptHistory")
    public ResponseEntity<String> indexEncryptHistory() {
        Set<Record> records = recordRepository.getAllRecords();

        return Inertia.render("records/Index", Map.of("records", records), Options.encryptHistory());
    }

    @GetMapping("/clearHistory")
    public ResponseEntity<String> indexClearHistory() {
        Set<Record> records = recordRepository.getAllRecords();

        return Inertia.render("records/Index", Map.of("records", records), Options.clearHistory());
    }

    @GetMapping("/allOptions")
    public ResponseEntity<String> allOptions() {
        Set<Record> records = recordRepository.getAllRecords();

        return Inertia.render("records/Index", Map.of("records", records), Options.clearHistory().encryptHistory());
    }

}
