package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.postgresql.core.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.lang.annotation.Native;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class ExperienceHibernateDao implements ExperienceDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao userDao;

    @Override
    public Experience create(User user, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        if(monthTo == null && yearTo != null || monthTo != null && yearTo == null)
            throw new InvalidParameterException(" monthTo y yearTo no pueden ser null simultaneamente");

        if(monthTo != null && yearTo != null) {
            if (!isDateValid(monthFrom, yearFrom, monthTo, yearTo))
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
    public List<Experience> findByUserId(long userID) {
        User user = userDao.findById(userID).orElseThrow(UserNotFoundException::new);
        final TypedQuery<Experience> query = em.createQuery("SELECT e FROM Experience AS e WHERE e.user = :user " +
                "ORDER BY e.yearFrom DESC, e.monthTo DESC", Experience.class);
        query.setParameter("user", user);
        return query.getResultList();
    }

    @Override
    public void deleteExperience(long experienceId) {
        final Query query = em.createQuery("DELETE FROM Experience AS e WHERE e.id = :experienceId");
        query.setParameter("experienceId", experienceId);
        query.executeUpdate();
    }

    private boolean isMonthValid(int month){
        return month >= 1 && month <= 12;
    }

    private boolean isYearValid(int year){
        return year >= 1900 && year <= 2100;
    }

    private boolean isDateValid(int monthFrom, int yearFrom, int monthTo, int yearTo){
        if(!isMonthValid(monthTo) || !isMonthValid(monthFrom) || !isYearValid(yearTo) || !isYearValid(yearFrom))
            return false;
        return yearTo > yearFrom || (yearTo == yearFrom && monthTo >= monthFrom);
    }
}
