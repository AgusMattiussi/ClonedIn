package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.JobOfferWithStatus;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Skill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface JobOfferService {

    JobOffer create(long enterpriseID, long categoryID, String position, String description, BigDecimal salary, String modality);

    Optional<JobOffer> findById(long id);

    List<JobOffer> findByEnterpriseId(long enterpriseID, int page, int pageSize);

    Optional<Integer> getJobOffersCountForEnterprise(long enterpriseID);

    Map<Long, List<Skill>> getJobOfferSkillsMapForEnterprise(long enterpriseID, int page, int pageSize);

    void closeJobOffer(long jobOfferID);

}
