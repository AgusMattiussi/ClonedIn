package ar.edu.itba.paw.models.ids;

import java.io.Serializable;
import java.util.Objects;

public class JobOfferSkillId implements Serializable {
    private Long jobOffer;
    private Long skill;

    public JobOfferSkillId(Long jobOffer, Long skill) {
        this.jobOffer = jobOffer;
        this.skill = skill;
    }

    public JobOfferSkillId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobOfferSkillId that = (JobOfferSkillId) o;
        return Objects.equals(jobOffer, that.jobOffer) && Objects.equals(skill, that.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobOffer, skill);
    }
}
