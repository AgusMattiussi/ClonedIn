package ar.edu.itba.paw.models;


import javax.persistence.*;
import java.util.Objects;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "usuario")
public class User {

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
    @Column(name = "currentPosition")
    private String currentPosition;
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "education")
    private String education;
    @Column(name = "visibility", columnDefinition = "integer default 1")
    private int visibility;
    private Long imageId; // FIXme: --> ?

    public User(Long id, String email, String password, String name, String location, Category category, String currentPosition, String description, String education, int visibility, Long imageId) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.location = location;
        this.category = category;
        this.currentPosition = currentPosition;
        this.description = description;
        this.education = education;
        this.visibility = visibility;
        this.imageId = imageId;
    }

    public User(String email, String password, String name, String location, Category category, String currentPosition, String description, String education, int visibility, Long imageId) {
        this(null, email, password, name, location, category, currentPosition, description, education, visibility, imageId);
    }

    /* package */ User() {
    // Just for Hibernate, we love you!
    }


    public Long getId() {
        return id;
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

    public String getCurrentPosition() {
        return currentPosition;
    }

    public String getDescription() {
        return description;
    }

    public String getEducation() {
        return education;
    }

    public String getName() {
        return name;
    }

    public int getVisibility() {
        return visibility;
    }

    public Long getImageId() {
        return imageId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", category=" + category.toString() +
                ", currentPosition='" + currentPosition + '\'' +
                ", description='" + description + '\'' +
                ", education='" + education + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
