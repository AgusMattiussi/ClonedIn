package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JobOfferService {

    JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality);

    List<JobOffer> getAllJobOffers();

    Integer getJobOffersCount();

    List<JobOffer> getJobOffersList(int page, int pageSize);

    Optional<JobOffer> findById(long id);

    List<JobOffer> findByEnterpriseId(long enterpriseID);
    List<JobOffer> findByEnterpriseId(long enterpriseID, int page, int pageSize);

    List<JobOffer> findActiveByEnterpriseId(long enterpriseID);

    List<JobOffer> findActiveByEnterpriseId(long enterpriseID, int page, int pageSize);

    Integer getJobOffersCountForEnterprise(long enterpriseID);

    List<JobOffer> getjobOffersListByFilters(int page, int pageSize, String categoryId, String modality);

    List<JobOffer> getJobOffersListByEnterprise(int page, int pageSize, String term);

    Map<Long, List<Skill>> getJobOfferSkillsMapForEnterprise(long enterpriseID, int page, int pageSize);

    void closeJobOffer(long jobOfferID);

    void cancelJobOffer(long jobOfferID);

}
