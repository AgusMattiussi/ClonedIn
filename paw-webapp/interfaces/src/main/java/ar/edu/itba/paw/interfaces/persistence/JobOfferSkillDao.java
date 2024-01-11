package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;

import java.util.List;

public interface JobOfferSkillDao {

    void addSkillToJobOffer(Skill skill, JobOffer jobOffer);


    List<JobOffer> getJobOffersWithSkill(Skill skill);

    List<Skill> getSkillsForJobOffer(JobOffer jobOffer);
}
