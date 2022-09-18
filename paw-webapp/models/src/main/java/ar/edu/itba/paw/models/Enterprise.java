package ar.edu.itba.paw.models;

import java.util.Objects;

public class Enterprise {
    private final long id;
    private final String name;
    private final String email;
    private final String password;
    private final String location;
    private final long categoryId_fk;
    private final String description;

    //TODO: Aniadir imagen

    public Enterprise(long id, String name, String email, String password, String location, long categoryId_fk, String description) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.categoryId_fk = categoryId_fk;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLocation() {
        return location;
    }

    public long getCategoryId_fk() {
        return categoryId_fk;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", categoryId_fk=" + categoryId_fk +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enterprise that = (Enterprise) o;
        return id == that.id && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
