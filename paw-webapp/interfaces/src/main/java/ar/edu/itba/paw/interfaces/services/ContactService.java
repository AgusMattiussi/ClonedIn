package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.ContactStatus;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.enums.ContactSorting;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import java.util.List;
import java.util.Optional;

// TODO: delete unused functions (and implementations)
public interface ContactService {

    Optional<Contact> getContact(long userID, long jobOfferID);

    Optional<Contact> getContact(long userID, long jobOfferID, boolean fetchYearsOfExperience);

    Contact addContact(long userId, long jobOfferId, FilledBy filledBy);

    Contact addContact(long enterpriseId, long userId, long jobOfferId, FilledBy filledBy, String contactMessage);

    List<Enterprise> getEnterprisesForUser(User user, FilledBy filledBy);

    List<User> getUsersForEnterprise(Enterprise enterprise, FilledBy filledBy);

    List<Contact> getContactsForUser(User user, FilledBy filledBy);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, ContactSorting sortBy, int page, int pageSize);

    List<Contact> getContactsForUser(User user, FilledBy filledBy, String status);

    PaginatedResource<Contact> getContactsForUser(long userId, FilledBy filledBy, ContactStatus status, ContactSorting sortBy, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, ContactSorting sortBy, int page, int pageSize);

    List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status);

    PaginatedResource<Contact> getContacts(Long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                           ContactStatus status, ContactSorting sortBy, int page, int pageSize);

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

    long getContactsCount(Enterprise enterprise, JobOffer jobOffer, User user, FilledBy filledBy, ContactStatus status);

    long getContactsCountForUser(User user, FilledBy filledBy, ContactStatus status);

    long getContactsCountForUser(User user);

    void updateEnterpriseContactStatus(long userId, long jobOfferId, ContactStatus status, Role updatedBy);

    void updateUserContactStatus(long userId, long jobOfferId, ContactStatus status, Role updatedBy);

    void updateContactStatus(Role requesterRole, long userId, long jobOfferId, ContactStatus status);

    void updateContactStatus(Role requesterRole, String contactId, ContactStatus status);

    default PaginatedResource<Contact> getContactsForRole(Role requesterRole, Long requesterId, Long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                                          ContactStatus status, ContactSorting sortBy, int page, int pageSize){
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

    default Optional<Contact> getContact(String contactId, Role requesterRole) {
        final long[] ids = Contact.splitId(contactId);
        return getContact(ids[0], ids[1], requesterRole == Role.ENTERPRISE);
    }
}
