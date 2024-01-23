package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import ar.edu.itba.paw.models.enums.Role;
import ar.edu.itba.paw.models.enums.SortBy;
import ar.edu.itba.paw.models.exceptions.*;
import ar.edu.itba.paw.models.utils.PaginatedResource;
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
    public Optional<Contact> findByPrimaryKey(long userID, long jobOfferID) {
        return contactDao.findByPrimaryKey(userID, jobOfferID);
    }

    @Override
    @Transactional
    public Contact addContact(long userId, long jobOfferId, FilledBy filledBy) {

        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));
        long enterpriseId = jobOffer.getEnterpriseID();
        Enterprise enterprise = enterpriseService.findById(enterpriseId).orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));

        if(this.alreadyContacted(userId, jobOfferId))
            throw new AlreadyAppliedException(userId, jobOfferId);

        Contact contact = contactDao.addContact(enterprise, user, jobOffer, filledBy);

        emailService.sendApplicationEmail(enterprise, user, jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return contact;
    }

    @Override
    @Transactional
    public Contact addContact(long enterpriseId, long userId, long jobOfferId, FilledBy filledBy, String contactMessage) {
        Enterprise enterprise = enterpriseService.findById(enterpriseId)
                .orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));

        if(this.alreadyContacted(userId, jobOfferId))
            throw new AlreadyContactedException(userId, jobOfferId);

        JobOffer jobOffer = jobOfferService.findById(jobOfferId)
                .orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));

        if(jobOffer.getEnterpriseID() != enterpriseId)
            throw new NotJobOfferOwnerException(enterpriseId, jobOfferId);

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Contact contact = contactDao.addContact(enterprise, user, jobOffer, FilledBy.ENTERPRISE);

        emailService.sendContactEmail(user, enterprise, jobOffer, contactMessage, LocaleContextHolder.getLocale());

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
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, SortBy sortBy, int page, int pageSize) {
        return contactDao.getContactsForUser(user, filledBy, sortBy, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, String status) {
        return contactDao.getContactsForUser(user, filledBy, status);
    }

    @Override
    public PaginatedResource<Contact> getContactsForUser(long userId, FilledBy filledBy, JobOfferStatus status, SortBy sortBy,
                                                         int page, int pageSize) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

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
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, SortBy sortBy, int page, int pageSize) {
        return contactDao.getContactsForEnterprise(enterprise, filledBy, sortBy, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status) {
        return contactDao.getContactsForEnterprise(enterprise, filledBy, status);
    }

    @Override
    @Transactional
    public PaginatedResource<Contact> getContactsForEnterprise(long enterpriseId, Long jobOfferId, Long userId, FilledBy filledBy,
                                           JobOfferStatus status, SortBy sortBy, int page, int pageSize) {
        String statusValue = status != null ? status.getStatus() : null;

        Enterprise enterprise = enterpriseService.findById(enterpriseId)
                .orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));
        User user = userId != null ?
                userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)) : null;
        JobOffer jobOffer = jobOfferId != null ?
                jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId)) : null;

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
    public boolean acceptJobOffer(User user, JobOffer jobOffer) {
        return contactDao.acceptJobOffer(user, jobOffer);
    }

    @Override
    public boolean rejectJobOffer(User user, JobOffer jobOffer) {
        return contactDao.rejectJobOffer(user, jobOffer);
    }

    @Override
    @Transactional
    public boolean cancelJobOffer(long userId, long jobOfferId) {

        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));

        if(!contactDao.cancelJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.CANCELLED, jobOfferId, userId);

        emailService.sendCancelApplicationEmail(jobOffer.getEnterprise(), user, jobOffer.getPosition(), LocaleContextHolder.getLocale());

        return true;
    }

    @Override
    public boolean cancelJobOfferForEveryone(JobOffer jobOffer) {
        return contactDao.cancelJobOfferForEveryone(jobOffer);
    }

    @Override
    public boolean closeJobOffer(User user, JobOffer jobOffer) {
        return contactDao.closeJobOffer(user, jobOffer);
    }

    @Override
    public boolean closeJobOfferForEveryone(JobOffer jobOffer) {
        return contactDao.closeJobOfferForEveryone(jobOffer);
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
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId)
                .orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));
        Contact contact = this.findByPrimaryKey(user.getId(), jobOffer.getId())
                .orElseThrow(() -> new ContactNotFoundException(user.getId(), jobOffer.getId()));


       if (status == JobOfferStatus.PENDING)
           throw new IllegalArgumentException("Cannot update contact status to PENDING");

       boolean successful = false;
       switch (status) {
           case ACCEPTED:
               if(contact.getFilledByEnum() == FilledBy.ENTERPRISE)
                   throw new IllegalStateException("Cannot accept a contact that was filled by this enterprise");
               successful = this.acceptJobOffer(user, jobOffer);
               break;
           case DECLINED:
               if(contact.getFilledByEnum() == FilledBy.ENTERPRISE)
                   throw new IllegalStateException("Cannot decline a contact that was filled by this enterprise");
               successful = this.rejectJobOffer(user, jobOffer);
               break;
           case CANCELLED:
               successful = this.cancelJobOffer(userId, jobOfferId);
               break;
           case CLOSED:
               successful = this.closeJobOffer(user, jobOffer);
               break;
       }

       if(!successful)
           throw new IllegalStateException(String.format("Could not update contact status to '%s'", status.getStatus()));
    }

    @Override
    @Transactional
    public void updateUserContactStatus(long userId, long jobOfferId, JobOfferStatus status){
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        JobOffer jobOffer = jobOfferService.findById(jobOfferId).orElseThrow(() -> new JobOfferNotFoundException(jobOfferId));

        JobOfferStatus currentStatus = contactDao.getStatus(user, jobOffer)
                .map(JobOfferStatus::fromString)
                .orElseThrow(() -> new ContactNotFoundException(userId, jobOfferId));

        if(currentStatus != JobOfferStatus.PENDING)
            throw new JobOfferStatusException(status, jobOfferId, userId);

        if(status != JobOfferStatus.ACCEPTED && status != JobOfferStatus.DECLINED)
            throw new JobOfferStatusException(status, jobOfferId, userId);

        if (status == JobOfferStatus.ACCEPTED && !this.acceptJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.ACCEPTED, jobOfferId, userId);
        else if (status == JobOfferStatus.DECLINED && !this.rejectJobOffer(user, jobOffer))
            throw new JobOfferStatusException(JobOfferStatus.DECLINED, jobOfferId, userId);
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
