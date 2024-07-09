package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.services.JobOfferService;
import ar.edu.itba.paw.interfaces.services.JobOfferSkillService;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary
@Service
public class JobOfferSkillServiceImpl implements JobOfferSkillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobOfferServiceImpl.class);

    @Autowired
    private JobOfferService jobOfferService;

    private final JobOfferSkillDao jobOfferSkillDao;

    @Autowired
    public JobOfferSkillServiceImpl(JobOfferSkillDao jobOfferSkillDao){
        this.jobOfferSkillDao = jobOfferSkillDao;
    }

    @Override
    public void addSkillToJobOffer(Skill skill, JobOffer jobOffer) {
        jobOfferSkillDao.addSkillToJobOffer(skill, jobOffer);
        LOGGER.debug("A new skill was added to the job offer with id: {}", jobOffer.getId());
    }

    @Override
    public void addSkillsToJobOffer(List<Skill> skills, JobOffer jobOffer) {
        for(Skill skill : skills)
            jobOfferSkillDao.addSkillToJobOffer(skill, jobOffer);
    }

    @Override
    public List<JobOffer> getJobOffersWithSkill(Skill skill) {
        return jobOfferSkillDao.getJobOffersWithSkill(skill);
    }


    @Override
    public List<Skill> getSkillsForJobOffer(JobOffer jobOffer) {
        return jobOfferSkillDao.getSkillsForJobOffer(jobOffer);
    }

    @Override
    @Transactional
    public PaginatedResource<Skill> getSkillsForJobOffer(String descriptionLike, long jobOfferId, int page, int pageSize) {
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("JobOffer with id {} was not found - getSkillsForJobOffer", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        List<Skill> skills = jobOfferSkillDao.getSkillsForJobOffer(descriptionLike, jobOffer, page - 1, pageSize);
        long skillCount = this.getSkillCountForJobOffer(descriptionLike, jobOffer);
        long maxPages = (long) Math.ceil((double) skillCount / pageSize);

        return new PaginatedResource<>(skills, page, maxPages);
    }

    @Override
    public long getSkillCountForJobOffer(String descriptionLike, JobOffer jobOffer) {
        return jobOfferSkillDao.getSkillCountForJobOffer(descriptionLike, jobOffer);
    }
}
