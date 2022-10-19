package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name="aptitudUsuario")
@IdClass(UserSkillId.class)
public class UserSkill {
    @Id
    private Long userId;

    @Id
    private Long skillId;
}
