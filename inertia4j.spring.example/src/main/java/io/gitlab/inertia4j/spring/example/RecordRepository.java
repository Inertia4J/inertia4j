package io.gitlab.inertia4j.spring.example;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Component
public class RecordRepository {
    public Set<Record> getAllRecords() {
        SortedSet<Record> records = new TreeSet<>();
        records.add(new Record(1, "John Doe"));
        records.add(new Record(2, "Jane Smith"));
        records.add(new Record(3, "Alice Johnson"));
        return records;
    }

    public Record getRecordById(int id) {
        return new Record(id, "Mock Name " + id);
    }
}
