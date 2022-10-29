package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    List<JobOffer> getJobOffersListByFilters(int page, int pageSize, String categoryId, String modality);

    Integer getActiveJobOffersCount(String categoryId, String modality);

    List<JobOffer> getJobOffersListByEnterprise(int page, int pageSize, String term);

    Map<Long, List<Skill>> getJobOfferSkillsMapForEnterprise(Enterprise enterprise, int page, int pageSize);

    void closeJobOffer(JobOffer jobOffer);

    void cancelJobOffer(JobOffer jobOffer);

}
