package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import ar.edu.itba.paw.models.enums.SortBy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.time.Instant;
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
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, SortBy sortBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user", Contact.class);
        else {
            if(sortBy.equals(SortBy.DATE_ASC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy ORDER BY c.date ASC", Contact.class);
            else if(sortBy.equals(SortBy.DATE_DESC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy ORDER BY c.date DESC", Contact.class);
            else
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
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, String status, SortBy sortBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.status = :status", Contact.class);
        else {
            if(sortBy.equals(SortBy.DATE_ASC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy AND c.status = :status ORDER BY c.date ASC", Contact.class);
            else if(sortBy.equals(SortBy.DATE_DESC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy AND c.status = :status ORDER BY c.date DESC", Contact.class);
            else
                query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy AND c.status = :status", Contact.class);
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
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, SortBy sortBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise", Contact.class);
        else {
            if(sortBy.equals(SortBy.USERNAME))
                query = em.createQuery("SELECT c FROM Contact c JOIN c.user u WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY u.name", Contact.class);
            else if(sortBy.equals(SortBy.STATUS))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY c.status", Contact.class);
            else if(sortBy.equals(SortBy.JOB_OFFER_POSITION))
                query = em.createQuery("SELECT c FROM Contact c JOIN c.jobOffer j WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY j.position", Contact.class);
            else if(sortBy.equals(SortBy.DATE_ASC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY c.date ASC", Contact.class);
            else if(sortBy.equals(SortBy.DATE_DESC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY c.date DESC", Contact.class);
            else
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
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, String status, SortBy sortBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.status = :status", Contact.class);
        else {
            if(sortBy.equals(SortBy.USERNAME))
                query = em.createQuery("SELECT c FROM Contact c JOIN c.user u WHERE c.enterprise = :enterprise AND c.status = :status AND c.filledBy = :filledBy ORDER BY u.name", Contact.class);
            else if(sortBy.equals(SortBy.STATUS))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.status = :status AND c.filledBy = :filledBy ORDER BY c.status", Contact.class);
            else if(sortBy.equals(SortBy.JOB_OFFER_POSITION))
                query = em.createQuery("SELECT c FROM Contact c JOIN c.jobOffer j WHERE c.enterprise = :enterprise AND c.status = :status AND c.filledBy = :filledBy ORDER BY j.position", Contact.class);
            else if(sortBy.equals(SortBy.DATE_ASC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.status = :status AND c.filledBy = :filledBy ORDER BY c.date ASC", Contact.class);
            else if(sortBy.equals(SortBy.DATE_DESC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.status = :status AND c.filledBy = :filledBy ORDER BY c.date DESC", Contact.class);
            else
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
    public boolean alreadyContactedByEnterprise(long userID, long enterpriseID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM contactado WHERE idUsuario = :userID AND idEmpresa = :enterpriseID AND creadoPor = :filledBy");
        query.setParameter("userID", userID);
        query.setParameter("enterpriseID", enterpriseID);
        query.setParameter("filledBy", FilledBy.ENTERPRISE.getFilledBy());

        return ((BigInteger) query.getSingleResult()).longValue() > 0;
    }

    @Override
    public Optional<String> getStatus(User user, JobOffer jobOffer) {
        TypedQuery<String> query = em.createQuery("SELECT c.status FROM Contact c WHERE c.user = :user AND c.jobOffer = :jobOffer", String.class);
        query.setParameter("jobOffer", jobOffer);
        query.setParameter("user", user);

        return Optional.ofNullable(query.getSingleResult());
    }

    private boolean updateJobOfferStatus(User user, JobOffer jobOffer, JobOfferStatus jobOfferStatus){
        Query query = em.createQuery("UPDATE Contact SET status = :status WHERE user = :user AND jobOffer = :jobOffer");
        query.setParameter("status", jobOfferStatus.getStatus());
        query.setParameter("user", user);
        query.setParameter("jobOffer", jobOffer);

        return query.executeUpdate() > 0;
    }

    private boolean updateJobOfferStatusForEveryone(JobOffer jobOffer, JobOfferStatus jobOfferStatus){
        Query query = em.createQuery("UPDATE Contact SET status = :status WHERE jobOffer = :jobOffer");
        query.setParameter("status", jobOfferStatus.getStatus());
        query.setParameter("jobOffer", jobOffer);

        return query.executeUpdate() > 0;
    }

    @Override
    public boolean acceptJobOffer(User user, JobOffer jobOffer) {
       return updateJobOfferStatus(user, jobOffer, JobOfferStatus.ACCEPTED);
    }

    @Override
    public boolean rejectJobOffer(User user, JobOffer jobOffer) {
        return updateJobOfferStatus(user, jobOffer, JobOfferStatus.DECLINED);
    }

    @Override
    public boolean cancelJobOffer(User user, JobOffer jobOffer) {
        return updateJobOfferStatus(user, jobOffer, JobOfferStatus.CANCELLED);
    }

    @Override
    public boolean cancelJobOfferForEveryone(JobOffer jobOffer) {
        return updateJobOfferStatusForEveryone(jobOffer, JobOfferStatus.CANCELLED);
    }

    @Override
    public boolean closeJobOffer(User user, JobOffer jobOffer) {
        return updateJobOfferStatus(user, jobOffer, JobOfferStatus.CLOSED);
    }

    @Override
    public boolean closeJobOfferForEveryone(JobOffer jobOffer) {
        return updateJobOfferStatusForEveryone(jobOffer, JobOfferStatus.CLOSED);
    }

    @Override
    public long getContactsCountForEnterprise(Enterprise enterprise) {
        Query query = em.createQuery("SELECT COUNT(c) FROM Contact c WHERE c.enterprise = :enterprise");
        query.setParameter("enterprise", enterprise);
        return (Long) query.getSingleResult();
    }

    @Override
    public long getContactsCountForEnterprise(long enterpriseID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM contactado WHERE idEmpresa = :enterpriseID AND creadoPor = :filledBy");
        query.setParameter("enterpriseID", enterpriseID);
        query.setParameter("filledBy", FilledBy.ENTERPRISE.getFilledBy());
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    public long getContactsCountForUser(User user) {
        Query query = em.createQuery("SELECT COUNT(c) FROM Contact c WHERE c.user = :user");
        query.setParameter("user", user);
        return (Long) query.getSingleResult();
    }

    @Override
    public long getContactsCountForUser(long userID, FilledBy filledBy) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM contactado WHERE idUsuario = :userID AND creadoPor = :filledBy");
        query.setParameter("userID", userID);
        query.setParameter("filledBy", filledBy.getFilledBy());

        return ((BigInteger) query.getSingleResult()).longValue();
    }
}
