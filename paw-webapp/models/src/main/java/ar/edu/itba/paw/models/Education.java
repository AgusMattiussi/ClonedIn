package ar.edu.itba.paw.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "educacion")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "educacion_id_seq")
    @SequenceGenerator(sequenceName = "educacion_id_seq", name = "educacion_id_seq", allocationSize = 1)
    private Long id;

    //FIXme: o @OneToMany
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private User user;
    //FIXme: check
    @Column(name = "mesDesde", columnDefinition = "CHECK(mesDesde BETWEEN 1 AND 12)")
    private int monthFrom;
    @Column(name = "anioDesde", columnDefinition = "CHECK(anioDesde BETWEEN 1900 AND 2100)")
    private int yearFrom;
    @Column(name = "mesHasta", columnDefinition = "CHECK((mesHasta IS NULL AND anioHasta IS NULL) OR ((mesHasta BETWEEN 1 AND 12) AND (anioHasta BETWEEN 1900 AND 2100)))")
    private int monthTo;
    @Column(name = "anioHasta", columnDefinition = "CHECK((mesHasta IS NULL AND anioHasta IS NULL) OR (anioHasta > anioDesde) OR (anioHasta = anioDesde AND mesHasta >= mesDesde))")
    private int yearTo;
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "titulo", nullable = false)
    private String title;
    @Column(name = "institucion", length = 100, nullable = false)
    private String institutionName;
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descripcion")
    private String description;

    public Education(Long id, User user, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description) {
        this.id = id;
        this.user = user;
        this.monthFrom = monthFrom;
        this.yearFrom = yearFrom;
        this.monthTo = monthTo;
        this.yearTo = yearTo;
        this.title = title;
        this.institutionName = institutionName;
        this.description = description;
    }

    public Education(User user, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description) {
        this(null, user, monthFrom, yearFrom, monthTo, yearTo, title, institutionName, description);
    }

    /* package */ Education() {
        // Just for Hibernate, we love you!
    }

    public Long getId() {
        return id;
    }

    public User getUserId() {
        return user;
    }

    public int getMonthFrom() {
        return monthFrom;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public int getMonthTo() {
        return monthTo;
    }

    public int getYearTo() {
        return yearTo;
    }

    public String getTitle() {
        return title;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Education{" +
                "id=" + id +
                ", user=" + user +
                ", monthFrom=" + monthFrom +
                ", yearFrom=" + yearFrom +
                ", monthTo=" + monthTo +
                ", yearTo=" + yearTo +
                ", title='" + title + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Education education = (Education) o;
        return id == education.id && user.getId() == education.user.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user.getId());
    }
}
