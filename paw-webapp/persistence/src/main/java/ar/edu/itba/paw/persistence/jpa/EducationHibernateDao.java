package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
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
public class EducationHibernateDao implements EducationDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Education add(User user, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description) {
        final Education education = new Education(user, monthFrom, yearFrom, monthTo, yearTo, title, institutionName, description);
        em.persist(education);
        return education;
    }

    @Override
    public Optional<Education> findById(long educationID) {
        return Optional.ofNullable(em.find(Education.class, educationID));
    }

    @Override
    public List<Education> findByUser(User user) {
        TypedQuery<Education> query = em.createQuery("SELECT e FROM Education e WHERE e.user = :user", Education.class);
        query.setParameter("user", user);

        return query.getResultList();
    }

    @Override
    public void deleteEducation(long educationId) {
        Optional<Education> toDelete = findById(educationId);
        toDelete.ifPresent(education -> em.remove(education));
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
