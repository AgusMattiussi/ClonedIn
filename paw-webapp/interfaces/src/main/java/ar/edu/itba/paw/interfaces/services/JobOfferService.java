package ar.edu.itba.paw.interfaces.services;


import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface JobOfferService {

    JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality);

    List<JobOffer> getAllJobOffers();

    long getJobOffersCount();

    List<JobOffer> getAllJobOffers(int page, int pageSize);

    Optional<JobOffer> findById(long id);

    List<JobOffer> findByEnterprise(Enterprise enterprise);

    List<JobOffer> findByEnterprise(Enterprise enterprise, int page, int pageSize);

    List<JobOffer> findActiveByEnterprise(Enterprise enterprise);

    List<JobOffer> findActiveByEnterprise(Enterprise enterprise, int page, int pageSize);

    long getJobOffersCountForEnterprise(Enterprise enterprise);

    long getActiveJobOffersCountForEnterprise(Enterprise enterprise);

    List<JobOffer> getJobOffersListByFilters(Category category, String modality, String enterpriseName, String searchTerm,
                                             String position, BigDecimal minSalary, BigDecimal maxSalary, int page, int pageSize);

    List<JobOffer> getJobOffersListByFilters(Category category, String modality, String term, BigDecimal minSalary, BigDecimal maxSalary, int page, int pageSize);

    long getActiveJobOffersCount(Category category, String modality, String enterpriseName, String searchTerm,
                                 String position, BigDecimal minSalary, BigDecimal maxSalary);


    long getActiveJobOffersCount(Category category, String modality, String term, BigDecimal minSalary, BigDecimal maxSalary);

    void closeJobOffer(JobOffer jobOffer);

    void cancelJobOffer(JobOffer jobOffer);

}
