package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.enums.JobOfferAvailability;
import ar.edu.itba.paw.models.enums.JobOfferModality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class JobOfferHibernateDao implements JobOfferDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ContactDao contactDao;

    @Override
    public JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, JobOfferModality modality) {
        final JobOffer jobOffer = new JobOffer(enterprise, category, position, description, salary,
                modality.getModality(), JobOfferAvailability.ACTIVE.getStatus());
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

    private void filterQueryAppendConditions(StringBuilder queryStringBuilder, Category category, JobOfferModality modality,
                                             String skillDescription, String enterpriseName, String searchTerm, String position,
                                             BigDecimal minSalary, BigDecimal maxSalary){
        if(category != null)
            queryStringBuilder.append(" AND jo.category = :category");
        if(modality != null)
            queryStringBuilder.append(" AND jo.modality = :modality");
        if(minSalary != null)
            queryStringBuilder.append(" AND jo.salary >= :minSalary");
        if(maxSalary != null)
            queryStringBuilder.append(" AND jo.salary <= :maxSalary");
        if(position != null && !position.isEmpty())
            queryStringBuilder.append(" AND LOWER(jo.position) LIKE LOWER(CONCAT('%', :position, '%'))");
        if(skillDescription != null && !skillDescription.isEmpty()) {
            queryStringBuilder
                    .append(" AND EXISTS (SELECT josk FROM JobOfferSkill josk JOIN josk.skill sk WHERE josk.jobOffer = jo AND LOWER(sk.description) LIKE LOWER(CONCAT('%', :skillDescription, '%'))  ESCAPE '\\')");
        }
        if(enterpriseName != null && !enterpriseName.isEmpty())
            queryStringBuilder.append(" AND LOWER(e.name) LIKE LOWER(CONCAT('%', :enterpriseName, '%'))");
        if(searchTerm != null && !searchTerm.isEmpty()) {
            queryStringBuilder.append(" AND (LOWER(jo.position) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'")
                    .append(" OR LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'")
                    .append(" OR EXISTS (SELECT josk1 FROM JobOfferSkill josk1 JOIN josk1.skill sk1 WHERE josk1.jobOffer = jo")
                    .append(" AND LOWER(sk1.description) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'))");
        }
    }

    private void filterQueryAppendConditions(StringBuilder queryStringBuilder, Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary){
        if(category != null)
            queryStringBuilder.append(" AND jo.category = :category");
        if(modality != null)
            queryStringBuilder.append(" AND jo.modality = :modality");
        if(minSalary != null)
            queryStringBuilder.append(" AND jo.salary >= :minSalary");
        if(maxSalary != null)
            queryStringBuilder.append(" AND jo.salary <= :maxSalary");
        if(term != null && !term.isEmpty()) {
            queryStringBuilder.append(" AND (LOWER(jo.position) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'")
                    .append(" OR LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'")
                    .append(" OR EXISTS (SELECT josk FROM JobOfferSkill josk JOIN josk.skill sk WHERE josk.jobOffer = jo")
                    .append(" AND LOWER(sk.description) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'))");
        }
    }

    private void filterQuerySetParameters(Query query, Category category, JobOfferModality modality, String skillDescription,
                                          String enterpriseName, String searchTerm, String position, BigDecimal minSalary,
                                          BigDecimal maxSalary, boolean onlyActive){
        if(onlyActive)
            query.setParameter("active", JobOfferAvailability.ACTIVE.getStatus());
        if(category != null)
            query.setParameter("category", category);
        if(modality != null)
            query.setParameter("modality", modality.getModality());
        if(minSalary != null)
            query.setParameter("minSalary", minSalary);
        if(maxSalary != null)
            query.setParameter("maxSalary", maxSalary);
        if(position != null && !position.isEmpty())
            query.setParameter("position", position);
        if(skillDescription != null && !skillDescription.isEmpty())
            query.setParameter("skillDescription", skillDescription);
        if(enterpriseName != null && !enterpriseName.isEmpty())
            query.setParameter("enterpriseName", enterpriseName);
        if(searchTerm != null && !searchTerm.isEmpty())
            query.setParameter("term", searchTerm);
    }

    private void filterQuerySetParameters(Query query, Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary){
        query.setParameter("active", JobOfferAvailability.ACTIVE.getStatus());
        if(category != null)
            query.setParameter("category", category);
        if(modality != null)
            query.setParameter("modality", modality.getModality());
        if(minSalary != null)
            query.setParameter("minSalary", minSalary);
        if(maxSalary != null)
            query.setParameter("maxSalary", maxSalary);
        if(term != null && !term.isEmpty())
            query.setParameter("term", term);
    }

    @Override
    public List<JobOffer> getJobOffersListByFilters(Category category, JobOfferModality modality, String skillDescription,
                                                    String enterpriseName, String searchTerm, String position, BigDecimal minSalary,
                                                    BigDecimal maxSalary, boolean onlyActive, int page, int pageSize) {
        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT jo FROM JobOffer jo");

        if(enterpriseName != null && !enterpriseName.isEmpty() || searchTerm != null && !searchTerm.isEmpty())
            queryStringBuilder.append(" JOIN jo.enterprise e");

        if(onlyActive)
            queryStringBuilder.append(" WHERE jo.available = :active");
        else
            queryStringBuilder.append(" WHERE 1 = 1");

        filterQueryAppendConditions(queryStringBuilder, category, modality, skillDescription, enterpriseName, searchTerm,
                position, minSalary, maxSalary);

        TypedQuery<JobOffer> query = em.createQuery(queryStringBuilder.toString(), JobOffer.class);
        filterQuerySetParameters(query, category, modality, skillDescription, enterpriseName, searchTerm, position,
                minSalary, maxSalary, onlyActive);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<JobOffer> getJobOffersListByFilters(Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary, int page, int pageSize) {

        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT jo FROM JobOffer jo");

        if(term != null && !term.isEmpty()){
            term = term.replace("_", "\\_");
            term = term.replace("%", "\\%");
            queryStringBuilder.append(" JOIN jo.enterprise e");
        }

        queryStringBuilder.append(" WHERE jo.available = :active");

        filterQueryAppendConditions(queryStringBuilder, category, modality, term, minSalary, maxSalary);

        TypedQuery<JobOffer> query = em.createQuery(queryStringBuilder.toString(), JobOffer.class);
        filterQuerySetParameters(query, category, modality, term, minSalary, maxSalary);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }



    @Override
    public long getJobOfferCount(Category category, JobOfferModality modality, String skillDescription, String enterpriseName,
                                 String searchTerm, String position, BigDecimal minSalary, BigDecimal maxSalary, boolean onlyActive) {
        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT COUNT(jo) FROM JobOffer jo");

        if(enterpriseName != null && !enterpriseName.isEmpty() || searchTerm != null && !searchTerm.isEmpty())
            queryStringBuilder.append(" JOIN jo.enterprise e");

        if(onlyActive)
            queryStringBuilder.append(" WHERE jo.available = :active");
        else
            queryStringBuilder.append(" WHERE 1 = 1");

        filterQueryAppendConditions(queryStringBuilder, category, modality, skillDescription, enterpriseName, searchTerm,
                position, minSalary, maxSalary);

        Query query = em.createQuery(queryStringBuilder.toString());
        filterQuerySetParameters(query, category, modality, skillDescription, enterpriseName, searchTerm, position,
                minSalary, maxSalary, onlyActive);

        return (Long) query.getSingleResult();
    }

    @Override
    public long getJobOfferCount(Category category, JobOfferModality modality, String term, BigDecimal minSalary, BigDecimal maxSalary) {
        if(term != null && !term.isEmpty()){
            term = term.replace("_", "\\_");
            term = term.replace("%", "\\%");
        }

        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT COUNT(jo) FROM JobOffer jo");

        if(term != null && !term.isEmpty())
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
