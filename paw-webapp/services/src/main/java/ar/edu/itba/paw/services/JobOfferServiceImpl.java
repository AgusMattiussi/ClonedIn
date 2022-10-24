package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.services.JobOfferService;
import ar.edu.itba.paw.models.*;
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
    public List<JobOffer> getAllJobOffers() {
        return jobOfferDao.getAllJobOffers();
    }

    @Override
    public Integer getJobOffersCount() {
        return jobOfferDao.getJobOffersCount();
    }

    @Override
    public List<JobOffer> getJobOffersList(int page, int pageSize) {
        return jobOfferDao.getJobOffersList(page, pageSize);
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
    public List<JobOffer> getjobOffersListByFilters(int page, int pageSize, String categoryId, String modality) {
        return jobOfferDao.getjobOffersListByFilters(page, pageSize, categoryId, modality);
    }

    @Override
    public List<JobOffer> getJobOffersListByEnterprise(int page, int pageSize, String term) {
        return jobOfferDao.getJobOffersListByEnterprise(page, pageSize, term);
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
