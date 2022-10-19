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

    //FIXME: Chequear que ande el count
    @Override
    public List<JobOffer> findByEnterpriseId(long enterpriseID) {
        return em.createQuery("SELECT jo FROM JobOffer jo ", JobOffer.class).getResultList();
    }

    @Override
    public List<JobOffer> findByEnterpriseId(long enterpriseID, int page, int pageSize) {
        Query query = em.createNativeQuery("SELECT * FROM ofertaLaboral WHERE idEmpresa = :enterpriseID OFFSET :offset LIMIT :limit ", JobOffer.class);
        query.setParameter("offset", pageSize * page);
        query.setParameter("limit", pageSize);
        query.setParameter("enterpriseID", enterpriseID);
        return (List<JobOffer>) query.getResultList();
    }

    @Override
    public List<JobOffer> findActiveByEnterpriseId(long enterpriseID) {
        TypedQuery<JobOffer> query = em.createQuery("SELECT jo FROM JobOffer jo WHERE jo.available = :active", JobOffer.class);
        query.setParameter("active", JobOfferAvailability.ACTIVE.getStatus());
        return query.getResultList();
    }

    @Override
    public List<JobOffer> findActiveByEnterpriseId(long enterpriseID, int page, int pageSize) {
        Query query = em.createNativeQuery("SELECT * FROM ofertaLaboral WHERE idEmpresa = :enterpriseID AND disponible = :active " +
                "OFFSET :offset LIMIT :limit ", JobOffer.class);
        query.setParameter("offset", pageSize * page);
        query.setParameter("limit", pageSize);
        query.setParameter("enterpriseID", enterpriseID);
        query.setParameter("active", JobOfferAvailability.ACTIVE.getStatus());
        return (List<JobOffer>) query.getResultList();
    }

    @Override
    public Integer getJobOffersCountForEnterprise(long enterpriseID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM ofertaLaboral");
        BigInteger bi = (BigInteger) query.getSingleResult();
        return bi.intValue();
    }

    private void updateJobOfferAvailability(long jobOfferID, JobOfferAvailability joa){
        Query query = em.createQuery("UPDATE JobOffer SET available = :status WHERE id = :jobOfferID");
        query.setParameter("status", joa.getStatus());
        query.setParameter("jobOfferID", jobOfferID);
        query.executeUpdate();
    }

    @Override
    public void closeJobOffer(long jobOfferID) {
        updateJobOfferAvailability(jobOfferID, JobOfferAvailability.CLOSED);
        contactDao.closeJobOfferForEveryone(jobOfferID);
    }

    @Override
    public void cancelJobOffer(long jobOfferID) {
        updateJobOfferAvailability(jobOfferID, JobOfferAvailability.CANCELLED);
        contactDao.cancelJobOfferForEveryone(jobOfferID);
    }
}
