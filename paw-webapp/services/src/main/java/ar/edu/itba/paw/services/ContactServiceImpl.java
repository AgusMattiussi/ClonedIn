package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.services.ContactService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.JobOfferWithStatus;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
