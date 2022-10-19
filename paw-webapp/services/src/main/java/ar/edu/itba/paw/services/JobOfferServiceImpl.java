package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.services.JobOfferService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Primary
@Service
public class JobOfferServiceImpl implements JobOfferService {

    private final JobOfferDao jobOfferDao;
    private final JobOfferSkillDao jobOfferSkillDao;

    @Autowired
    public JobOfferServiceImpl(JobOfferDao jobOfferDao, JobOfferSkillDao jobOfferSkillDao){
        this.jobOfferDao = jobOfferDao;
        this.jobOfferSkillDao= jobOfferSkillDao;
    }

    @Override
    public JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality) {
        return jobOfferDao.create(enterprise, category, position, description, salary, modality);
    }


    @Override
    public Optional<JobOffer> findById(long id) {
        return jobOfferDao.findById(id);
    }

    @Override
    public List<JobOffer> findByEnterpriseId(long enterpriseID) {
        return jobOfferDao.findByEnterpriseId(enterpriseID);
    }

    @Override
    public List<JobOffer> findByEnterpriseId(long enterpriseID, int page, int pageSize) {
        return jobOfferDao.findByEnterpriseId(enterpriseID, page, pageSize);
    }

    @Override
    public List<JobOffer> findActiveByEnterpriseId(long enterpriseID) {
        return jobOfferDao.findActiveByEnterpriseId(enterpriseID);
    }

    @Override
    public List<JobOffer> findActiveByEnterpriseId(long enterpriseID, int page, int pageSize) {
        return jobOfferDao.findActiveByEnterpriseId(enterpriseID, page, pageSize);
    }

    @Override
    public Integer getJobOffersCountForEnterprise(long enterpriseID) {
        return jobOfferDao.getJobOffersCountForEnterprise(enterpriseID);
    }

    @Override
    public Map<Long, List<Skill>> getJobOfferSkillsMapForEnterprise(long enterpriseID, int page, int pageSize) {
        List<JobOffer> jobOfferList = findByEnterpriseId(enterpriseID, page, pageSize);
        Map<Long, List<Skill>> jobOfferSkillMap = new HashMap<>();
        for (JobOffer jobOffer : jobOfferList) {
            jobOfferSkillMap.put(jobOffer.getId(), jobOfferSkillDao.getSkillsForJobOffer(jobOffer.getId()));
        }
        return jobOfferSkillMap;
    }

    @Override
    public void closeJobOffer(long jobOfferID) {
        jobOfferDao.closeJobOffer(jobOfferID);
    }

    @Override
    public void cancelJobOffer(long jobOfferID) {
        jobOfferDao.cancelJobOffer(jobOfferID);
    }
}
