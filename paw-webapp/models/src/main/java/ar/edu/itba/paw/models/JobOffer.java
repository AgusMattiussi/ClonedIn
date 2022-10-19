package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "ofertaLaboral")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ofertalaboral_id_seq")
    @SequenceGenerator(sequenceName = "ofertalaboral_id_seq", name = "ofertalaboral_id_seq", allocationSize = 1)
    @Column(name = "id")
    private final Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpresa")
    private final Enterprise enterprise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRubro")
    private final Category category;

    @Column(name = "posicion", nullable = false)
    private final String position;

    @Column(name = "descripcion")
    private final String description;

    @Column(name = "salary")
    private final BigDecimal salary;

    @Column(name = "modality")
    private final String modality;

    @Column(name = "available")
    private final String available;

    public JobOffer(long id, Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality, String available) {
        this.id = id;
        this.enterprise = enterprise;
        this.category = category;
        this.position = position;
        this.description = description;
        this.salary = salary;
        this.modality = modality;
        this.available = available;
    }

    public long getId() {
        return id;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public Long getEnterpriseID() {
        return enterprise.getId();
    }

    public Category getCategory() {
        return category;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getModality() {
        return modality;
    }

    public String getAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return "JobOffer{" +
                "id=" + id +
                ", enterprise=" + enterprise +
                ", category=" + category +
                ", position='" + position + '\'' +
                ", description='" + description + '\'' +
                ", salary=" + salary +
                ", modality='" + modality + '\'' +
                ", available='" + available + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobOffer jobOffer = (JobOffer) o;
        return id.equals(jobOffer.id) && enterprise.equals(jobOffer.enterprise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enterprise);
    }
}
