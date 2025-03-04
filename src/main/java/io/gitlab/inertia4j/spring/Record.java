package io.gitlab.inertia4j.spring;

public class Record {
    private int id;
    private String name;

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
}

