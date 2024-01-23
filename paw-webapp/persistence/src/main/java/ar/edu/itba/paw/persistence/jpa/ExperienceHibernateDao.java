package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
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
public class ExperienceHibernateDao implements ExperienceDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Experience create(User user, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        final Experience experience = new Experience(user, monthFrom, yearFrom, monthTo, yearTo, enterpriseName, position, description);
        em.persist(experience);
        return experience;
    }

    @Override
    public Optional<Experience> findById(long experienceId) {
        return Optional.ofNullable(em.find(Experience.class, experienceId));
    }

    @Override
    public List<Experience> findByUser(User user, int page, int pageSize) {
        TypedQuery<Experience> query = em.createQuery("SELECT e FROM Experience e WHERE e.user = :user ORDER BY e.yearFrom DESC, e.monthFrom DESC", Experience.class);
        query.setParameter("user", user);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getExperienceCountForUser(User user) {
        Query query = em.createQuery("SELECT COUNT(e) FROM Experience e WHERE e.user = :user");
        query.setParameter("user", user);

        return (Long) query.getSingleResult();
    }

    @Override
    public void deleteExperience(long experienceId) {
        Optional<Experience> toDelete = findById(experienceId);
        toDelete.ifPresent(experience -> em.remove(experience));
    }

}
