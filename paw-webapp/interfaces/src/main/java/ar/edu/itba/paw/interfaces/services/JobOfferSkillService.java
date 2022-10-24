package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;

import java.util.List;

public interface JobOfferSkillService {

    void addSkillToJobOffer(Skill skill, JobOffer jobOffer);

    List<JobOffer> getJobOffersWithSkill(String skillDescription);

    List<JobOffer> getJobOffersWithSkill(long skillID);

    List<Skill> getSkillsForJobOffer(long jobOfferID);
}
