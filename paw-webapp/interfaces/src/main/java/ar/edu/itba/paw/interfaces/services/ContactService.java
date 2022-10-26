package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Map;

public interface ContactService {

    void addContact(Enterprise enterprise, User user, JobOffer jobOffer);

    List<Enterprise> getEnterprisesForUser(User user);

    List<User> getUsersForEnterprise(Enterprise enterprise);

    List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId);

    List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize, String status);

    List<JobOfferStatusUserData> getAllJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize);

    List<JobOfferStatusEnterpriseData> getJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize, String status);

    List<JobOfferStatusEnterpriseData> getAllJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize);

    boolean alreadyContacted(long userID, long jobOfferID);

    String getStatus(long userID, long jobOfferID);

    void acceptJobOffer(long userID, long jobOfferID);

    void rejectJobOffer(long userID, long jobOfferID);

    void cancelJobOffer(long userID, long jobOfferID);

    void cancelJobOfferForEveryone(long jobOfferID);

    void closeJobOffer(long userID, long jobOfferID);

    void closeJobOfferForEveryone(long jobOfferID);

    long getContactsCountForEnterprise(long enterpriseID);

    long getContactsCountForUser(long userID);

    Map<Long, List<Skill>> getJobOfferSkillsMapForUser(List<JobOfferStatusEnterpriseData> jobOfferList);

}
