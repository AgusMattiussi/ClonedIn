package ar.edu.itba.paw.models;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id_seq")
    @SequenceGenerator(sequenceName = "usuario_id_seq", name = "usuario_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "contrasenia", length = 100, nullable = false)
    private String password;

    @Column(name = "nombre", length = 100, nullable = false)
    private String name;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "ubicacion")
    private String location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRubro")
    private Category category;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "posicionActual")
    private String currentPosition;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descripcion")
    private String description;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "educacion")
    private String education;

    @Column(name = "visibilidad"/*, columnDefinition = "integer default 1"*/)
    private int visibility;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idImagen")
    private Image image;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Experience> experiences;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserSkill> userSkills;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Contact> contacts;

    public User(Long id, String email, String password, String name, String location, Category category, String currentPosition, String description, String education, int visibility, Image image) {
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
        this.image = image;
    }

    public User(String email, String password, String name, String location, Category category, String currentPosition, String description, String education, int visibility, Image image) {
        this(null, email, password, name, location, category, currentPosition, description, education, visibility, image);
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

    public Image getImage() {
        return image;
    }

    public Set<Experience> getExperiences() {
        return experiences;
    }

    public Set<UserSkill> getUserSkillSet() {
        return userSkills;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public List<Skill> getSkills(){
        return userSkills.stream().map(UserSkill::getSkill).collect(Collectors.toList());
    }

    public int getYearsOfExperience() {
        int result = 0;
        int currentYear=2022;
        
        for (Experience experience : experiences)
            result += (experience.getYearTo() == null? currentYear:experience.getYearTo()) - experience.getYearFrom();

        return result;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", category=" + category +
                ", currentPosition='" + currentPosition + '\'' +
                ", description='" + description + '\'' +
                ", education='" + education + '\'' +
                ", visibility=" + visibility +
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
