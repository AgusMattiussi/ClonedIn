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
    public List<JobOffer> findByEnterprise(Enterprise enterprise) {
        return jobOfferDao.findByEnterprise(enterprise);
    }

    @Override
    public List<JobOffer> findByEnterprise(Enterprise enterprise, int page, int pageSize) {
        return jobOfferDao.findByEnterprise(enterprise, page, pageSize);
    }


    @Override
    public List<JobOffer> getActiveJobOffersListByEnterpriseId(long enterpriseID) {
        return jobOfferDao.getActiveJobOffersListByEnterpriseId(enterpriseID);
    }

    @Override
    public List<JobOffer> getActiveJobOffersListByEnterpriseId(long enterpriseID, int page, int pageSize) {
        return jobOfferDao.getActiveJobOffersListByEnterpriseId(enterpriseID, page, pageSize);
    }

    @Override
    public Integer getJobOffersCountForEnterprise(long enterpriseID) {
        return jobOfferDao.getJobOffersCountForEnterprise(enterpriseID);
    }

    @Override
    public List<JobOffer> getJobOffersListByFilters(int page, int pageSize, String categoryId, String modality) {
        return jobOfferDao.getJobOffersListByFilters(page, pageSize, categoryId, modality);
    }

    @Override
    public Integer getActiveJobOffersCount(String categoryId, String modality) {
        return jobOfferDao.getActiveJobOffersCount(categoryId, modality);
    }

    @Override
    public List<JobOffer> getJobOffersListByEnterprise(int page, int pageSize, String term) {
        return jobOfferDao.getJobOffersListByEnterprise(page, pageSize, term);
    }


    //TODO: Eliminar este metodo horrible
    @Override
    public Map<Long, List<Skill>> getJobOfferSkillsMapForEnterprise(Enterprise enterprise, int page, int pageSize) {
        List<JobOffer> jobOfferList = findByEnterprise(enterprise, page, pageSize);
        Map<Long, List<Skill>> jobOfferSkillMap = new HashMap<>();
        for (JobOffer jobOffer : jobOfferList) {
            jobOfferSkillMap.put(jobOffer.getId(), jobOfferSkillDao.getSkillsForJobOffer(jobOffer.getId()));
        }
        return jobOfferSkillMap;
    }

    @Override
    public void closeJobOffer(JobOffer jobOffer) {
        jobOfferDao.closeJobOffer(jobOffer);
    }

    @Override
    public void cancelJobOffer(JobOffer jobOffer) {
        jobOfferDao.cancelJobOffer(jobOffer);
    }
}
