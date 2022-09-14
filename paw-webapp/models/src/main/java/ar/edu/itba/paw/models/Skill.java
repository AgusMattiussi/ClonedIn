package ar.edu.itba.paw.models;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return id == skill.id && description.equals(skill.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
