package ar.edu.itba.paw.models;

public class Skill {
    private long id;

    private String description;

    public Skill(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
