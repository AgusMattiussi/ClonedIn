package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.services.JobOfferSkillService;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class JobOfferSkillServiceImpl implements JobOfferSkillService {

    private final JobOfferSkillDao jobOfferSkillDao;

    @Autowired
    public JobOfferSkillServiceImpl(JobOfferSkillDao jobOfferSkillDao){
        this.jobOfferSkillDao = jobOfferSkillDao;
    }

    @Override
    public void addSkillToJobOffer(Skill skill, JobOffer jobOffer) {
        jobOfferSkillDao.addSkillToJobOffer(skill, jobOffer);
    }

    @Override
    public void addSkillToJobOffer(List<Skill> skills, JobOffer jobOffer) {
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
}
