package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.enums.ContactSorting;
import ar.edu.itba.paw.models.utils.PaginatedResource;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    Optional<Contact> getContact(long userID, long jobOfferID);

    Contact addContact(long userId, long jobOfferId, FilledBy filledBy);

    Contact addContact(long enterpriseId, long userId, long jobOfferId, FilledBy filledBy, String contactMessage);

    List<Enterprise> getEnterprisesForUser(User user, FilledBy filledBy);

    List<User> getUsersForEnterprise(Enterprise enterprise, FilledBy filledBy);

    List<Contact> getContactsForUser(User user, FilledBy filledBy);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, ContactSorting sortBy, int page, int pageSize);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, String status);

    PaginatedResource<Contact> getContactsForUser(long userId, FilledBy filledBy, JobOfferStatus status, ContactSorting sortBy, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, ContactSorting sortBy, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status);

    PaginatedResource<Contact> getContacts(Long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                                        JobOfferStatus status, ContactSorting sortBy, int page, int pageSize);

    List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy);

    List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy, int page, int pageSize);

    List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, FilledBy filledBy);

    List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, FilledBy filledBy, int page, int pageSize);

    List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy);

    List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy, int page, int pageSize);

    boolean alreadyContacted(long userID, long jobOfferID);

    boolean alreadyContactedByEnterprise(long userID, long enterpriseID);

    Optional<String> getStatus(User user, JobOffer jobOffer);

    boolean acceptJobOffer(User user, JobOffer jobOffer, Role updatedBy);

    boolean rejectJobOffer(User user, JobOffer jobOffer, Role updatedBy);

    boolean cancelJobOffer(long userId, long jobOfferId, Role updatedBy);

    boolean cancelJobOfferForEveryone(JobOffer jobOffer);

    boolean closeJobOfferForUser(User user, JobOffer jobOffer);

    boolean closeJobOfferForEveryone(JobOffer jobOffer);

    long getContactsCount(long enterpriseID);
    long getContactsCount(Enterprise enterprise);

    long getContactsCount(Enterprise enterprise, JobOffer jobOffer, User user, FilledBy filledBy, JobOfferStatus status);

    long getContactsCountForUser(User user, FilledBy filledBy, JobOfferStatus status);

    long getContactsCountForUser(User user);

    void updateEnterpriseContactStatus(long userId, long jobOfferId, JobOfferStatus status, Role updatedBy);

    void updateUserContactStatus(long userId, long jobOfferId, JobOfferStatus status, Role updatedBy);

    default void updateContactStatus(long userId, long jobOfferId, JobOfferStatus status, Role updatedBy) {
        if (updatedBy == Role.ENTERPRISE)
            updateEnterpriseContactStatus(userId, jobOfferId, status, updatedBy);
        else
            updateUserContactStatus(userId, jobOfferId, status, updatedBy);
    }

    default PaginatedResource<Contact> getContactsForRole(Role requesterRole, Long requesterId,  Long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                                        JobOfferStatus status, ContactSorting sortBy, int page, int pageSize){
        if(requesterRole == null)
            throw new IllegalArgumentException("Requester role cannot be null");
        if(requesterId == null)
            throw new IllegalArgumentException("Requester id cannot be null");

        if(requesterRole == Role.USER)
            return getContacts(enterpriseId, jobOfferId, requesterId, filledBy, status, sortBy, page, pageSize);
        else
            return getContacts(requesterId, jobOfferId, userId, filledBy, status, sortBy, page, pageSize);
    }

    Contact addContact(Role requesterRole, Long requesterId, Long jobOfferId, Long userId, String message);

    default Optional<Contact> getContact(String contactId) {
        final long[] ids = Contact.splitId(contactId);
        return getContact(ids[0], ids[1]);
    }

}
