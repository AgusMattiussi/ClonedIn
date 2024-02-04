package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Month;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@CacheConfig(cacheNames = "educations-cache")
public class EducationHibernateDao implements EducationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(key = "#result.id")
    public Education add(User user, Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo, String title, String institutionName, String description) {
        final Education education = new Education(user, monthFrom, yearFrom, monthTo, yearTo, title, institutionName, description);
        em.persist(education);
        return education;
    }

    @Override
    @Cacheable(key = "#educationID", unless = "#result == null")
    public Optional<Education> findById(long educationID) {
        return Optional.ofNullable(em.find(Education.class, educationID));
    }

    @Override
    public List<Education> findByUser(User user, int page, int pageSize) {
        TypedQuery<Education> query = em.createQuery("SELECT e FROM Education e WHERE e.user = :user", Education.class);
        query.setParameter("user", user);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getEducationCountForUser(User user) {
        Query query = em.createQuery("SELECT COUNT(e) FROM Education e WHERE e.user = :user");
        query.setParameter("user",user);

        return (Long) query.getSingleResult();
    }

    @Override
    @CacheEvict(key = "#educationId")
    public void deleteEducation(long educationId) {
        Optional<Education> toDelete = findById(educationId);
        toDelete.ifPresent(education -> em.remove(education));
    }

}
