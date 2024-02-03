package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.enums.ContactSorting;
import ar.edu.itba.paw.models.exceptions.*;
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
    public Optional<Contact> findByPrimaryKey(long userID, long jobOfferID) {
        return contactDao.findByPrimaryKey(userID, jobOfferID);
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
    public List<Enterprise> getEnterprisesForUser(User user, FilledBy filledBy) {
        return contactDao.getEnterprisesForUser(user, filledBy);
    }

    @Override
    public List<User> getUsersForEnterprise(Enterprise enterprise, FilledBy filledBy) {
        return contactDao.getUsersForEnterprise(enterprise, filledBy);
    }


    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy) {
        return contactDao.getContactsForUser(user, filledBy);
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, ContactSorting sortBy, int page, int pageSize) {
        return contactDao.getContactsForUser(user, filledBy, sortBy, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, String status) {
        return contactDao.getContactsForUser(user, filledBy, status);
    }

    @Override
    public PaginatedResource<Contact> getContactsForUser(long userId, FilledBy filledBy, JobOfferStatus status, ContactSorting sortBy,
                                                         int page, int pageSize) {
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} not found - getContactsForUser method", userId);
            return new UserNotFoundException(userId);
        });

        List<Contact> contacts = contactDao.getContactsForUser(user, filledBy, status, sortBy, page-1, pageSize);

        long applicationsCount = this.getContactsCountForUser(user, filledBy, status);
        long maxPages = applicationsCount/pageSize + 1;

        return new PaginatedResource<>(contacts, page, maxPages);
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy) {
        return contactDao.getContactsForEnterprise(enterprise, filledBy);
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, ContactSorting sortBy, int page, int pageSize) {
        return contactDao.getContactsForEnterprise(enterprise, filledBy, sortBy, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status) {
        return contactDao.getContactsForEnterprise(enterprise, filledBy, status);
    }

    @Override
    @Transactional
    public PaginatedResource<Contact> getContactsForEnterprise(long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                                               JobOfferStatus status, ContactSorting sortBy, int page, int pageSize) {
        String statusValue = status != null ? status.getStatus() : null;

        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> {
                    LOGGER.error("Enterprise with id {} not found - getContactsForEnterprise method", enterpriseId);
                    return new EnterpriseNotFoundException(enterpriseId);
                });
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

        List<Contact> contacts = contactDao.getContactsForEnterprise(enterprise, jobOffer, user, filledBy, statusValue,
                sortBy, page-1, pageSize);

        long contactCount = this.getContactsCountForEnterprise(enterprise, jobOffer, user, filledBy, status);
        long maxPages = contactCount / pageSize + 1;

        return new PaginatedResource<>(contacts, page, maxPages);
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy) {
        return contactDao.getContactsForJobOffer(jobOffer, filledBy);
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy, int page, int pageSize) {
        return contactDao.getContactsForJobOffer(jobOffer, filledBy, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, FilledBy filledBy) {
        return contactDao.getContactsForEnterpriseAndUser(enterprise, user, filledBy);
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, FilledBy filledBy, int page, int pageSize) {
        return contactDao.getContactsForEnterpriseAndUser(enterprise, user, filledBy, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy) {
        return contactDao.getContactsForEnterpriseAndJobOffer(enterprise, jobOffer, filledBy);
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy, int page, int pageSize) {
        return contactDao.getContactsForEnterpriseAndJobOffer(enterprise, jobOffer, filledBy, page, pageSize);
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        return contactDao.alreadyContacted(userID, jobOfferID);
    }

    @Override
    public boolean alreadyContactedByEnterprise(long userID, long enterpriseID) {
        return contactDao.alreadyContactedByEnterprise(userID, enterpriseID);
    }

    @Override
    public Optional<String> getStatus(User user, JobOffer jobOffer) {
        return contactDao.getStatus(user, jobOffer);
    }

    @Override
    @Transactional
    public boolean acceptJobOffer(User user, JobOffer jobOffer) {
        if(contactDao.acceptJobOffer(user, jobOffer)){
            LOGGER.info("JobOffer with id {} accepted by user with id {} - acceptJobOffer", jobOffer.getId(), user.getId());

            emailService.sendAcceptApplicationEmail(user, jobOffer.getEnterprise().getName(),
                    jobOffer.getEnterprise().getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
            LOGGER.debug("Accept application email sent to user '{}' for enterprise '{}' and jobOffer '{}' - acceptJobOffer",
                    user, jobOffer.getEnterprise(), jobOffer);
            return true;
        }
        LOGGER.error("JobOffer with id {} could not be accepted by user with id {} - acceptJobOffer", jobOffer.getId(), user.getId());
        return false;
    }

    @Override
    @Transactional
    public boolean rejectJobOffer(User user, JobOffer jobOffer) {
        if(contactDao.rejectJobOffer(user, jobOffer)){
            LOGGER.info("JobOffer with id {} rejected by user with id {} - rejectJobOffer", jobOffer.getId(), user.getId());

            emailService.sendRejectApplicationEmail(user, jobOffer.getEnterprise().getName(),
                    jobOffer.getEnterprise().getEmail(), jobOffer.getPosition(), LocaleContextHolder.getLocale());
            LOGGER.debug("Reject application email sent to user '{}' for enterprise '{}' and jobOffer '{}' - rejectJobOffer",
                    user, jobOffer.getEnterprise(), jobOffer);

            return true;
        }
        LOGGER.error("JobOffer with id {} could not be rejected by user with id {} - rejectJobOffer", jobOffer.getId(), user.getId());
        return false;
    }

    @Override
    @Transactional
    public boolean cancelJobOffer(long userId, long jobOfferId) {
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
            throw new JobOfferStatusException(JobOfferStatus.CANCELLED, jobOfferId, userId);
        }

        emailService.sendCancelApplicationEmail(jobOffer.getEnterprise(), user, jobOffer.getPosition(), LocaleContextHolder.getLocale());
        LOGGER.debug("Cancel application email sent to enterprise '{}' for user '{}' and jobOffer '{}' - cancelJobOffer",
                jobOffer.getEnterprise(), user, jobOffer);

        return true;
    }

    @Override
    @Transactional
    public boolean cancelJobOfferForEveryone(JobOffer jobOffer) {
        if(!contactDao.cancelJobOfferForEveryone(jobOffer)){
            LOGGER.error("JobOffer with id {} could not be cancelled for everyone - cancelJobOfferForEveryone", jobOffer.getId());
            throw new JobOfferStatusException(JobOfferStatus.CANCELLED, jobOffer.getId());
        }
        LOGGER.info("JobOffer with id {} cancelled for everyone - cancelJobOfferForEveryone", jobOffer.getId());
        return true;
    }

    @Override
    public boolean closeJobOfferForUser(User user, JobOffer jobOffer) {
        if(!contactDao.closeJobOffer(user, jobOffer)){
            LOGGER.error("JobOffer with id {} could not be closed by user with id {} - closeJobOffer", jobOffer.getId(), user.getId());
            throw new JobOfferStatusException(JobOfferStatus.CLOSED, jobOffer.getId(), user.getId());
        }
        LOGGER.info("JobOffer with id {} closed by user with id {} - closeJobOffer", jobOffer.getId(), user.getId());
        return true;
    }

    @Override
    public boolean closeJobOfferForEveryone(JobOffer jobOffer) {
        if(!contactDao.closeJobOfferForEveryone(jobOffer)){
            LOGGER.error("JobOffer with id {} could not be closed for everyone - closeJobOfferForEveryone", jobOffer.getId());
            throw new JobOfferStatusException(JobOfferStatus.CLOSED, jobOffer.getId());
        }
        LOGGER.info("JobOffer with id {} closed for everyone - closeJobOfferForEveryone", jobOffer.getId());
        return true;
    }

    @Override
    public long getContactsCountForEnterprise(long enterpriseID) {
        return contactDao.getContactsCountForEnterprise(enterpriseID);
    }

    @Override
    public long getContactsCountForEnterprise(Enterprise enterprise) {
        return contactDao.getContactsCountForEnterprise(enterprise);
    }

    @Override
    public long getContactsCountForEnterprise(Enterprise enterprise, JobOffer jobOffer, User user, FilledBy filledBy, JobOfferStatus status) {
        String statusValue = status != null ? status.getStatus() : null;
        return contactDao.getContactsCountForEnterprise(enterprise, jobOffer, user, filledBy, statusValue);
    }

    @Override
    public long getContactsCountForUser(User user, FilledBy filledBy, JobOfferStatus status) {
        return contactDao.getContactsCountForUser(user, filledBy, status);
    }

    @Override
    public long getContactsCountForUser(User user) {
        return contactDao.getContactsCountForUser(user);
    }

    @Override
    @Transactional
    public void updateEnterpriseContactStatus(long userId, long jobOfferId, JobOfferStatus status){
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} not found - updateEnterpriseContactStatus", userId);
            return new UserNotFoundException(userId);
        });

        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("JobOffer with id {} not found - updateEnterpriseContactStatus", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        Contact contact = this.findByPrimaryKey(user.getId(), jobOffer.getId()).orElseThrow(() -> {
            LOGGER.error("Contact not found for user with id {} and jobOffer with id {} - updateEnterpriseContactStatus", user.getId(), jobOffer.getId());
            return new ContactNotFoundException(user.getId(), jobOffer.getId());
        });

       if (status == JobOfferStatus.PENDING) {
           LOGGER.error("Cannot update contact (JobOffer id: {}, User id:{}, Enterprise id: {}) status to PENDING, since it is the default status - updateEnterpriseContactStatus",
                   jobOfferId, userId, contact.getEnterprise().getId());
           throw new IllegalArgumentException("Cannot update contact status to PENDING");
       }

       if(status == JobOfferStatus.CLOSED){
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
               successful = this.acceptJobOffer(user, jobOffer);
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
               successful = this.rejectJobOffer(user, jobOffer);
                if(successful) {
                    LOGGER.info("JobOffer with id {} and user with id {} rejected by enterprise with id {} - updateEnterpriseContactStatus",
                            jobOfferId, userId, contact.getEnterprise().getId());
                }
               break;
           case CANCELLED:
               successful = this.cancelJobOffer(userId, jobOfferId);
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
    public void updateUserContactStatus(long userId, long jobOfferId, JobOfferStatus status){
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} not found - updateUserContactStatus", userId);
            return new UserNotFoundException(userId);
        });
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> {
            LOGGER.error("JobOffer with id {} not found - updateUserContactStatus", jobOfferId);
            return new JobOfferNotFoundException(jobOfferId);
        });

        Contact contact = this.findByPrimaryKey(user.getId(), jobOffer.getId()).orElseThrow(() -> {
            LOGGER.error("Contact not found for user with id {}, jobOffer with id {} and enterprise with id {} - updateUserContactStatus",
                    userId, jobOfferId, jobOffer.getEnterprise().getId());
            return new ContactNotFoundException(userId, jobOfferId);
        });

        JobOfferStatus currentStatus = contact.getStatusEnum();
        if(currentStatus != JobOfferStatus.PENDING) {
            LOGGER.error("Cannot update contact (JobOffer id: {}, User id:{}, Enterprise id: {}) status, since it has already been updated. Current status: {} - updateUserContactStatus",
                    jobOfferId, userId, jobOffer.getEnterprise().getId(), currentStatus);
            throw new JobOfferStatusException(status, jobOfferId, userId);
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
                successful = this.acceptJobOffer(user, jobOffer);
                if(successful) {
                    LOGGER.info("JobOffer with id {} and user with id {} accepted by user - updateUserContactStatus",
                            jobOfferId, userId);
                }
                break;
            case DECLINED:
                if(filledBy == FilledBy.USER) {
                    LOGGER.error("Cannot decline contact for job offer with id {} and user with id {} since it was filled by the user - updateUserContactStatus",
                            jobOfferId, userId);
                    throw new IllegalArgumentException("Cannot decline a contact that was filled by this user");
                }
                successful = this.rejectJobOffer(user, jobOffer);
                if(successful) {
                    LOGGER.info("JobOffer with id {} and user with id {} rejected by user - updateUserContactStatus",
                            jobOfferId, userId);
                }
                break;
            case CANCELLED:
                if(filledBy == FilledBy.ENTERPRISE){
                    LOGGER.error("Cannot cancel contact for job offer with id {} and user with id {} since it was filled by the enterprise - updateUserContactStatus",
                            jobOfferId, userId);
                    throw new IllegalArgumentException("Cannot cancel a contact that was filled by the enterprise");
                }
                successful = this.cancelJobOffer(userId, jobOfferId);
                if(successful) {
                    LOGGER.info("JobOffer with id {} cancelled by user with id {} - updateUserContactStatus", jobOfferId, userId);
                }
                break;
        }

        if(!successful) {
            LOGGER.error("Could not update contact (JobOffer id: {}, User id:{}, Enterprise id: {}) status to '{}' - updateUserContactStatus",
                    jobOfferId, userId, jobOffer.getEnterprise().getId(), status.getStatus());
            throw new JobOfferStatusException(status, jobOfferId, userId);
        }

    }

    @Override
    @Transactional
    public void updateContactStatus(long userId, long jobOfferId, JobOfferStatus status, Role updatedBy) {
        if (updatedBy == Role.ENTERPRISE)
            updateEnterpriseContactStatus(userId, jobOfferId, status);
        else
            updateUserContactStatus(userId, jobOfferId, status);
    }


}
