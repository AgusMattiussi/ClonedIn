package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.ContactStatus;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.enums.ContactSorting;
import ar.edu.itba.paw.models.ids.ContactId;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import java.util.List;
import java.util.Optional;

public interface ContactService {

    Optional<Contact> getContact(long userID, long jobOfferID);

    Optional<Contact> getContact(long userID, long jobOfferID, boolean fetchYearsOfExperience);

    Contact addContact(long userId, long jobOfferId, FilledBy filledBy);

    Contact addContact(long enterpriseId, long userId, long jobOfferId, FilledBy filledBy, String contactMessage);

    Contact addContact(Role requesterRole, Long requesterId, Long jobOfferId, Long userId, String message);

    PaginatedResource<Contact> getContacts(Long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                           ContactStatus status, ContactSorting sortBy, int page, int pageSize);

    List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy, int page, int pageSize);


    boolean alreadyContacted(long userID, long jobOfferID);

    boolean acceptJobOffer(User user, JobOffer jobOffer, Role updatedBy);

    boolean rejectJobOffer(User user, JobOffer jobOffer, Role updatedBy);

    boolean cancelJobOffer(long userId, long jobOfferId, Role updatedBy);

    boolean cancelJobOfferForEveryone(JobOffer jobOffer);

    boolean closeJobOfferForUser(User user, JobOffer jobOffer);

    boolean closeJobOfferForEveryone(JobOffer jobOffer);

    long getContactsCount(Enterprise enterprise, JobOffer jobOffer, User user, FilledBy filledBy, ContactStatus status);

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

    default Optional<Contact> getContact(String contactId, Role requesterRole) {
        final long[] ids = ContactId.splitId(contactId);
        return getContact(ids[0], ids[1], requesterRole == Role.ENTERPRISE);
    }
}
