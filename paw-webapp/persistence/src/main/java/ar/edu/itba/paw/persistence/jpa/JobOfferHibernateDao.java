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
        Query query = em.createQuery("SELECT COUNT(j) FROM JobOffer j WHERE j.enterprise = :enterprise AND j.available = 'Activa'");
        query.setParameter("enterprise", enterprise);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<JobOffer> getJobOffersListByFilters(int page, int pageSize, String categoryId, String modality) {
        StringBuilder filterQuery = new StringBuilder();
        filterQuery.append("SELECT * FROM ofertaLaboral WHERE disponible = 'Activa'");
        filterQuery = buildFilterQuery(filterQuery, categoryId, modality);
        filterQuery.append(" ORDER BY id OFFSET :offset LIMIT :limit ");
        Query query = em.createNativeQuery(filterQuery.toString(), JobOffer.class);
        query.setParameter("offset", pageSize * page);
        query.setParameter("limit", pageSize);
        return (List<JobOffer>) query.getResultList();
    }

    // TODO: Tratar de mejorar estos metodos
    @Override
    public Integer getActiveJobOffersCount(String categoryId, String modality) {
        StringBuilder filterQuery = new StringBuilder();
        filterQuery.append("SELECT COUNT(*) FROM ofertaLaboral WHERE disponible = 'Activa'");
        filterQuery = buildFilterQuery(filterQuery, categoryId, modality);
        Query query = em.createNativeQuery(filterQuery.toString());
        BigInteger bi = (BigInteger) query.getSingleResult();
        return bi.intValue();
    }

    private StringBuilder buildFilterQuery(StringBuilder query, String categoryId, String modality){
        int catId;
        try {
            catId = Integer.parseInt(categoryId);
        } catch (NumberFormatException exception){
            catId = UNEXISTING_CATEGORY_ID;
        }
        Object[] sanitizedInputs = new Object[]{catId, modality};

        if(!categoryId.isEmpty())
            query.append(" AND idRubro = '").append(sanitizedInputs[0]).append("'");

        if(!modality.isEmpty())
            query.append(" AND modalidad ILIKE CONCAT('%', '").append(sanitizedInputs[1]).append("', '%')");

        return query;
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
