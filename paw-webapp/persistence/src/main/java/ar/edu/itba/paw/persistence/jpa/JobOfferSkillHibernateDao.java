package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.interfaces.services.JobOfferService;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.JobOfferSkill;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.exceptions.JobOfferNotFoundException;
import ar.edu.itba.paw.models.exceptions.SkillNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Primary
@Repository
@Transactional
public class JobOfferSkillHibernateDao implements JobOfferSkillDao {

    @PersistenceContext
    private EntityManager em;

    //TODO: Cambiar para seguir filosofia JPA

    @Autowired
    private SkillDao skillDao;
    @Autowired
    private JobOfferDao jobOfferDao;

    @Override
    public void addSkillToJobOffer(Skill skill, JobOffer jobOffer) {
        JobOfferSkill jobOfferSkill = new JobOfferSkill(jobOffer, skill);
        em.persist(jobOfferSkill);
    }


    @Override
    public List<JobOffer> getJobOffersWithSkill(String skillDescription) {
        Skill skill = skillDao.findByDescription(skillDescription).orElseThrow(SkillNotFoundException::new);
        return getJobOffersWithSkill(skill);
    }

    @Override
    public List<JobOffer> getJobOffersWithSkill(long skillID) {
        Skill skill = skillDao.findById(skillID).orElseThrow(SkillNotFoundException::new);
        return getJobOffersWithSkill(skill);
    }

    @Override
    public List<JobOffer> getJobOffersWithSkill(Skill skill) {
        TypedQuery<JobOffer> query = em.createQuery("SELECT jos.jobOffer FROM JobOfferSkill AS jos WHERE jos.skill = :skill", JobOffer.class);
        query.setParameter("skill", skill);

        return query.getResultList();
    }

    @Override
    public List<Skill> getSkillsForJobOffer(long jobOfferID) {
        JobOffer jobOffer = jobOfferDao.findById(jobOfferID).orElseThrow(JobOfferNotFoundException::new);

        TypedQuery<Skill> query = em.createQuery("SELECT jos.skill FROM JobOfferSkill AS jos WHERE jos.jobOffer = :jobOffer", Skill.class);
        query.setParameter("jobOffer", jobOffer);

        return query.getResultList();
    }
}
