package io.gitlab.inertia4j.spring.example;

import io.gitlab.inertia4j.spring.Inertia;
import io.gitlab.inertia4j.spring.Inertia.Options;
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

    @GetMapping("/record/first")
    public ResponseEntity<String> first() {
        Record record = recordRepository.getRecordById(1);

        return Inertia.render("App/Show", record);
    }

    @GetMapping("/encryptHistory")
    public ResponseEntity<String> indexEncryptHistory() {
        Set<Record> records = recordRepository.getAllRecords();

        return Inertia.render("App/Index", records, Options.encryptHistory());
    }

    @GetMapping("/clearHistory")
    public ResponseEntity<String> indexClearHistory() {
        Set<Record> records = recordRepository.getAllRecords();

        return Inertia.render("App/Index", records, Options.clearHistory());
    }

    @GetMapping("/allOptions")
    public ResponseEntity<String> allOptions() {
        Set<Record> records = recordRepository.getAllRecords();

        return Inertia.render("App/Index", records, Options.clearHistory().encryptHistory());
    }

}
