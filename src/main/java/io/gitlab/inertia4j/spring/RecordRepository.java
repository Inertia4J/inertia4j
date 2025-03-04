package io.gitlab.inertia4j.spring;
import java.util.HashSet;
import java.util.Set;

public class RecordRepository {
    public Set<Record> getAllRecords() {
        Set<Record> records = new HashSet<>();
        records.add(new Record(1, "John Doe"));
        records.add(new Record(2, "Jane Smith"));
        records.add(new Record(3, "Alice Johnson"));
        return records;
    }

    public Record getRecordById(int id) {
        return new Record(id, "Mock Name " + id);
    }
}
