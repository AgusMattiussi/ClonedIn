package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ContactHibernateDao implements ContactDao {

    @Autowired
    private EntityManager em;

    @Override
    public void addContact(Enterprise enterprise, User user, JobOffer jobOffer) {
        Contact contact = new Contact(user, enterprise, jobOffer);
        em.persist(contact);
    }

    @Override
    public List<Enterprise> getEnterprisesForUser(User user) {
        TypedQuery<Enterprise> query = em.createQuery("SELECT c.enterprise FROM Contact c WHERE c.user = :user", Enterprise.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public List<User> getUsersForEnterprise(long enterpriseID) {
        return null;
    }

    @Override
    public List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId) {
        return null;
    }

    @Override
    public List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize, String status) {
        return null;
    }

    @Override
    public List<JobOfferStatusUserData> getAllJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize) {
        return null;
    }

    @Override
    public List<JobOfferStatusEnterpriseData> getJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize, String status) {
        return null;
    }

    @Override
    public List<JobOfferStatusEnterpriseData> getAllJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize) {
        return null;
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        return false;
    }

    @Override
    public String getStatus(long userID, long jobOfferID) {
        return null;
    }

    @Override
    public void acceptJobOffer(long userID, long jobOfferID) {

    }

    @Override
    public void rejectJobOffer(long userID, long jobOfferID) {

    }

    @Override
    public void cancelJobOffer(long userID, long jobOfferID) {

    }

    @Override
    public void cancelJobOfferForEveryone(long jobOfferID) {

    }

    @Override
    public void closeJobOffer(long userID, long jobOfferID) {

    }

    @Override
    public void closeJobOfferForEveryone(long jobOfferID) {

    }

    @Override
    public long getContactsCountForEnterprise(long enterpriseID) {
        return 0;
    }

    @Override
    public long getContactsCountForUser(long userID) {
        return 0;
    }
}
