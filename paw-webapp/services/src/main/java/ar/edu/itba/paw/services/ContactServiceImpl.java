package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.services.ContactService;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.SortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactDao contactDao;
    private final JobOfferSkillDao jobOfferSkillDao;

    @Autowired
    public ContactServiceImpl(ContactDao contactDao, JobOfferSkillDao jobOfferSkillDao) {
        this.contactDao = contactDao;
        this.jobOfferSkillDao= jobOfferSkillDao;
    }

    @Override
    public Optional<Contact> findByPrimaryKey(long userID, long jobOfferID) {
        return contactDao.findByPrimaryKey(userID, jobOfferID);
    }

    @Override
    public Contact addContact(Enterprise enterprise, User user, JobOffer jobOffer, FilledBy filledBy) {
        return contactDao.addContact(enterprise, user, jobOffer, filledBy);
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
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, String status, SortBy sortBy, int page, int pageSize) {
        return contactDao.getContactsForUser(user, filledBy, status, sortBy, page, pageSize);
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
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status, SortBy sortBy, int page, int pageSize) {
        return contactDao.getContactsForEnterprise(enterprise, filledBy, status, sortBy, page, pageSize);
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
    public boolean cancelJobOffer(User user, JobOffer jobOffer) {
        return contactDao.cancelJobOffer(user, jobOffer);
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
    public long getContactsCountForUser(long userID, FilledBy filledBy, String status) {
        return contactDao.getContactsCountForUser(userID, filledBy, status);
    }

    @Override
    public long getContactsCountForUser(User user) {
        return contactDao.getContactsCountForUser(user);
    }

}
