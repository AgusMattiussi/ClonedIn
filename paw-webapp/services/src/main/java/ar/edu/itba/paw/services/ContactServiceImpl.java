package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.ContactStatus;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.enums.ContactSorting;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.models.ids.ContactId;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Autowired
    private ContactDao contactDao;
    @Autowired
    private EnterpriseService enterpriseService;
    @Autowired
    private UserService userService;
    @Autowired
    private JobOfferService jobOfferService;
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public Optional<Contact> getContact(long userID, long jobOfferID) {
        if(userID <= 0 || jobOfferID <= 0) {
            LOGGER.error("Invalid user id {} or jobOffer id {} - getContact method", userID, jobOfferID);
            throw new IllegalArgumentException("Invalid user id or jobOffer id");
        }

        return contactDao.findByPrimaryKey(userID, jobOfferID);
    }

    @Override
    @Transactional
    public Optional<Contact> getContact(long userID, long jobOfferID, boolean fetchYearsOfExperience) {
        if(!fetchYearsOfExperience) {
            return this.getContact(userID, jobOfferID);
        }

        if(userID <= 0 || jobOfferID <= 0) {
            LOGGER.error("Invalid user id {} or jobOffer id {} - getContact method", userID, jobOfferID);
            throw new IllegalArgumentException("Invalid user id or jobOffer id");
        }

        Optional<Contact> optContact = contactDao.findByPrimaryKey(userID, jobOfferID);
        optContact.ifPresent(contact -> {
            contact.getUser().getYearsOfExperience();
        });

        return optContact;
    }

    @Override
    @Transactional
    public Contact addContact(long userId, long jobOfferId, FilledBy filledBy) {

        if(filledBy == FilledBy.ANY) {
            LOGGER.error("FilledBy cannot be ANY for 'addContact' method. Should be ENTERPRISE or USER.");
            throw new IllegalArgumentException("FilledBy cannot be ANY");
        }

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} not found - addContact method", userId);
            return new UserNotFoundException(userId);
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("JobOffer with id {} not found - addContact method", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        long enterpriseId = jobOffer.getEnterpriseID();
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("Enterprise with id {} not found - addContact method", enterpriseId);
            return new EnterpriseNotFoundException(enterpriseId);
        });

        if(this.alreadyContacted(userId, jobOfferId)) {
            LOGGER.error("User with id {} already contacted for jobOffer with id {} - addContact method", userId, jobOfferId);
            throw new AlreadyAppliedException(userId, jobOfferId);
        }

        Contact contact = contactDao.addContact(enterprise, user, jobOffer, filledBy);
        LOGGER.info("Contact added for user with id {} and jobOffer with id {}", userId, jobOfferId);
        LOGGER.debug("Contact added for user '{}' and jobOffer '{}'", user, jobOffer);

        emailService.sendApplicationEmail(enterprise, user, jobOffer.getPosition(), LocaleContextHolder.getLocale());
        LOGGER.debug("Application email sent to enterprise '{}' for user '{}' and jobOffer '{}'", enterprise, user, jobOffer);

        return contact;
    }

    @Override
    @Transactional
    public Contact addContact(long enterpriseId, long userId, long jobOfferId, FilledBy filledBy, String contactMessage) {
        if(filledBy == FilledBy.ANY) {
            LOGGER.error("FilledBy cannot be ANY for 'addContact' method. Should be ENTERPRISE or USER.");
            throw new IllegalArgumentException("FilledBy cannot be ANY");
        }

        if(this.alreadyContacted(userId, jobOfferId)) {
            LOGGER.error("User with id {} already contacted for jobOffer with id {} - addContact method", userId, jobOfferId);
            throw new AlreadyContactedException(userId, jobOfferId);
        }

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("JobOffer with id {} not found - addContact method", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        if(jobOffer.getEnterpriseID() != enterpriseId) {
            LOGGER.error("Enterprise with id {} is not the owner of jobOffer with id {} - addContact method", enterpriseId, jobOfferId);
            throw new NotJobOfferOwnerException(enterpriseId, jobOfferId);
        }

        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
            LOGGER.error("Enterprise with id {} not found - addContact method", enterpriseId);
            return new EnterpriseNotFoundException(enterpriseId);
        });

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} not found - addContact method", userId);
            return new UserNotFoundException(userId);
        });

        Contact contact = contactDao.addContact(enterprise, user, jobOffer, FilledBy.ENTERPRISE);
        LOGGER.info("Contact added for user with id {} and jobOffer with id {}", userId, jobOfferId);
        LOGGER.debug("Contact added for user '{}' and jobOffer '{}'", user, jobOffer);

        emailService.sendContactEmail(user, enterprise, jobOffer, contactMessage, LocaleContextHolder.getLocale());
        LOGGER.debug("Contact email sent to user '{}' for enterprise '{}' and jobOffer '{}'", user, enterprise, jobOffer);

        return contact;
    }


    @Override
    @Transactional
    public PaginatedResource<Contact> getContacts(Long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                                  ContactStatus status, ContactSorting sortBy, int page, int pageSize) {
        String statusValue = status != null ? status.getStatus() : null;

        Enterprise enterprise = enterpriseId != null ? enterpriseService.findById(enterpriseId).orElseThrow(() -> {
                    LOGGER.error("Enterprise with id {} not found - getContactsForEnterprise method", enterpriseId);
                    return new EnterpriseNotFoundException(enterpriseId);
                }) : null;
        User user = userId != null ? userService.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("User with id {} not found - getContactsForEnterprise method", userId);
                    return new UserNotFoundException(userId);
                }) : null;
        JobOffer jobOffer = jobOfferId != null ?
                jobOfferService.findById(jobOfferId).orElseThrow(() -> {
                    LOGGER.error("JobOffer with id {} not found - getContactsForEnterprise method", jobOfferId);
                    return new JobOfferNotFoundException(jobOfferId);
                }) : null;


        List<Contact> contacts = contactDao.getContacts(enterprise, jobOffer, user, filledBy, statusValue,
                sortBy, page-1, pageSize);

        // Fetch user years of experience to avoid LazyInitializationException
        contacts.forEach(contact -> contact.getUser().getYearsOfExperience());

        long contactCount = this.getContactsCount(enterprise, jobOffer, user, filledBy, status);
        long maxPages = (long) Math.ceil((double) contactCount / pageSize);

        return new PaginatedResource<>(contacts, page, maxPages);
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy, int page, int pageSize) {
        return contactDao.getContactsForJobOffer(jobOffer, filledBy, page, pageSize);
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        return contactDao.alreadyContacted(userID, jobOfferID);
    }


    @Override
    @Transactional
    public boolean acceptJobOffer(User user, JobOffer jobOffer, Role updatedBy) {
        if(contactDao.acceptJobOffer(user, jobOffer)){
            LOGGER.debug("JobOffer with id {} accepted by user with id {} - acceptJobOffer", jobOffer.getId(), user.getId());

            if(updatedBy == Role.ENTERPRISE){
                emailService.sendAcceptApplicationEmail(user, jobOffer.getEnterprise().getName(),
                        user.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
            } else {
                emailService.sendAcceptJobOfferEmail(jobOffer.getEnterprise(), user.getName(), user.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
            }

            LOGGER.debug("Accept application email sent to user '{}' for enterprise '{}' and jobOffer '{}' - acceptJobOffer",
                    user, jobOffer.getEnterprise(), jobOffer);
            return true;
        }
        LOGGER.error("JobOffer with id {} could not be accepted by user with id {} - acceptJobOffer", jobOffer.getId(), user.getId());
        return false;
    }

    @Override
    @Transactional
    public boolean rejectJobOffer(User user, JobOffer jobOffer, Role updatedBy) {
        if(contactDao.rejectJobOffer(user, jobOffer)){
            LOGGER.debug("JobOffer with id {} rejected by user with id {} - rejectJobOffer", jobOffer.getId(), user.getId());

            if(updatedBy == Role.ENTERPRISE){
                emailService.sendRejectApplicationEmail(user, jobOffer.getEnterprise().getName(),
                        user.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
            } else {
                emailService.sendRejectJobOfferEmail(jobOffer.getEnterprise(), user.getName(), user.getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
            }

            LOGGER.debug("Reject application email sent to user '{}' for enterprise '{}' and jobOffer '{}' - rejectJobOffer",
                    user, jobOffer.getEnterprise(), jobOffer);

            return true;
        }
        LOGGER.error("JobOffer with id {} could not be rejected by user with id {} - rejectJobOffer", jobOffer.getId(), user.getId());
        return false;
    }

    @Override
    @Transactional
    public boolean cancelJobOffer(long userId, long jobOfferId, Role updatedBy) {
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} not found - cancelJobOffer", userId);
            return new UserNotFoundException(userId);
        });
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("JobOffer with id {} not found - cancelJobOffer method", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        if(!contactDao.cancelJobOffer(user, jobOffer)) {
            LOGGER.error("JobOffer with id {} could not be cancelled by user with id {} - cancelJobOffer", jobOffer.getId(), user.getId());
            throw new ContactStatusException(ContactStatus.CANCELLED, jobOfferId, userId);
        }
        if(updatedBy == Role.ENTERPRISE){
            emailService.sendCancelJobOfferEmail(user, jobOffer.getEnterprise().getName(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
        } else {
            emailService.sendCancelApplicationEmail(jobOffer.getEnterprise(), user, jobOffer.getPosition(), LocaleContextHolder.getLocale());
        }
        LOGGER.debug("Cancel application email sent to enterprise '{}' for user '{}' and jobOffer '{}' - cancelJobOffer",
                jobOffer.getEnterprise(), user, jobOffer);

        return true;
    }

    @Override
    @Transactional
    public boolean cancelJobOfferForEveryone(JobOffer jobOffer) {
        if(!contactDao.cancelJobOfferForEveryone(jobOffer)){
            LOGGER.error("JobOffer with id {} could not be cancelled for everyone - cancelJobOfferForEveryone", jobOffer.getId());
            throw new ContactStatusException(ContactStatus.CANCELLED, jobOffer.getId());
        }
        LOGGER.info("JobOffer with id {} cancelled for everyone - cancelJobOfferForEveryone", jobOffer.getId());
        return true;
    }

    @Override
    public boolean closeJobOfferForUser(User user, JobOffer jobOffer) {
        if(!contactDao.closeJobOffer(user, jobOffer)){
            LOGGER.error("JobOffer with id {} could not be closed by user with id {} - closeJobOffer", jobOffer.getId(), user.getId());
            throw new ContactStatusException(ContactStatus.CLOSED, jobOffer.getId(), user.getId());
        }
        LOGGER.debug("JobOffer with id {} closed by user with id {} - closeJobOffer", jobOffer.getId(), user.getId());
        return true;
    }

    @Override
    public boolean closeJobOfferForEveryone(JobOffer jobOffer) {
        if(!contactDao.closeJobOfferForEveryone(jobOffer)){
            LOGGER.error("JobOffer with id {} could not be closed for everyone - closeJobOfferForEveryone", jobOffer.getId());
            throw new ContactStatusException(ContactStatus.CLOSED, jobOffer.getId());
        }
        LOGGER.debug("JobOffer with id {} closed for everyone - closeJobOfferForEveryone", jobOffer.getId());
        return true;
    }

    @Override
    public long getContactsCount(Enterprise enterprise, JobOffer jobOffer, User user, FilledBy filledBy, ContactStatus status) {
        String statusValue = status != null ? status.getStatus() : null;
        return contactDao.getContactsCount(enterprise, jobOffer, user, filledBy, statusValue);
    }

    @Override
    @Transactional
    public void updateEnterpriseContactStatus(long userId, long jobOfferId, ContactStatus status, Role updatedBy){
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} not found - updateEnterpriseContactStatus", userId);
            return new UserNotFoundException(userId);
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("JobOffer with id {} not found - updateEnterpriseContactStatus", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        Contact contact = this.getContact(user.getId(), jobOffer.getId()).orElseThrow(() -> {
            LOGGER.error("Contact not found for user with id {} and jobOffer with id {} - updateEnterpriseContactStatus", user.getId(), jobOffer.getId());
            return new ContactNotFoundException(user.getId(), jobOffer.getId());
        });

       if (status == ContactStatus.PENDING) {
           LOGGER.error("Cannot update contact (JobOffer id: {}, User id:{}, Enterprise id: {}) status to PENDING, since it is the default status - updateEnterpriseContactStatus",
                   jobOfferId, userId, contact.getEnterprise().getId());
           throw new IllegalArgumentException("Cannot update contact status to PENDING");
       }

       if(status == ContactStatus.CLOSED){
           LOGGER.error("Cannot update single contact (JobOffer id: {}, User id:{}, Enterprise id: {}) status to CLOSED. Job offer must be closed for everyone for this status change.",
                   jobOfferId, userId, contact.getEnterprise().getId());
           throw new IllegalArgumentException("Cannot update single contact status to CLOSED. Job offer must be closed for everyone for this status change.");
       }

       boolean successful = false;
       switch (status) {
           case ACCEPTED:
               if(contact.getFilledByEnum() == FilledBy.ENTERPRISE) {
                   LOGGER.error("Cannot accept contact for job offer with id {} and user with id {} since it was filled by the enterprise (id {}) - updateEnterpriseContactStatus",
                           jobOfferId, userId, contact.getEnterprise().getId());
                   throw new IllegalArgumentException("Cannot accept a contact that was filled by this enterprise");
               }
               successful = this.acceptJobOffer(user, jobOffer, updatedBy);
               if(successful) {
                   LOGGER.info("JobOffer with id {} and user with id {} accepted by enterprise with id {} - updateEnterpriseContactStatus",
                           jobOfferId, userId, contact.getEnterprise().getId());
               }
               break;
           case DECLINED:
               if(contact.getFilledByEnum() == FilledBy.ENTERPRISE) {
                   LOGGER.error("Cannot decline contact for job offer with id {} and user with id {} since it was filled by the enterprise (id {}) - updateEnterpriseContactStatus",
                           jobOfferId, userId, contact.getEnterprise().getId());
                   throw new IllegalArgumentException("Cannot decline a contact that was filled by this enterprise");
               }
               successful = this.rejectJobOffer(user, jobOffer, updatedBy);
                if(successful) {
                    LOGGER.info("JobOffer with id {} and user with id {} rejected by enterprise with id {} - updateEnterpriseContactStatus",
                            jobOfferId, userId, contact.getEnterprise().getId());
                }
               break;
           case CANCELLED:
               successful = this.cancelJobOffer(userId, jobOfferId, updatedBy);
                if(successful) {
                    LOGGER.info("JobOffer with id {} cancelled by enterprise with id {} - updateEnterpriseContactStatus", jobOfferId, contact.getEnterprise().getId());
                }
                break;
       }

       if(!successful) {
              LOGGER.error("Could not update contact (JobOffer id: {}, User id:{}, Enterprise id: {}) status to '{}' - updateEnterpriseContactStatus",
                      jobOfferId, userId, contact.getEnterprise().getId(), status.getStatus());
           throw new IllegalStateException(String.format("Could not update contact status to '%s'", status.getStatus()));
       }
    }

    @Override
    @Transactional
    public void updateUserContactStatus(long userId, long jobOfferId, ContactStatus status, Role updatedBy) {
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} not found - updateUserContactStatus", userId);
            return new UserNotFoundException(userId);
        });
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("JobOffer with id {} not found - updateUserContactStatus", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        Contact contact = this.getContact(user.getId(), jobOffer.getId()).orElseThrow(() -> {
            LOGGER.error("Contact not found for user with id {}, jobOffer with id {} and enterprise with id {} - updateUserContactStatus",
                    userId, jobOfferId, jobOffer.getEnterprise().getId());
            return new ContactNotFoundException(userId, jobOfferId);
        });

        ContactStatus currentStatus = contact.getStatusEnum();
        if(currentStatus != ContactStatus.PENDING) {
            LOGGER.error("Cannot update contact (JobOffer id: {}, User id:{}, Enterprise id: {}) status, since it has already been updated. Current status: {} - updateUserContactStatus",
                    jobOfferId, userId, jobOffer.getEnterprise().getId(), currentStatus);
            throw new ContactStatusException(status, jobOfferId, userId);
        }

        boolean successful = false;
        FilledBy filledBy = contact.getFilledByEnum();
        switch (status) {
            case ACCEPTED:
                if(filledBy == FilledBy.USER) {
                    LOGGER.error("Cannot accept contact for job offer with id {} and user with id {} since it was filled by the user - updateUserContactStatus",
                            jobOfferId, userId);
                    throw new IllegalArgumentException("Cannot accept a contact that was filled by this user");
                }
                successful = this.acceptJobOffer(user, jobOffer, updatedBy);
                if(successful) {
                    LOGGER.debug("JobOffer with id {} and user with id {} accepted by user - updateUserContactStatus",
                            jobOfferId, userId);
                }
                break;
            case DECLINED:
                if(filledBy == FilledBy.USER) {
                    LOGGER.error("Cannot decline contact for job offer with id {} and user with id {} since it was filled by the user - updateUserContactStatus",
                            jobOfferId, userId);
                    throw new IllegalArgumentException("Cannot decline a contact that was filled by this user");
                }
                successful = this.rejectJobOffer(user, jobOffer, updatedBy);
                if(successful) {
                    LOGGER.debug("JobOffer with id {} and user with id {} rejected by user - updateUserContactStatus",
                            jobOfferId, userId);
                }
                break;
            case CANCELLED:
                if(filledBy == FilledBy.ENTERPRISE){
                    LOGGER.error("Cannot cancel contact for job offer with id {} and user with id {} since it was filled by the enterprise - updateUserContactStatus",
                            jobOfferId, userId);
                    throw new IllegalArgumentException("Cannot cancel a contact that was filled by the enterprise");
                }
                successful = this.cancelJobOffer(userId, jobOfferId, updatedBy);
                if(successful) {
                    LOGGER.info("JobOffer with id {} cancelled by user with id {} - updateUserContactStatus", jobOfferId, userId);
                }
                break;
        }

        if(!successful) {
            LOGGER.error("Could not update contact (JobOffer id: {}, User id:{}, Enterprise id: {}) status to '{}' - updateUserContactStatus",
                    jobOfferId, userId, jobOffer.getEnterprise().getId(), status.getStatus());
            throw new ContactStatusException(status, jobOfferId, userId);
        }

    }

    @Override
    @Transactional
    public void updateContactStatus(Role requesterRole, long userId, long jobOfferId, ContactStatus status) {
        if (requesterRole == Role.ENTERPRISE)
            updateEnterpriseContactStatus(userId, jobOfferId, status, requesterRole);
        else
            updateUserContactStatus(userId, jobOfferId, status, requesterRole);
    }

    @Override
    @Transactional
    public void updateContactStatus(Role requesterRole, String contactId, ContactStatus status) {
        long[] ids = ContactId.splitId(contactId);
        updateContactStatus(requesterRole, ids[0], ids[1], status);
    }

    @Override
    @Transactional
    public PaginatedResource<Contact> getContactsForRole(Role requesterRole, Long requesterId, Long enterpriseId,
                                                         Long jobOfferId, Long userId, FilledBy filledBy, ContactStatus status,
                                                         ContactSorting sortBy, int page, int pageSize) {
        if(requesterRole == null)
            throw new IllegalArgumentException("Requester role cannot be null");
        if(requesterId == null)
            throw new IllegalArgumentException("Requester id cannot be null");

        if(requesterRole == Role.USER)
            return getContacts(enterpriseId, jobOfferId, requesterId, filledBy, status, sortBy, page, pageSize);
        else
            return getContacts(requesterId, jobOfferId, userId, filledBy, status, sortBy, page, pageSize);
    }

    @Override
    @Transactional
    public Contact addContact(Role requesterRole, Long requesterId, Long jobOfferId, Long userId, String message) {
        if(requesterRole == null)
            throw new IllegalArgumentException("Requester role cannot be null");
        if(requesterId == null)
            throw new IllegalArgumentException("Requester id cannot be null");

        if(requesterRole == Role.USER){
            if(userId != null && !userId.equals(requesterId))
                throw new IllegalArgumentException("User id must be null or equal to requester id");
            if(jobOfferId == null)
                throw new IllegalArgumentException("Job offer id cannot be null");
            return addContact(requesterId, jobOfferId, FilledBy.USER);
        } else {
            if(userId == null)
                throw new IllegalArgumentException("User id cannot be null");
            if(jobOfferId == null)
                throw new IllegalArgumentException("Job offer id cannot be null");
            return addContact(requesterId, userId, jobOfferId, FilledBy.ENTERPRISE, message);
        }
    }

    @Override
    @Transactional
    public Optional<Contact> getContact(String contactId, Role requesterRole) {
        final long[] ids = ContactId.splitId(contactId);
        return getContact(ids[0], ids[1], requesterRole == Role.ENTERPRISE);
    }


}
