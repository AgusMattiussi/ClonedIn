package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;

import java.util.List;

public interface JobOfferSkillDao {
    
    boolean addSkillToJobOffer(String skillDescription, long jobOfferID);

    boolean addSkillToJobOffer(long skillID, long jobOfferID);

    List<JobOffer> getJobOffersWithSkill(String skillDescription);

    List<JobOffer> getJobOffersWithSkill(long skillID);

    List<Skill> getSkillsForJobOffer(long jobOfferID);
}
