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

    List<JobOffer> findByEnterpriseId(long enterpriseID);
    List<JobOffer> findByEnterpriseId(long enterpriseID, int page, int pageSize);

    List<JobOffer> findActiveByEnterpriseId(long enterpriseID);

    List<JobOffer> findActiveByEnterpriseId(long enterpriseID, int page, int pageSize);

    Integer getJobOffersCountForEnterprise(long enterpriseID);

    void closeJobOffer(long jobOfferID);

    void cancelJobOffer(long jobOfferID);

}
