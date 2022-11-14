package ar.edu.itba.paw.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "aptitud")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aptitud_id_seq")
    @SequenceGenerator(sequenceName = "aptitud_id_seq", name = "aptitud_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descripcion")
    private String description;

    @OneToMany(mappedBy = "skill")
    private Set<JobOfferSkill> jobOfferSkillSet;

    @OneToMany(mappedBy = "skill")
    private Set<UserSkill> userSkillSet;


    public Skill(Long id, String description) {
        this.id = id;
        this.description = description;
    }

    public Skill(String description) {
        this(null, description);
    }

    /* package */ Skill() {
        // Just for Hibernate, we love you!
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Skill skill = (Skill) o;
        return id.equals(skill.id) && description.equals(skill.description);
    }

    public Set<JobOfferSkill> getJobOfferSkillSet() {
        return jobOfferSkillSet;
    }

    public Set<UserSkill> getUserSkillSet() {
        return userSkillSet;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
