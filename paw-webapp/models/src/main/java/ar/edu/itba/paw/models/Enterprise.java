package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.enums.Role;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "empresa")
public class Enterprise implements CustomUserDetails {
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
    @Column(name = "empleados")
    private String workers;

    @Column(name = "año")
    private Integer year;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "link")
    private String link;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descripcion")
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idImagen")
    private Image image;

    /*@Transient
    @Enumerated(EnumType.STRING)
    private Role role;*/

    @OneToMany(mappedBy = "enterprise", fetch = FetchType.LAZY)
    private Set<Contact> contacts;

    @OneToMany(mappedBy = "enterprise", fetch = FetchType.LAZY)
    private Set<JobOffer> jobOffersSet;


    public Enterprise(Long id, String name, String email, String password, String location, Category category, String workers, Integer year, String link, String description, Image image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.category = category;
        this.workers = workers;
        this.year = year;
        this.link = link;
        this.description = description;
        this.image = image;
    }
    public Enterprise(String name, String email, String password, String location, Category category, String workers, Integer year, String link, String description, Image image) {
        this.id = null;
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.category = category;
        this.workers = workers;
        this.year = year;
        this.link = link;
        this.description = description;
        this.image = image;
    }

    /* package */ Enterprise() {
        // Just for Hibernate, we love you!
    }

    @Override
    public Role getRole() {
        return Role.ENTERPRISE;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.ENTERPRISE.name()));
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

    public String getWorkers() {
        return workers;
    }

    public Integer getYear() {
        return year;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public Set<JobOffer> getJobOffersSet() {
        return jobOffersSet;
    }

    public boolean isJobOfferOwner(long jobOfferId){
        if(jobOffersSet == null || jobOffersSet.isEmpty()) {
            return false;
        }

        for(JobOffer jo : jobOffersSet){
            if(jo.getId().equals(jobOfferId)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enterprise that = (Enterprise) o;
        return Objects.equals(id, that.id) && email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Enterprise{");
        sb.append("id=").append(id);
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", category=").append(category);
        sb.append(", workers='").append(workers).append('\'');
        sb.append(", year=").append(year);
        sb.append(", link='").append(link).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
