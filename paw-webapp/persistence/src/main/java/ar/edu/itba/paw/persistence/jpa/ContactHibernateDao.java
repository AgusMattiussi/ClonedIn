package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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
    public List<User> getUsersForEnterprise(Enterprise enterprise) {
        TypedQuery<User> query = em.createQuery("SELECT c.user FROM Contact c WHERE c.enterprise = :enterprise", User.class);
        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    // TODO: Esto se podria solucionar con variaciones de getContact

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
    public List<Contact> getContactsForUser(User user) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user", Contact.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForUser(User user, int page, int pageSize) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user", Contact.class);
        query.setParameter("user", user);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise) {
        return null;
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, int page, int pageSize) {
        return null;
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer) {
        return null;
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer, int page, int pageSize) {
        return null;
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user) {
        return null;
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, int page, int pageSize) {
        return null;
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer) {
        return null;
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, int page, int pageSize) {
        return null;
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM contactado WHERE idUsuario = :userID AND idOferta = :jobOfferID", Long.class);
        query.setParameter("userID", userID);
        query.setParameter("jobOfferID", jobOfferID);

        return ((Long) query.getSingleResult()) > 0;
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
