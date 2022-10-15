package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class EducationHibernateDao implements EducationDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Education add(User user, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description) {
        if(!isDateValid(monthFrom, yearFrom, monthTo, yearTo))
            throw new InvalidParameterException("La fecha" + monthFrom+ "/" + yearFrom +
                    " - " + monthTo + "/" + yearTo +  " es incorrecta");

        final Education education = new Education(user, monthFrom, yearFrom, monthTo, yearTo, title, institutionName, description);
        em.persist(education);
        return education;
    }

    @Override
    public Optional<Education> findById(long educationID) {
        return Optional.ofNullable(em.find(Education.class, educationID));
    }

    @Override
    public List<Education> findByUserId(long userID) {
        final TypedQuery<Education> query = em.createQuery("FROM Education AS e WHERE e.idUsuario = :userID " +
                "ORDER BY e.anioDesde DESC, e.mesDesde DESC", Education.class);
        query.setParameter("userID", userID);
        return query.getResultList();
    }

    @Override
    public void deleteEducation(long educationId) {
        final Query query = em.createQuery("DELETE FROM Education AS e WHERE e.id = :educationId");
        query.setParameter("educationId", educationId);
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
