package ar.edu.itba.paw.models;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "experiencia")
@Check(constraints="(mesDesde BETWEEN 1 AND 12) AND (anioDesde BETWEEN 1900 AND 2100) AND ((mesHasta IS NULL AND anioHasta IS NULL) OR ((mesHasta BETWEEN 1 AND 12) AND (anioHasta BETWEEN 1900 AND 2100))) AND ((mesHasta IS NULL AND anioHasta IS NULL) OR (anioHasta > anioDesde) OR (anioHasta = anioDesde AND mesHasta >= mesDesde))")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "experiencia_id_seq")
    @SequenceGenerator(sequenceName = "experiencia_id_seq", name = "experiencia_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", referencedColumnName="id")
    private User user;

    @Column(name = "mesDesde")
    private int monthFrom;

    @Column(name = "anioDesde")
    private int yearFrom;

    @Column(name = "mesHasta")
    private Integer monthTo;

    @Column(name = "anioHasta")
    private Integer yearTo;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "posicion", nullable = false)
    private String position;

    @Column(name = "empresa", length = 100, nullable = false)
    private String enterpriseName;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descripcion")
    private String description;


    public Experience(Long id, User user, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        this.id = id;
        this.user = user;
        this.monthFrom = monthFrom;
        this.yearFrom = yearFrom;
        this.monthTo = monthTo;
        this.yearTo = yearTo;
        this.enterpriseName = enterpriseName;
        this.position = position;
        this.description = description;
    }

    public Experience(User user, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        this(null, user, monthFrom, yearFrom, monthTo, yearTo, enterpriseName, position, description);
    }

    /* package */ Experience() {
        // Just for Hibernate, we love you!
    }

    public Long getId() {
        return id;
    }

    public User getUserId() {
        return user;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public int getMonthFrom() {
        return monthFrom;
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public Integer getMonthTo() {
        return monthTo;
    }

    public Integer getYearTo() {
        return yearTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return id == that.id && user.getId() == that.user.getId();
    }

    @Override
    public String toString() {
        return "Experience{" +
                "id=" + id +
                ", user=" + user +
                ", monthFrom=" + monthFrom +
                ", yearFrom=" + yearFrom +
                ", monthTo=" + monthTo +
                ", yearTo=" + yearTo +
                ", enterpriseName='" + enterpriseName + '\'' +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user.getId());
    }
}
