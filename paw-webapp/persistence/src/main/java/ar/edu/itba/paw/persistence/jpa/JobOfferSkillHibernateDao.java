package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.JobOfferSkill;
import ar.edu.itba.paw.models.Skill;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Primary
@Repository
public class JobOfferSkillHibernateDao implements JobOfferSkillDao {

    @PersistenceContext
    private EntityManager em;

    @Override
//    @CacheEvict(value = "jobOffers-cache", key = "#jobOffer.id")
    public void addSkillToJobOffer(Skill skill, JobOffer jobOffer) {
        JobOfferSkill jobOfferSkill = new JobOfferSkill(jobOffer, skill);
        em.persist(jobOfferSkill);
    }

    @Override
    public List<JobOffer> getJobOffersWithSkill(Skill skill) {
        TypedQuery<JobOffer> query = em.createQuery("SELECT jos.jobOffer FROM JobOfferSkill AS jos WHERE jos.skill = :skill", JobOffer.class);
        query.setParameter("skill", skill);

        return query.getResultList();
    }

    @Override
    public List<Skill> getSkillsForJobOffer(JobOffer jobOffer) {
        TypedQuery<Skill> query = em.createQuery("SELECT jos.skill FROM JobOfferSkill AS jos WHERE jos.jobOffer = :jobOffer", Skill.class);
        query.setParameter("jobOffer", jobOffer);

        return query.getResultList();
    }

    @Override
    public List<Skill> getSkillsForJobOffer(String descriptionLike, JobOffer jobOffer, int page, int pageSize) {
        StringBuilder queryBuilder = new StringBuilder("SELECT jos.skill FROM JobOfferSkill AS jos WHERE jos.jobOffer = :jobOffer");

        if(descriptionLike != null && !descriptionLike.isEmpty()) {
            queryBuilder.append(" AND LOWER(jos.skill.description) LIKE LOWER(CONCAT('%', :description, '%')) ESCAPE '\\'");
        }

        TypedQuery<Skill> query = em.createQuery(queryBuilder.toString(), Skill.class);
        query.setParameter("jobOffer", jobOffer);

        if(descriptionLike != null && !descriptionLike.isEmpty()) {
            query.setParameter("description", descriptionLike);
        }

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getSkillCountForJobOffer(String descriptionLike, JobOffer jobOffer) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(jos) FROM JobOfferSkill AS jos WHERE jos.jobOffer = :jobOffer");

        if(descriptionLike != null && !descriptionLike.isEmpty()) {
            queryBuilder.append(" AND LOWER(jos.skill.description) LIKE LOWER(CONCAT('%', :description, '%')) ESCAPE '\\'");
        }

        Query query = em.createQuery(queryBuilder.toString());
        query.setParameter("jobOffer", jobOffer);

        if(descriptionLike != null && !descriptionLike.isEmpty()) {
            query.setParameter("description", descriptionLike);
        }

        return (Long) query.getSingleResult();
    }

}
