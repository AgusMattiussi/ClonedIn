package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.enums.JobOfferModality;
import ar.edu.itba.paw.models.enums.JobOfferSorting;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface JobOfferDao {

    JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, JobOfferModality modality);

    Optional<JobOffer> findById(long id);

    List<JobOffer> findByEnterprise(Enterprise enterprise);

    List<JobOffer> findByEnterprise(Enterprise enterprise, int page, int pageSize);

    List<JobOffer> findActiveByEnterprise(Enterprise enterprise);

    List<JobOffer> findActiveByEnterprise(Enterprise enterprise, int page, int pageSize);

    List<JobOffer> getAllJobOffers();

    List<JobOffer> getAllJobOffers(int page, int pageSize);

    long getJobOffersCount();

    long getJobOffersCountForEnterprise(Enterprise enterprise);

    long getActiveJobOffersCountForEnterprise(Enterprise enterprise);

    List<JobOffer> getJobOffersListByFilters(Category category, JobOfferModality modality, Long skillId, String skillDescription,
                                             Long enterpriseId, String searchTerm, String position, BigDecimal minSalary,
                                             BigDecimal maxSalary, JobOfferSorting sortBy, boolean onlyActive, int page, int pageSize);

    List<JobOffer> getJobOffersListByFilters(Category category, JobOfferModality modality, String term, BigDecimal minSalary,
                                             BigDecimal maxSalary, int page, int pageSize);

    long getJobOfferCount(Category category, JobOfferModality modality, Long skillId, String skillDescription, Long enterpriseId, String searchTerm,
                          String position, BigDecimal minSalary, BigDecimal maxSalary, boolean onlyActive);

    long getJobOfferCount(Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary);

    void closeJobOffer(JobOffer jobOffer);

    void cancelJobOffer(JobOffer jobOffer);
}
