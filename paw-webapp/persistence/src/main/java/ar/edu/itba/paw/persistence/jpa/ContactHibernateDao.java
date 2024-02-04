package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.FilledBy;
import ar.edu.itba.paw.models.enums.JobOfferStatus;
import ar.edu.itba.paw.models.enums.ContactSorting;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class ContactHibernateDao implements ContactDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Contact> findByPrimaryKey(long userID, long jobOfferID) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.user.id = :userID AND c.jobOffer.id = :jobOfferID", Contact.class);
        query.setParameter("userID", userID);
        query.setParameter("jobOfferID", jobOfferID);

        return query.getResultList().stream().findFirst();
    }

    @Override
    public Contact addContact(Enterprise enterprise, User user, JobOffer jobOffer, FilledBy filledBy) {
        Contact contact = new Contact(user, enterprise, jobOffer, filledBy, Date.from(Instant.now()));
        em.persist(contact);
        return contact;
    }

    @Override
    public List<Enterprise> getEnterprisesForUser(User user, FilledBy filledBy) {
        TypedQuery<Enterprise> query;

        if (filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c.enterprise FROM Contact c WHERE c.user = :user", Enterprise.class);
        else {
            query = em.createQuery("SELECT c.enterprise FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy", Enterprise.class);
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
        }


        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, ContactSorting sortBy, int page, int pageSize) {
        TypedQuery<Contact> query;


        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user", Contact.class);
        else {
            if(sortBy.equals(ContactSorting.DATE_ASC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy ORDER BY c.date ASC", Contact.class);
            else if(sortBy.equals(ContactSorting.DATE_DESC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy ORDER BY c.date DESC", Contact.class);
            else
                query = em.createQuery("SELECT c FROM Contact c WHERE c.user = :user AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
        }

        query.setParameter("user", user);
        query.setParameter("status", status);
        return query.getResultList();
    }

    private void getContactsForUserAppendConditions(StringBuilder queryBuilder, FilledBy filledBy, JobOfferStatus status, ContactSorting sortBy){
        if(filledBy != null && !filledBy.equals(FilledBy.ANY)){
            queryBuilder.append(" AND c.filledBy = :filledBy");
        }

        if(status != null){
            if(status == JobOfferStatus.CANCELLED && filledBy == FilledBy.ENTERPRISE
            || status == JobOfferStatus.DECLINED && filledBy == FilledBy.USER) {
                queryBuilder.append(" AND (c.status = :status OR c.status = 'cerrada')");
            } else {
                queryBuilder.append(" AND c.status = :status");
            }
        }

        if(sortBy != null){
            switch (sortBy){
                case JOB_OFFER_POSITION:
                    queryBuilder.append(" ORDER BY j.position");
                    break;
                case DATE_ASC:
                    queryBuilder.append(" ORDER BY c.date ASC");
                    break;
                case DATE_DESC:
                    queryBuilder.append(" ORDER BY c.date DESC");
                    break;
                case STATUS:
                    queryBuilder.append(" ORDER BY c.status");
                    break;
            }
        }

    }

    private void getContactsForUserSetParameters(Query query, User user, FilledBy filledBy, JobOfferStatus status){
        query.setParameter("user", user);

        if(status != null){
            query.setParameter("status", status.getStatus());
        }

        if(filledBy != null && !filledBy.equals(FilledBy.ANY)) {
            query.setParameter("filledBy", filledBy.getValue());
        }
    }

    @Override
    public List<Contact> getContactsForUser(User user, FilledBy filledBy, JobOfferStatus status, ContactSorting sortBy, int page, int pageSize) {
        TypedQuery<Contact> query;
        StringBuilder queryBuilder = new StringBuilder().append(" SELECT c FROM Contact c");

        if(sortBy == ContactSorting.JOB_OFFER_POSITION)
            queryBuilder.append(" JOIN c.jobOffer j");

        queryBuilder.append(" WHERE c.user = :user");

        getContactsForUserAppendConditions(queryBuilder, filledBy, status, sortBy);

        query = em.createQuery(queryBuilder.toString(), Contact.class);
        getContactsForUserSetParameters(query, user, filledBy, status);

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
            query.setParameter("filledBy", filledBy.getValue());
        }

        query.setParameter("enterprise", enterprise);
        return query.getResultList();
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, FilledBy filledBy, ContactSorting sortBy, int page, int pageSize) {
        TypedQuery<Contact> query;

        if(filledBy.equals(FilledBy.ANY))
            query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise", Contact.class);
        else {
            if(sortBy.equals(ContactSorting.USERNAME))
                query = em.createQuery("SELECT c FROM Contact c JOIN c.user u WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY u.name", Contact.class);
            else if(sortBy.equals(ContactSorting.STATUS))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY c.status", Contact.class);
            else if(sortBy.equals(ContactSorting.JOB_OFFER_POSITION))
                query = em.createQuery("SELECT c FROM Contact c JOIN c.jobOffer j WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY j.position", Contact.class);
            else if(sortBy.equals(ContactSorting.DATE_ASC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY c.date ASC", Contact.class);
            else if(sortBy.equals(ContactSorting.DATE_DESC))
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy ORDER BY c.date DESC", Contact.class);
            else
                query = em.createQuery("SELECT c FROM Contact c WHERE c.enterprise = :enterprise AND c.filledBy = :filledBy", Contact.class);
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
        }

        query.setParameter("enterprise", enterprise);
        query.setParameter("status", status);
        return query.getResultList();
    }

    private void getContactsForEnterpriseAppendConditions(StringBuilder queryBuilder, JobOffer jobOffer, User user, FilledBy filledBy, String status, ContactSorting sortBy){

        if(sortBy != null){
            switch (sortBy){
                case USERNAME:
                    queryBuilder.append(" JOIN c.user u");
                    break;
                case JOB_OFFER_POSITION:
                    queryBuilder.append(" JOIN c.jobOffer j");
            }
        }

        queryBuilder.append(" WHERE c.enterprise = :enterprise");

        if(jobOffer != null){
            queryBuilder.append(" AND c.jobOffer = :jobOffer");
        }

        if(user != null){
            queryBuilder.append(" AND c.user = :user");
        }

        if(status != null && !status.isEmpty()){
            if(status.equals(JobOfferStatus.CANCELLED.getStatus()) && filledBy.equals(FilledBy.ENTERPRISE)
            || status.equals(JobOfferStatus.DECLINED.getStatus()) && filledBy.equals(FilledBy.USER)) {
                queryBuilder.append(" AND (c.status = :status OR c.status = 'cerrada')");
            } else {
                queryBuilder.append(" AND c.status = :status");
            }
        }

        if(filledBy != null && !filledBy.equals(FilledBy.ANY)) {
            queryBuilder.append(" AND c.filledBy = :filledBy");
        }

        if(sortBy != null){
            switch (sortBy){
                case USERNAME:
                    queryBuilder.append(" ORDER BY u.name");
                    break;
                case JOB_OFFER_POSITION:
                    queryBuilder.append(" ORDER BY j.position");
                    break;
                case DATE_ASC:
                    queryBuilder.append(" ORDER BY c.date ASC");
                    break;
                case DATE_DESC:
                    queryBuilder.append(" ORDER BY c.date DESC");
                    break;
            }
        }
    }

    private void getContactsForEnterpriseSetParameters(Query query, Enterprise enterprise, JobOffer jobOffer, User user,
                                                       FilledBy filledBy, String status){
        if(enterprise != null){
            query.setParameter("enterprise", enterprise);
        }

        if(jobOffer != null){
            query.setParameter("jobOffer", jobOffer);
        }

        if(user != null){
            query.setParameter("user", user);
        }

        if(status != null && !status.isEmpty()){
            query.setParameter("status", status);
        }

        if(filledBy != null && !filledBy.equals(FilledBy.ANY)) {
            query.setParameter("filledBy", filledBy.getValue());
        }
    }

    @Override
    public List<Contact> getContactsForEnterprise(Enterprise enterprise, JobOffer jobOffer, User user, FilledBy filledBy,
                                                  String status, ContactSorting sortBy, int page, int pageSize) {
        TypedQuery<Contact> query;
        StringBuilder queryBuilder = new StringBuilder().append("SELECT c FROM Contact c");

        getContactsForEnterpriseAppendConditions(queryBuilder, jobOffer, user, filledBy, status, sortBy);

        query = em.createQuery(queryBuilder.toString(), Contact.class);
        getContactsForEnterpriseSetParameters(query, enterprise, jobOffer, user, filledBy, status);

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
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
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
            query.setParameter("filledBy", filledBy.getValue());
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
        query.setParameter("filledBy", FilledBy.ENTERPRISE.getValue());

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
        Query query = em.createQuery("UPDATE Contact SET status = :status WHERE jobOffer = :jobOffer AND status = 'pendiente' ");
        query.setParameter("status", JobOfferStatus.CLOSED.getStatus());
        query.setParameter("jobOffer", jobOffer);

        return query.executeUpdate() > 0;
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
        query.setParameter("filledBy", FilledBy.ENTERPRISE.getValue());
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    public long getContactsCountForEnterprise(Enterprise enterprise, JobOffer jobOffer, User user, FilledBy filledBy, String status) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM contactado WHERE idEmpresa = :enterpriseId");

        if(jobOffer != null)
            queryBuilder.append(" AND idOferta = :jobOfferId");

        if(user != null)
            queryBuilder.append(" AND idUsuario = :userId");

        if(filledBy != null && filledBy != FilledBy.ANY)
            queryBuilder.append(" AND creadoPor = :filledBy");

        if(status != null && !status.isEmpty())
            queryBuilder.append(" AND estado = :status");

        Query query = em.createNativeQuery(queryBuilder.toString());
        query.setParameter("enterpriseId", enterprise.getId());

        if(jobOffer != null)
            query.setParameter("jobOfferId", jobOffer.getId());

        if(user != null)
            query.setParameter("userId", user.getId());

        if(filledBy != null && filledBy != FilledBy.ANY)
            query.setParameter("filledBy", filledBy.getValue());

        if(status != null && !status.isEmpty())
            query.setParameter("status", status);

        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    public long getContactsCountForUser(User user) {
        Query query = em.createQuery("SELECT COUNT(c) FROM Contact c WHERE c.user = :user");
        query.setParameter("user", user);
        return (Long) query.getSingleResult();
    }

    @Override
    public long getContactsCountForUser(User user, FilledBy filledBy, JobOfferStatus status) {
        StringBuilder queryBuilder = new StringBuilder().append("SELECT COUNT(c) FROM Contact c WHERE c.user = :user");

        getContactsForUserAppendConditions(queryBuilder, filledBy, status, null);

        Query query = em.createQuery(queryBuilder.toString());
        getContactsForUserSetParameters(query, user, filledBy, status);

        return (Long) query.getSingleResult();
    }
}
