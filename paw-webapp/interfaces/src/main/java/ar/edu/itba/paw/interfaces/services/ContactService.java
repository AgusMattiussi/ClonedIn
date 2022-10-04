package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Map;

public interface ContactService {

    void addContact(long enterpriseID, long userID, long jobOfferID);

    List<Enterprise> getEnterprisesForUser(long userID);

    List<User> getUsersForEnterprise(long enterpriseID);

    List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId);

    List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize, String status);

    List<JobOfferStatusEnterpriseData> getJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize, String status);

    boolean alreadyContacted(long userID, long jobOfferID);

    String getStatus(long userID, long jobOfferID);

    void acceptJobOffer(long userID, long jobOfferID);

    void rejectJobOffer(long userID, long jobOfferID);

    void cancelJobOffer(long userID, long jobOfferID);

    void closeJobOffer(long userID, long jobOfferID);

    long getContactsCountForEnterprise(long enterpriseID);

    long getContactsCountForUser(long userID);

    Map<Long, List<Skill>> getJobOfferSkillsMapForUser(List<JobOfferStatusEnterpriseData> jobOfferList);

}
