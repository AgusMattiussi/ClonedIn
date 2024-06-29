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
}
