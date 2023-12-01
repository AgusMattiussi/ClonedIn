package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import ar.edu.itba.paw.models.enums.Role;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuario")
public class User implements UserDetails {

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

    @Transient
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Experience> experiences;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Education> educationSet;

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
        this.role = Role.USER;
    }

    public User(String email, String password, String name, String location, Category category, String currentPosition,
                String description, String education, int visibility, Image image) {
        this.id = null;
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
        this.role = Role.USER;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public Set<Education> getEducationSet() {
        return educationSet;
    }

    public Set<UserSkill> getUserSkills() {
        return userSkills;
    }

    public List<Skill> getSkills(){
        return userSkills.stream().map(UserSkill::getSkill).collect(Collectors.toList());
    }

    public int getYearsOfExperience() {
        int result = 0;
        int currentYear=2022;
        
        for (Experience experience : experiences) {
            result += (experience.getYearTo() == null ? currentYear : experience.getYearTo()) - experience.getYearFrom();
        }

        return result;
    }

    public boolean hasSkill(long skillId){
        if(userSkills == null || userSkills.isEmpty()) {
            return false;
        }

        for(UserSkill usk : userSkills){
            if(usk.getSkill().getId().equals(skillId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEducation(long educationId){
        if(educationSet == null || educationSet.isEmpty()) {
            return false;
        }

        for(Education e : educationSet){
            if(e.getId().equals(educationId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasExperience(long experienceID){
        if(experiences == null || experiences.isEmpty()) {
            return false;
        }

        for(Experience e : experiences){
            if(e.getId().equals(experienceID)) {
                return true;
            }
        }
        return false;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
