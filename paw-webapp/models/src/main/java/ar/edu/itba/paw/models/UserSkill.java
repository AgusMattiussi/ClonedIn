package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.ids.UserSkillId;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="aptitudUsuario")
@IdClass(UserSkillId.class)
public class UserSkill {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUsuario")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAptitud")
    private Skill skill;

    public UserSkill(User user, Skill skill) {
        this.user = user;
        this.skill = skill;
    }

    /* package */ UserSkill() {
        //Just for Hibernate, we love you!
    }

    public User getUser() {
        return user;
    }

    public Skill getSkill() {
        return skill;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserSkill{");
        sb.append("user=").append(user);
        sb.append(", skill=").append(skill);
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
        UserSkill userSkill = (UserSkill) o;
        return Objects.equals(user, userSkill.user) && Objects.equals(skill, userSkill.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, skill);
    }
}
