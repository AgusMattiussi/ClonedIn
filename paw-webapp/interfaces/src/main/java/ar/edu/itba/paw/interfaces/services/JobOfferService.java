package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.enums.JobOfferAvailability;
import ar.edu.itba.paw.models.enums.JobOfferModality;
import ar.edu.itba.paw.models.enums.JobOfferSorting;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// TODO: delete unused functions (and implementations)
public interface JobOfferService {

    JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, JobOfferModality modality);

    JobOffer create(long enterpriseId, String categoryName, String position, String description, BigDecimal salary, String modalityName, List<String> skillDescriptions);

    List<Skill> getSkills(long jobOfferId);

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

    PaginatedResource<JobOffer> getJobOffersListByFilters(String categoryName, JobOfferModality modality, Long skillId, String skillDescription,
                                                          Long enterpriseId, String searchTerm, String position, BigDecimal minSalary,
                                                          BigDecimal maxSalary, JobOfferSorting sortBy, boolean onlyActive, int page, int pageSize);

    List<JobOffer> getJobOffersListByFilters(Category category, JobOfferModality modality, String term, BigDecimal minSalary,
                                             BigDecimal maxSalary, int page, int pageSize);

    long getJobOfferCount(Category category, JobOfferModality modality, Long skillId, String skillDescription, Long enterpriseId,
                          String searchTerm, String position, BigDecimal minSalary, BigDecimal maxSalary, boolean onlyActive);


    long getJobOfferCount(Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary);

    void closeJobOffer(JobOffer jobOffer);

    void cancelJobOffer(JobOffer jobOffer);

    void updateJobOfferAvailability(long jobOfferId, JobOfferAvailability availability);

    boolean isJobOfferOwner(long jobOfferId, long enterpriseId);
}
