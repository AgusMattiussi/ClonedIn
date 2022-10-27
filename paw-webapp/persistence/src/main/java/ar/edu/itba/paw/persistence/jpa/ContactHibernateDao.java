package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

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
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise", Contact.class);
        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, int page, int pageSize) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise", Contact.class);
        query.setParameter("enterprise", enterprise);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer", Contact.class);
        query.setParameter("jobOffer", jobOffer);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer, int page, int pageSize) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer", Contact.class);
        query.setParameter("jobOffer", jobOffer);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.enterprise = :enterprise", Contact.class);
        query.setParameter("user", user);
        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, int page, int pageSize) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.enterprise = :enterprise", Contact.class);
        query.setParameter("user", user);
        query.setParameter("enterprise", enterprise);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer AND c.enterprise = :enterprise", Contact.class);
        query.setParameter("jobOffer", jobOffer);
        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, int page, int pageSize) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer AND c.enterprise = :enterprise", Contact.class);
        query.setParameter("jobOffer", jobOffer);
        query.setParameter("enterprise", enterprise);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    // FIXME: Puede no funcionar
    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM contactado WHERE idUsuario = :userID AND idOferta = :jobOfferID", Long.class);
        query.setParameter("userID", userID);
        query.setParameter("jobOfferID", jobOfferID);

        return ((Long) query.getSingleResult()) > 0;
    }

    @Override
    public Optional<String> getStatus(User user, JobOffer jobOffer) {
        TypedQuery<String> query = em.createQuery("SELECT c.status FROM Contact c WHERE c.user = :user AND c.jobOffer = :jobOffer", String.class);
        query.setParameter("jobOffer", jobOffer);
        query.setParameter("user", user);

        return Optional.ofNullable(query.getSingleResult());
    }

    private void updateJobOfferStatus(User user, JobOffer jobOffer, JobOfferStatus jobOfferStatus){
        Query query = em.createQuery("UPDATE Contact SET status = :status WHERE user = :user AND jobOffer = :jobOffer");
        query.setParameter("status", jobOfferStatus.getStatus());
        query.setParameter("user", user);
        query.setParameter("jobOffer", jobOffer);

        query.executeUpdate();
    }

    @Override
    public void acceptJobOffer(User user, JobOffer jobOffer) {
        updateJobOfferStatus(user, jobOffer, JobOfferStatus.ACCEPTED);
    }

    @Override
    public void rejectJobOffer(User user, JobOffer jobOffer) {
        updateJobOfferStatus(user, jobOffer, JobOfferStatus.DECLINED);
    }

    @Override
    public void cancelJobOffer(User user, JobOffer jobOffer) {
        updateJobOfferStatus(user, jobOffer, JobOfferStatus.CANCELLED);
    }

    @Override
    public void cancelJobOfferForEveryone(long jobOfferID) {

    }

    @Override
    public void closeJobOffer(User user, JobOffer jobOffer) {
        updateJobOfferStatus(user, jobOffer, JobOfferStatus.CLOSED);
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
