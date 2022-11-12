package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "ofertaLaboral")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ofertalaboral_id_seq")
    @SequenceGenerator(sequenceName = "ofertalaboral_id_seq", name = "ofertalaboral_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idEmpresa")
    private Enterprise enterprise;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRubro")
    private Category category;

    @Column(name = "posicion", nullable = false)
    private String position;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "salario")
    private BigDecimal salary;

    @Column(name = "modalidad")
    private String modality;

    @Column(name = "disponible")
    private String available;

    @OneToMany(mappedBy = "jobOffer", fetch = FetchType.EAGER)
    private Set<JobOfferSkill> jobOfferSkillSet;

    @OneToMany(mappedBy = "jobOffer", fetch = FetchType.EAGER)
    private Set<Contact> contactSet;

    public JobOffer(Long id, Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality, String available) {
        this.id = id;
        this.enterprise = enterprise;
        this.category = category;
        this.position = position;
        this.description = description;
        this.salary = salary;
        this.modality = modality;
        this.available = available;
    }

    public JobOffer(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality, String available) {;
        this.enterprise = enterprise;
        this.category = category;
        this.position = position;
        this.description = description;
        this.salary = salary;
        this.modality = modality;
        this.available = available;
    }

    /* package */ JobOffer() {
        // Just for Hibernate, we love you!
    }


    public Long getId() {
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

    public Set<JobOfferSkill> getJobOfferSkillSet() {
        return jobOfferSkillSet;
    }

    public List<Skill> getSkills() {
        return jobOfferSkillSet.stream().map(JobOfferSkill::getSkill).collect(Collectors.toList());
    }

    public Set<Contact> getContactSet() {
        return contactSet;
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
