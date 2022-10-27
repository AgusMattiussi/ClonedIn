package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.services.ContactService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void addContact(Enterprise enterprise, User user, JobOffer jobOffer) {
        contactDao.addContact(enterprise, user, jobOffer);
    }

    @Override
    public List<Enterprise> getEnterprisesForUser(User user) {
        return contactDao.getEnterprisesForUser(user);
    }

    @Override
    public List<User> getUsersForEnterprise(Enterprise enterprise) {
        return contactDao.getUsersForEnterprise(enterprise);
    }

    @Override
    public List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId) {
        return contactDao.getJobOffersWithStatusForUser(userId);
    }

    @Override
    public List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize, String status) {
        return contactDao.getJobOffersWithStatusUserData(enterpriseID, page, pageSize, status);
    }

    @Override
    public List<JobOfferStatusUserData> getAllJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize) {
        return contactDao.getAllJobOffersWithStatusUserData(enterpriseID, page, pageSize);
    }

    @Override
    public List<JobOfferStatusEnterpriseData> getJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize, String status) {
        return contactDao.getJobOffersWithStatusEnterpriseData(userID, page, pageSize, status);
    }

    @Override
    public List<JobOfferStatusEnterpriseData> getAllJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize) {
        return contactDao.getAllJobOffersWithStatusEnterpriseData(userID, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForUser(User user) {
        return contactDao.getContactsForUser(user);
    }

    @Override
    public List<Contact> getContactsForUser(User user, int page, int pageSize) {
        return contactDao.getContactsForUser(user, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise) {
        return contactDao.getContactsForEnterprise(enterprise);
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, int page, int pageSize) {
        return contactDao.getContactsForEnterprise(enterprise, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer) {
        return contactDao.getContactsForJobOffer(jobOffer);
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer, int page, int pageSize) {
        return contactDao.getContactsForJobOffer(jobOffer, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user) {
        return contactDao.getContactsForEnterpriseAndUser(enterprise, user);
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, int page, int pageSize) {
        return contactDao.getContactsForEnterpriseAndUser(enterprise, user, page, pageSize);
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer) {
        return contactDao.getContactsForEnterpriseAndJobOffer(enterprise, jobOffer);
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, int page, int pageSize) {
        return contactDao.getContactsForEnterpriseAndJobOffer(enterprise, jobOffer, page, pageSize);
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        return contactDao.alreadyContacted(userID, jobOfferID);
    }

    @Override
    public Optional<String> getStatus(User user, JobOffer jobOffer) {
        return contactDao.getStatus(user, jobOffer);
    }

    @Override
    public void acceptJobOffer(User user, JobOffer jobOffer) {
        contactDao.acceptJobOffer(user, jobOffer);
    }

    @Override
    public void rejectJobOffer(User user, JobOffer jobOffer) {
        contactDao.rejectJobOffer(user, jobOffer);
    }

    @Override
    public void cancelJobOffer(User user, JobOffer jobOffer) {
        contactDao.cancelJobOffer(user, jobOffer);
    }

    @Override
    public void cancelJobOfferForEveryone(JobOffer jobOffer) {
        contactDao.cancelJobOfferForEveryone(jobOffer);
    }

    @Override
    public void closeJobOffer(User user, JobOffer jobOffer) {
        contactDao.closeJobOffer(user, jobOffer);
    }

    @Override
    public void closeJobOfferForEveryone(long jobOfferID) {
        contactDao.closeJobOfferForEveryone(jobOfferID);
    }

    @Override
    public long getContactsCountForEnterprise(long enterpriseID) {
        return contactDao.getContactsCountForEnterprise(enterpriseID);
    }
    @Override
    public long getContactsCountForUser(long userID) {
        return contactDao.getContactsCountForUser(userID);
    }

    @Override
    public Map<Long, List<Skill>> getJobOfferSkillsMapForUser(List<JobOfferStatusEnterpriseData> jobOfferList) {
        Map<Long, List<Skill>> jobOfferSkillMap = new HashMap<>();
        for (JobOffer jobOffer : jobOfferList) {
            jobOfferSkillMap.put(jobOffer.getId(), jobOfferSkillDao.getSkillsForJobOffer(jobOffer.getId()));
        }
        return jobOfferSkillMap;
    }
}
