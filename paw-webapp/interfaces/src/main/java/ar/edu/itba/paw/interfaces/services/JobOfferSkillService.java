package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.utils.PaginatedResource;

import java.util.List;

// TODO: delete unused functions (and implementations)
public interface JobOfferSkillService {

    void addSkillToJobOffer(Skill skill, JobOffer jobOffer);

    void addSkillToJobOffer(List<Skill> skills, JobOffer jobOffer);

    List<JobOffer> getJobOffersWithSkill(Skill skill);

    List<Skill> getSkillsForJobOffer(JobOffer jobOffer);

    PaginatedResource<Skill> getSkillsForJobOffer(long jobOfferId, int page, int pageSize);


    long getSkillCountForJobOffer(JobOffer jobOffer);
}
