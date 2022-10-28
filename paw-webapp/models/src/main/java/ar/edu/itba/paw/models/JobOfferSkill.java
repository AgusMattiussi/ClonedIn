package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.ids.JobOfferSkillId;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "aptitudOfertaLaboral")
@IdClass(JobOfferSkillId.class)
public class JobOfferSkill {
    
    //TODO: chequear mapeo de relaciones

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idOferta")
    private JobOffer jobOffer;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idAptitud")
    private Skill skill;

    public JobOfferSkill(JobOffer jobOffer, Skill skill) {
        this.jobOffer = jobOffer;
        this.skill = skill;
    }

    /* package */ JobOfferSkill() {
        //Just for Hibernate, we love you!
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }

    public Skill getSkill() {
        return skill;
    }

    @Override
    public String toString() {
        return "JobOfferSkill{" +
                "jobOffer=" + jobOffer +
                ", skill=" + skill +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobOfferSkill that = (JobOfferSkill) o;
        return jobOffer.equals(that.jobOffer) && skill.equals(that.skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobOffer, skill);
    }
}
