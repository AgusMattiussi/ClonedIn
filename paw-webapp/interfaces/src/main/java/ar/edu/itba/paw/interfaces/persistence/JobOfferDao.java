package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface JobOfferDao {

    JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality);

    Optional<JobOffer> findById(long id);

    List<JobOffer> findByEnterprise(Enterprise enterprise);

    List<JobOffer> findByEnterprise(Enterprise enterprise, int page, int pageSize);

    List<JobOffer> getActiveJobOffersListByEnterpriseId(long enterpriseID);

    List<JobOffer> getAllJobOffers();

    Integer getJobOffersCount();

    List<JobOffer> getJobOffersList(int page, int pageSize);

    List<JobOffer> getActiveJobOffersListByEnterpriseId(long enterpriseID, int page, int pageSize);

    Integer getJobOffersCountForEnterprise(long enterpriseID);

    List<JobOffer> getJobOffersListByEnterprise(int page, int pageSize, String name);

    List<JobOffer> getJobOffersListByFilters(int page, int pageSize, String categoryId, String modality);

    Integer getActiveJobOffersCount(String categoryId, String modality);

    void closeJobOffer(JobOffer jobOffer);

    void cancelJobOffer(JobOffer jobOffer);

}
