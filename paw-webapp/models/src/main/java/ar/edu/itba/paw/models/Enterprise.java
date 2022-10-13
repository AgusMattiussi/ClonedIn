package ar.edu.itba.paw.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "empresa")
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id_seq")
    @SequenceGenerator(sequenceName = "usuario_id_seq", name = "usuario_id_seq", allocationSize = 1)
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 100, nullable = false)
    private String name;
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "location")
    private String location;
    private Category category; // FIXme --> ?
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;
    private Long imageId; // FIXme: --> ?


    public Enterprise(Long id, String name, String email, String password, String location, Category category, String description, Long imageId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.category = category;
        this.description = description;
        this.imageId = imageId;
    }
    public Enterprise(String name, String email, String password, String location, Category category, String description, Long imageId) {
        this(null, name, email, password, location, category, description, imageId);
    }

    /* package */ Enterprise() {
        // Just for Hibernate, we love you!
    }

    public Long getId() {
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

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public Long getImageId() {
        return imageId;
    }

    @Override
    public String toString() {
        return "Enterprise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", category=" + category.toString() +
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
