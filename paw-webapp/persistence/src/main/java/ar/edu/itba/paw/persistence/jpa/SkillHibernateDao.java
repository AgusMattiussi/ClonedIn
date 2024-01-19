package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.Skill;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class SkillHibernateDao implements SkillDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Skill create(String description) {
        final Skill skill = new Skill(description);
        em.persist(skill);
        return skill;
    }

    @Override
    public Optional<Skill> findById(long id) {
        return Optional.ofNullable(em.find(Skill.class, id));
    }

    @Override
    public Optional<Skill> findByDescription(String description) {
        final TypedQuery<Skill> query = em.createQuery("SELECT s FROM Skill AS s WHERE s.description = :description", Skill.class);
        query.setParameter("description", description);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Skill findByDescriptionOrCreate(String description) {
        Optional<Skill> optSkill = findByDescription(description);
        if (optSkill.isPresent())
            return optSkill.get();
        return create(description);
    }

    @Override
    public List<Skill> getAllSkills(int page, int pageSize) {
        TypedQuery<Skill> query = em.createQuery("SELECT s FROM Skill s ORDER BY s.description ASC", Skill.class);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getSkillCount() {
        Query query = em.createQuery("SELECT COUNT(s) FROM Skill s");

        return (Long) query.getSingleResult();
    }
}
