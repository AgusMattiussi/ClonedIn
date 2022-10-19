package ar.edu.itba.paw.models;

import java.io.Serializable;
import java.util.Objects;

public class UserSkillId implements Serializable {
    private Long userId;
    private Long skillId;

    public UserSkillId(Long userId, Long skillId) {
        this.userId = userId;
        this.skillId = skillId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSkillId that = (UserSkillId) o;
        return userId.equals(that.userId) && skillId.equals(that.skillId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, skillId);
    }

}
