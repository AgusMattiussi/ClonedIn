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

    @Autowired
    public JobOfferServiceImpl(JobOfferDao jobOfferDao){
        this.jobOfferDao = jobOfferDao;
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
    public long getJobOffersCount() {
        return jobOfferDao.getJobOffersCount();
    }

    @Override
    public List<JobOffer> getAllJobOffers(int page, int pageSize) {
        return jobOfferDao.getAllJobOffers(page, pageSize);
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
    public List<JobOffer> findActiveByEnterprise(Enterprise enterprise) {
        return jobOfferDao.findActiveByEnterprise(enterprise);
    }

    @Override
    public List<JobOffer> findActiveByEnterprise(Enterprise enterprise, int page, int pageSize) {
        return jobOfferDao.findActiveByEnterprise(enterprise, page, pageSize);
    }


    @Override
    public long getJobOffersCountForEnterprise(Enterprise enterprise) {
        return jobOfferDao.getJobOffersCountForEnterprise(enterprise);
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
    public void closeJobOffer(JobOffer jobOffer) {
        jobOfferDao.closeJobOffer(jobOffer);
    }

    @Override
    public void cancelJobOffer(JobOffer jobOffer) {
        jobOfferDao.cancelJobOffer(jobOffer);
    }
}
