package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Primary
@Repository
public class ContactHibernateDao implements ContactDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addContact(Enterprise enterprise, User user, JobOffer jobOffer, FilledBy filledBy) {
        Contact contact = new Contact(user, enterprise, jobOffer, filledBy, Date.from(Instant.now()));
        em.persist(contact);
    }

    @Override
    public List<Enterprise> getEnterprisesForUser(User user, FilledBy filledBy) {
        TypedQuery<Enterprise> query;

        if (filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c.enterprise FROM Contact c WHERE c.user = :user", Enterprise.class);
        else {
            query = em.createQuery("SELECT c.enterprise FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy", Enterprise.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public List<User> getUsersForEnterprise(Enterprise enterprise, FilledBy filledBy) {
        TypedQuery<User> query;

        if (filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c.user FROM Contact c WHERE c.enterprise = :enterprise", User.class);
        else {
            query = em.createQuery("SELECT c.user FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy", User.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }


        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }


        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("user", user);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, String status) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.status = :status", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.status = :status AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("user", user);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, String status, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.status = :status", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.status = :status AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("user", user);
        query.setParameter("status", status);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("enterprise", enterprise);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.status = :status", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.status = :status AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("enterprise", enterprise);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.status = :status", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.status = :status AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("enterprise", enterprise);
        query.setParameter("status", status);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("jobOffer", jobOffer);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForJobOffer(JobOffer jobOffer, FilledBy filledBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("jobOffer", jobOffer);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, FilledBy filledBy) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.enterprise = :enterprise", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.enterprise = :enterprise AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }
        query.setParameter("user", user);
        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndUser(Enterprise enterprise, User user, FilledBy filledBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.enterprise = :enterprise", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.enterprise = :enterprise AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("user", user);
        query.setParameter("enterprise", enterprise);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer AND c.enterprise = :enterprise", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer AND c.enterprise = :enterprise AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("jobOffer", jobOffer);
        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterpriseAndJobOffer(Enterprise enterprise, JobOffer jobOffer, FilledBy filledBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer AND c.enterprise = :enterprise", Contact.class);
        else {
            query = em.createQuery("SELECT c FROM Contact c WHERE c.jobOffer = :jobOffer AND c.enterprise = :enterprise AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getFilledBy());
        }

        query.setParameter("jobOffer", jobOffer);
        query.setParameter("enterprise", enterprise);
        query.setFirstResult(page * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM contactado WHERE idUsuario = :userID AND idOferta = :jobOfferID");
        query.setParameter("userID", userID);
        query.setParameter("jobOfferID", jobOfferID);

        return ((BigInteger) query.getSingleResult()).longValue() > 0;
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

    private void updateJobOfferStatusForEveryone(JobOffer jobOffer, JobOfferStatus jobOfferStatus){
        Query query = em.createQuery("UPDATE Contact SET status = :status WHERE user = :user AND jobOffer = :jobOffer");
        query.setParameter("status", jobOfferStatus.getStatus());
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
    public void cancelJobOfferForEveryone(JobOffer jobOffer) {
        updateJobOfferStatusForEveryone(jobOffer, JobOfferStatus.CANCELLED);
    }

    @Override
    public void closeJobOffer(User user, JobOffer jobOffer) {
        updateJobOfferStatus(user, jobOffer, JobOfferStatus.CLOSED);
    }

    @Override
    public void closeJobOfferForEveryone(JobOffer jobOffer) {
        updateJobOfferStatusForEveryone(jobOffer, JobOfferStatus.CLOSED);
    }

    @Override
    public long getContactsCountForEnterprise(Enterprise enterprise) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Contact c WHERE c.enterprise = :enterprise", Long.class);
        query.setParameter("enterprise", enterprise);
        return query.getSingleResult();
    }

    @Override
    public long getContactsCountForEnterprise(long enterpriseID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM contactado WHERE idEmpresa = :enterpriseID");
        query.setParameter("enterpriseID", enterpriseID);
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    public long getContactsCountForUser(User user) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Contact c WHERE c.user = :user", Long.class);
        query.setParameter("user", user);
        return query.getSingleResult();
    }

    @Override
    public long getContactsCountForUser(long userID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM contactado WHERE idUsuario = :userID");
        query.setParameter("userID", userID);
        return ((BigInteger) query.getSingleResult()).longValue();
    }
}
