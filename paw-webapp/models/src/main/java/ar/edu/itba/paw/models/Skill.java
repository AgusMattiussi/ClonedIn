package ar.edu.itba.paw.models;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "aptitud")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habilidad_id_seq")
    @SequenceGenerator(sequenceName = "habilidad_id_seq", name = "habilidad_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "descripcion")
    private String description;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return id.equals(skill.id) && description.equals(skill.description);
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
