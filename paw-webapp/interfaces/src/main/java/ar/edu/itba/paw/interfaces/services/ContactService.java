package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import ar.edu.itba.paw.models.enums.SortBy;
import ar.edu.itba.paw.models.utils.PaginatedResource;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    Optional<Contact> findByPrimaryKey(long userID, long jobOfferID);

    Contact addContact(Enterprise enterprise, User user, JobOffer jobOffer, FilledBy filledBy);

    Contact addContact(long enterpriseId, long userId, long jobOfferId, FilledBy filledBy, String contactMessage);

    List<Enterprise> getEnterprisesForUser(User user, FilledBy filledBy);

    List<User> getUsersForEnterprise(Enterprise enterprise, FilledBy filledBy);

    List<Contact> getContactsForUser(User user, FilledBy filledBy);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, SortBy sortBy, int page, int pageSize);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, String status);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, String status, SortBy sortBy, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, SortBy sortBy, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status);

    PaginatedResource<Contact> getContactsForEnterprise(long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                                        JobOfferStatus status, SortBy sortBy, int page, int pageSize);

    List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy);

    List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy, int page, int pageSize);

    List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, FilledBy filledBy);

    List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, FilledBy filledBy, int page, int pageSize);

    List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy);

    List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy, int page, int pageSize);

    boolean alreadyContacted(long userID, long jobOfferID);

    boolean alreadyContactedByEnterprise(long userID, long enterpriseID);

    Optional<String> getStatus(User user, JobOffer jobOffer);

    boolean acceptJobOffer(User user, JobOffer jobOffer);

    boolean rejectJobOffer(User user, JobOffer jobOffer);

    boolean cancelJobOffer(User user, JobOffer jobOffer);

    boolean cancelJobOfferForEveryone(JobOffer jobOffer);

    boolean closeJobOffer(User user, JobOffer jobOffer);

    boolean closeJobOfferForEveryone(JobOffer jobOffer);

    long getContactsCountForEnterprise(long enterpriseID);
    long getContactsCountForEnterprise(Enterprise enterprise);

    long getContactsCountForEnterprise(Enterprise enterprise, JobOffer jobOffer, User user, FilledBy filledBy, JobOfferStatus status);

    long getContactsCountForUser(long userID, FilledBy filledBy, String status);

    long getContactsCountForUser(User user);

}
