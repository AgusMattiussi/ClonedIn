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

public interface JobOfferService {

    JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, JobOfferModality modality);

    JobOffer create(long enterpriseId, String categoryName, String position, String description, BigDecimal salary, String modalityName, List<String> skillDescriptions);

    List<Skill> getSkills(long jobOfferId);

    Optional<JobOffer> findById(long id);


    PaginatedResource<JobOffer> getJobOffersListByFilters(String categoryName, JobOfferModality modality, Long skillId, String skillDescription,
                                                          Long enterpriseId, String searchTerm, String position, BigDecimal minSalary,
                                                          BigDecimal maxSalary, JobOfferSorting sortBy, boolean onlyActive, int page, int pageSize);

    long getJobOfferCount(Category category, JobOfferModality modality, Long skillId, String skillDescription, Long enterpriseId,
                          String searchTerm, String position, BigDecimal minSalary, BigDecimal maxSalary, boolean onlyActive);


    void closeJobOffer(JobOffer jobOffer);

    void cancelJobOffer(JobOffer jobOffer);

    void updateJobOfferAvailability(long jobOfferId, JobOfferAvailability availability);

    boolean isJobOfferOwner(long jobOfferId, long enterpriseId);
}
