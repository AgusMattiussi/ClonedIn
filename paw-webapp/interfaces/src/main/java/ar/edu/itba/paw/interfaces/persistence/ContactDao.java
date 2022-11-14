package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.SortBy;

import java.util.List;
import java.util.Optional;

public interface ContactDao {

    void addContact(Enterprise enterprise, User user, JobOffer jobOffer, FilledBy filledBy);

    List<Enterprise> getEnterprisesForUser(User user, FilledBy filledBy);

    List<User> getUsersForEnterprise(Enterprise enterprise, FilledBy filledBy);

    List<Contact> getContactsForUser(User user, FilledBy filledBy);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, SortBy sortBy, int page, int pageSize);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, String status);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, String status, SortBy sortBy, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, SortBy sortBy, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status, SortBy sortBy, int page, int pageSize);

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

    long getContactsCountForEnterprise(Enterprise enterprise);

    long getContactsCountForEnterprise(long enterpriseID);

    long getContactsCountForUser(User user);

    long getContactsCountForUser(long userID, FilledBy filledBy);

}
