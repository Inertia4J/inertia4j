package io.gitlab.inertia4j.spring.example;

public class Record implements Comparable<Record> {
    private final int id;
    private final String name;

    public Record(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Record that) {
        return Integer.compare(this.id, that.id);
    }
}
