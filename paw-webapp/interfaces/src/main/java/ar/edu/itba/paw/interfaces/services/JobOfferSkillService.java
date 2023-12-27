package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;

import java.util.List;

public interface JobOfferSkillService {

    void addSkillToJobOffer(Skill skill, JobOffer jobOffer);

    void addSkillToJobOffer(List<Skill> skills, JobOffer jobOffer);

    List<JobOffer> getJobOffersWithSkill(Skill skill);

    List<Skill> getSkillsForJobOffer(JobOffer jobOffer);
}
