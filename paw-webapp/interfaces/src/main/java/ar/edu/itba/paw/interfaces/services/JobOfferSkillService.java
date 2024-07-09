package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.utils.PaginatedResource;

import java.util.List;

public interface JobOfferSkillService {

    void addSkillToJobOffer(Skill skill, JobOffer jobOffer);

    void addSkillsToJobOffer(List<Skill> skills, JobOffer jobOffer);

    List<JobOffer> getJobOffersWithSkill(Skill skill);

    List<Skill> getSkillsForJobOffer(JobOffer jobOffer);

    PaginatedResource<Skill> getSkillsForJobOffer(String descriptionLike, long jobOfferId, int page, int pageSize);


    long getSkillCountForJobOffer(String descriptionLike, JobOffer jobOffer);
}
