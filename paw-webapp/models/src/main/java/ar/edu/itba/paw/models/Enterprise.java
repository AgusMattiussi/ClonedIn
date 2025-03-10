package ar.edu.itba.paw.models;

import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "empresa")
public class Enterprise {
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
        return "Enterprise{" + "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", category=" + category +
                ", workers='" + workers + '\'' +
                ", year=" + year +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
