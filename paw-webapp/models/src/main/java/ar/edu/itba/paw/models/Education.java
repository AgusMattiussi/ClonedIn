package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.enums.Month;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "educacion")
@Check(constraints = "(anioDesde BETWEEN 1900 AND 2100) AND ((mesHasta IS NULL AND anioHasta IS NULL) OR ((mesHasta BETWEEN 1 AND 12) AND (anioHasta BETWEEN 1900 AND 2100))) AND ((mesHasta IS NULL AND anioHasta IS NULL) OR (anioHasta > anioDesde) OR (anioHasta = anioDesde AND mesHasta >= mesDesde))")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "educacion_id_seq")
    @SequenceGenerator(sequenceName = "educacion_id_seq", name = "educacion_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    private User user;

    @Column(name = "mesDesde")
    private Integer monthFrom;

    @Column(name = "anioDesde")
    private Integer yearFrom;

    @Column(name = "mesHasta")
    private Integer monthTo;

    @Column(name = "anioHasta")
    private Integer yearTo;

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

    public Education(User user, Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo, String title, String institutionName, String description) {
        this(null, user, monthFrom, yearFrom, monthTo, yearTo, title, institutionName, description);
    }

    /* package */ Education() {
        // Just for Hibernate, we love you!
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
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
        final StringBuilder sb = new StringBuilder("Education{");
        sb.append("id=").append(id);
        sb.append(", user=").append(user);
        sb.append(", monthFrom=").append(monthFrom);
        sb.append(", yearFrom=").append(yearFrom);
        sb.append(", monthTo=").append(monthTo);
        sb.append(", yearTo=").append(yearTo);
        sb.append(", title='").append(title).append('\'');
        sb.append(", institutionName='").append(institutionName).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Education education = (Education) o;
        return Objects.equals(id, education.id) && Objects.equals(user.getId(), education.user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user.getId());
    }
}
