package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;

import javax.jws.soap.SOAPBinding;
import java.util.List;

public interface ContactDao {

    void addContact(Enterprise enterprise, User user, JobOffer jobOffer);

    List<Enterprise> getEnterprisesForUser(User user);

    List<User> getUsersForEnterprise(Enterprise enterprise);

    List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId);

    List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize, String status);

    List<JobOfferStatusUserData> getAllJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize);

    List<JobOfferStatusEnterpriseData> getJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize, String status);

    List<JobOfferStatusEnterpriseData> getAllJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize);

    List<Contact> getContactsForUser(User user);

    List<Contact> getContactsForUser(User user, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, int page, int pageSize);

    List<Contact> getContactsForJobOffer(JobOffer jobOffer);

    List<Contact> getContactsForJobOffer(JobOffer jobOffer, int page, int pageSize);

    List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user);

    List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, int page, int pageSize);

    List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer);

    List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, int page, int pageSize);

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

}
