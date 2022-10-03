package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.services.ContactService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class ContactServiceImpl implements ContactService {

    private final ContactDao contactDao;

    @Autowired
    public ContactServiceImpl(ContactDao contactDao) {
        this.contactDao = contactDao;
    }

    @Override
    public void addContact(long enterpriseID, long userID, long jobOfferID) {
        contactDao.addContact(enterpriseID, userID, jobOfferID);
    }

    @Override
    public List<Enterprise> getEnterprisesForUser(long userID) {
        return contactDao.getEnterprisesForUser(userID);
    }

    @Override
    public List<User> getUsersForEnterprise(long enterpriseID) {
        return contactDao.getUsersForEnterprise(enterpriseID);
    }

    @Override
    public List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId) {
        return contactDao.getJobOffersWithStatusForUser(userId);
    }

    @Override
    public List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize) {
        return contactDao.getJobOffersWithStatusUserData(enterpriseID, page, pageSize);
    }

    @Override
    public List<JobOfferStatusEnterpriseData> getJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize, String status) {
        return contactDao.getJobOffersWithStatusEnterpriseData(userID, page, pageSize, status);
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        return contactDao.alreadyContacted(userID, jobOfferID);
    }

    @Override
    public String getStatus(long userID, long jobOfferID) {
        return contactDao.getStatus(userID, jobOfferID);
    }

    @Override
    public void acceptJobOffer(long userID, long jobOfferID) {
        contactDao.acceptJobOffer(userID, jobOfferID);
    }

    @Override
    public void rejectJobOffer(long userID, long jobOfferID) {
        contactDao.rejectJobOffer(userID, jobOfferID);
    }

    @Override
    public void cancelJobOffer(long userID, long jobOfferID) {
        contactDao.cancelJobOffer(userID, jobOfferID);
    }

    @Override
    public void closeJobOffer(long userID, long jobOfferID) {
        contactDao.closeJobOffer(userID, jobOfferID);
    }

    @Override
    public long getContactsCountForEnterprise(long enterpriseID) {
        return contactDao.getContactsCountForEnterprise(enterpriseID);
    }
    @Override
    public long getContactsCountForUser(long userID) {
        return contactDao.getContactsCountForUser(userID);

    }
}
