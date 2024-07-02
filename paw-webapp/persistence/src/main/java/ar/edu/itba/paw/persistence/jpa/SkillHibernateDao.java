package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.Skill;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
//@CacheConfig(cacheNames = "skills-cache")
public class SkillHibernateDao implements SkillDao {

    @PersistenceContext
    private EntityManager em;

    @Override
//    @CachePut(key = "#result.id")
    public Skill create(String description) {
        final Skill skill = new Skill(description);
        em.persist(skill);
        return skill;
    }

    @Override
//    @Cacheable(key = "#id", unless = "#result == null")
    public Optional<Skill> findById(long id) {
        return Optional.ofNullable(em.find(Skill.class, id));
    }

    @Override
//    @Cacheable(key = "#description", unless = "#result == null")
    public Optional<Skill> findByDescription(String description) {
        final TypedQuery<Skill> query = em.createQuery("SELECT s FROM Skill AS s WHERE s.description = :description", Skill.class);
        query.setParameter("description", description);
        return query.getResultList().stream().findFirst();
    }

    @Override
//    @Cacheable(key = "#description")
    public Skill findByDescriptionOrCreate(String description) {
        Optional<Skill> optSkill = findByDescription(description);
        if (optSkill.isPresent())
            return optSkill.get();
        return create(description);
    }

    @Override
    public List<Skill> getAllSkills(String descriptionLike, int page, int pageSize) {
        StringBuilder queryBuilder = new StringBuilder("SELECT s FROM Skill s");

        if(descriptionLike != null && !descriptionLike.isEmpty()) {
            queryBuilder.append(" WHERE LOWER(s.description) LIKE LOWER(CONCAT('%', :description, '%')) ESCAPE '\\'");
        }

        System.out.println("\n\n\n\n\n\n" + queryBuilder + "\n\n\n\n\n\n");

        queryBuilder.append(" ORDER BY s.description ASC");

        TypedQuery<Skill> query = em.createQuery(queryBuilder.toString(), Skill.class);

        if(descriptionLike != null && !descriptionLike.isEmpty()) {
            query.setParameter("description", descriptionLike);
        }

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getSkillCount(String descriptionLike) {
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(s) FROM Skill s");

        if(descriptionLike != null && !descriptionLike.isEmpty()) {
            queryBuilder.append(" WHERE LOWER(s.description) LIKE LOWER(CONCAT('%', :description, '%')) ESCAPE '\\'");
        }

        Query query = em.createQuery(queryBuilder.toString());

        if(descriptionLike != null && !descriptionLike.isEmpty()) {
            query.setParameter("description", descriptionLike);
        }

        return (Long) query.getSingleResult();
    }
}
