package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.helpers.DateHelper;
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
@Transactional
public class ExperienceHibernateDao implements ExperienceDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Experience create(User user, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        if(monthTo == null && yearTo != null || monthTo != null && yearTo == null)
            throw new InvalidParameterException(" monthTo y yearTo no pueden ser null simultaneamente");

        if(monthTo != null && yearTo != null) {
            if (!DateHelper.isDateValid(monthFrom, yearFrom, monthTo, yearTo))
                throw new InvalidParameterException("La fecha" + monthFrom + "/" + yearFrom +
                        " - " + monthTo + "/" + yearTo + " es incorrecta");
        }

        final Experience experience = new Experience(user, monthFrom, yearFrom, monthTo, yearTo, enterpriseName, position, description);
        em.persist(experience);
        return experience;
    }

    @Override
    public Optional<Experience> findById(long experienceId) {
        return Optional.ofNullable(em.find(Experience.class, experienceId));
    }

    @Override
    public List<Experience> findByUser(User user) {
        TypedQuery<Experience> query = em.createQuery("SELECT e FROM Experience e WHERE e.user = :user", Experience.class);
        query.setParameter("user", user);

        return query.getResultList();
    }

    @Override
    public void deleteExperience(long experienceId) {
        final Query query = em.createQuery("DELETE FROM Experience AS e WHERE e.id = :experienceId");
        query.setParameter("experienceId", experienceId);
        query.executeUpdate();
    }

}
