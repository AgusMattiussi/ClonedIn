package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.enums.JobOfferAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class JobOfferHibernateDao implements JobOfferDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ContactDao contactDao;

    private static final int UNEXISTING_CATEGORY_ID = 0;

    @Override
    public JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality) {
        final JobOffer jobOffer = new JobOffer(enterprise, category, position, description, salary, modality, JobOfferAvailability.ACTIVE.getStatus());
        em.persist(jobOffer);
        return jobOffer;
    }

    @Override
    public Optional<JobOffer> findById(long id) {
        return Optional.ofNullable(em.find(JobOffer.class, id));
    }

    @Override
    public List<JobOffer> findByEnterprise(Enterprise enterprise) {
        TypedQuery<JobOffer> query = em.createQuery("SELECT j FROM JobOffer j WHERE j.enterprise = :enterprise", JobOffer.class);
        query.setParameter("enterprise", enterprise);

        return query.getResultList();
    }

    @Override
    public List<JobOffer> findByEnterprise(Enterprise enterprise, int page, int pageSize) {
        TypedQuery<JobOffer> query = em.createQuery("SELECT j FROM JobOffer j WHERE j.enterprise = :enterprise", JobOffer.class);
        query.setParameter("enterprise", enterprise);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<JobOffer> findActiveByEnterprise(Enterprise enterprise) {
        TypedQuery<JobOffer> query = em.createQuery("SELECT j FROM JobOffer j WHERE j.enterprise = :enterprise AND j.available = :status", JobOffer.class);
        query.setParameter("enterprise", enterprise);
        query.setParameter("status", JobOfferAvailability.ACTIVE.getStatus());

        return query.getResultList();
    }

    @Override
    public List<JobOffer> findActiveByEnterprise(Enterprise enterprise, int page, int pageSize) {
        TypedQuery<JobOffer> query = em.createQuery("SELECT j FROM JobOffer j WHERE j.enterprise = :enterprise AND j.available = :status", JobOffer.class);
        query.setParameter("enterprise", enterprise);
        query.setParameter("status", JobOfferAvailability.ACTIVE.getStatus());

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }


    @Override
    public List<JobOffer> getAllJobOffers() {
        return em.createQuery("SELECT j FROM JobOffer j", JobOffer.class).getResultList();
    }

    @Override
    public List<JobOffer> getAllJobOffers(int page, int pageSize) {
        TypedQuery<JobOffer> query = em.createQuery("SELECT j FROM JobOffer j", JobOffer.class);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getJobOffersCount() {
        Query query = em.createQuery("SELECT COUNT(j) FROM JobOffer j");
        return (Long) query.getSingleResult();
    }

    @Override
    public long getJobOffersCountForEnterprise(Enterprise enterprise) {
        Query query = em.createQuery("SELECT COUNT(j) FROM JobOffer j WHERE j.enterprise = :enterprise");
        query.setParameter("enterprise", enterprise);

        return (Long) query.getSingleResult();
    }

    @Override
    public long getActiveJobOffersCountForEnterprise(Enterprise enterprise) {
        Query query = em.createQuery("SELECT COUNT(j) FROM JobOffer j WHERE j.enterprise = :enterprise AND j.available = :active");
        query.setParameter("enterprise", enterprise);
        query.setParameter("active", JobOfferAvailability.ACTIVE.getStatus());

        return (Long) query.getSingleResult();
    }

    private void filterQueryAppendConditions(StringBuilder queryStringBuilder, Category category, String modality, String enterpriseName, String skillDescription,
                                             String position, BigDecimal minSalary, BigDecimal maxSalary){
        if(category != null)
            queryStringBuilder.append(" AND jo.category = :category");
        if(!modality.isEmpty())
            queryStringBuilder.append(" AND jo.modality = :modality");
        if(minSalary != null)
            queryStringBuilder.append(" AND jo.salary >= :minSalary");
        if(maxSalary != null)
            queryStringBuilder.append(" AND jo.salary <= :maxSalary");
        if(!position.isEmpty())
            queryStringBuilder.append(" AND LOWER(jo.position) LIKE LOWER(CONCAT('%', :position, '%'))");
        if(!enterpriseName.isEmpty())
            queryStringBuilder.append(" AND LOWER(e.name) LIKE LOWER(CONCAT('%', :enterpriseName, '%'))");
        if(!skillDescription.isEmpty())
            queryStringBuilder.append(" AND EXISTS (SELECT josk FROM JobOfferSkill josk JOIN josk.skill sk WHERE josk.jobOffer = jo")
                    .append(" AND LOWER(sk.description) LIKE LOWER(:skillDescription))");
    }

    private void filterQueryAppendConditions(StringBuilder queryStringBuilder, Category category, String modality, String term, BigDecimal minSalary, BigDecimal maxSalary){
        if(category != null)
            queryStringBuilder.append(" AND jo.category = :category");
        if(!modality.isEmpty())
            queryStringBuilder.append(" AND jo.modality = :modality");
        if(minSalary != null)
            queryStringBuilder.append(" AND jo.salary >= :minSalary");
        if(maxSalary != null)
            queryStringBuilder.append(" AND jo.salary <= :maxSalary");
        if(!term.isEmpty()) {
            queryStringBuilder.append(" AND (LOWER(jo.position) LIKE LOWER(CONCAT('%', :term, '%'))")
                    .append(" OR LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%'))")
                    .append(" OR EXISTS (SELECT josk FROM JobOfferSkill josk JOIN josk.skill sk WHERE josk.jobOffer = jo")
                    .append(" AND LOWER(sk.description) LIKE LOWER(CONCAT('%', :term, '%'))))");
        }
    }

    private void filterQuerySetParameters(Query query, Category category, String modality, String enterpriseName, String skillDescription,
                                          String position, BigDecimal minSalary, BigDecimal maxSalary){
        query.setParameter("active", JobOfferAvailability.ACTIVE.getStatus());
        if(category != null)
            query.setParameter("category", category);
        if(!modality.isEmpty())
            query.setParameter("modality", modality);
        if(minSalary != null)
            query.setParameter("minSalary", minSalary);
        if(maxSalary != null)
            query.setParameter("maxSalary", maxSalary);
        if(!position.isEmpty())
            query.setParameter("position", position);
        if(!enterpriseName.isEmpty())
            query.setParameter("enterpriseName", enterpriseName);
        if(!skillDescription.isEmpty())
            query.setParameter("skillDescription", skillDescription);
    }

    private void filterQuerySetParameters(Query query, Category category, String modality, String term, BigDecimal minSalary, BigDecimal maxSalary){
        query.setParameter("active", JobOfferAvailability.ACTIVE.getStatus());
        if(category != null)
            query.setParameter("category", category);
        if(!modality.isEmpty())
            query.setParameter("modality", modality);
        if(minSalary != null)
            query.setParameter("minSalary", minSalary);
        if(maxSalary != null)
            query.setParameter("maxSalary", maxSalary);
        if(!term.isEmpty())
            query.setParameter("term", term);
    }

    @Override
    public List<JobOffer> getJobOffersListByFilters(Category category, String modality, String enterpriseName, String skillDescription,
                                                    String position, BigDecimal minSalary, BigDecimal maxSalary, int page, int pageSize) {
        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT jo FROM JobOffer jo");

        if(!enterpriseName.isEmpty())
            queryStringBuilder.append(" JOIN jo.enterprise e");

        queryStringBuilder.append(" WHERE jo.available = :active");

        filterQueryAppendConditions(queryStringBuilder, category, modality, enterpriseName, skillDescription, position, minSalary, maxSalary);

        TypedQuery<JobOffer> query = em.createQuery(queryStringBuilder.toString(), JobOffer.class);
        filterQuerySetParameters(query, category, modality, enterpriseName, skillDescription, position, minSalary, maxSalary);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<JobOffer> getJobOffersListByFilters(Category category, String modality, String term, BigDecimal minSalary, BigDecimal maxSalary, int page, int pageSize) {
        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT jo FROM JobOffer jo");

        if(!term.isEmpty())
            queryStringBuilder.append(" JOIN jo.enterprise e");

        queryStringBuilder.append(" WHERE jo.available = :active");

        filterQueryAppendConditions(queryStringBuilder, category, modality, term, minSalary, maxSalary);

        TypedQuery<JobOffer> query = em.createQuery(queryStringBuilder.toString(), JobOffer.class);
        filterQuerySetParameters(query, category, modality, term, minSalary, maxSalary);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }


    @Override
    public long getActiveJobOffersCount(Category category, String modality, String enterpriseName, String skillDescription,
                                        String position, BigDecimal minSalary, BigDecimal maxSalary) {
        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT COUNT(jo) FROM JobOffer jo");

        if(!enterpriseName.isEmpty())
            queryStringBuilder.append(" JOIN jo.enterprise e");

        queryStringBuilder.append(" WHERE jo.available = :active");

        filterQueryAppendConditions(queryStringBuilder, category, modality, enterpriseName, skillDescription, position, minSalary, maxSalary);

        Query query = em.createQuery(queryStringBuilder.toString());
        filterQuerySetParameters(query, category, modality, enterpriseName, skillDescription, position, minSalary, maxSalary);

        return (Long) query.getSingleResult();
    }

    @Override
    public long getActiveJobOffersCount(Category category, String modality, String term, BigDecimal minSalary, BigDecimal maxSalary) {
        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT COUNT(jo) FROM JobOffer jo");

        if(!term.isEmpty())
            queryStringBuilder.append(" JOIN jo.enterprise e");

        queryStringBuilder.append(" WHERE jo.available = :active");

        filterQueryAppendConditions(queryStringBuilder, category, modality, term, minSalary, maxSalary);

        Query query = em.createQuery(queryStringBuilder.toString());
        filterQuerySetParameters(query, category, modality, term, minSalary, maxSalary);

        return (Long) query.getSingleResult();
    }

    private void updateJobOfferAvailability(long jobOfferID, JobOfferAvailability joa){
        Query query = em.createQuery("UPDATE JobOffer SET available = :status WHERE id = :jobOfferID");
        query.setParameter("status", joa.getStatus());
        query.setParameter("jobOfferID", jobOfferID);
        query.executeUpdate();
    }

    @Override
    public void closeJobOffer(JobOffer jobOffer) {
        updateJobOfferAvailability(jobOffer.getId(), JobOfferAvailability.CLOSED);
        contactDao.closeJobOfferForEveryone(jobOffer);
    }

    @Override
    public void cancelJobOffer(JobOffer jobOffer) {
        updateJobOfferAvailability(jobOffer.getId(), JobOfferAvailability.CANCELLED);
        contactDao.cancelJobOfferForEveryone(jobOffer);
    }
}
