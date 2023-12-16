package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Visibility;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class UserHibernateDao implements UserDao {

    private static final Image DEFAULT_IMAGE = null;

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String email, String password, String name, String location, Category category, String currentPosition, String description, String education) {
        final User user = new User(email, password, name, location, category, currentPosition, description, education, Visibility.VISIBLE.getValue(), DEFAULT_IMAGE);
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        final TypedQuery<User> query = em.createQuery("SELECT u FROM User AS u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    @Override
    public Optional<Long> getIdForEmail(String email) {
        Query query = em.createQuery("SELECT u.id FROM User u WHERE u.email = :email");
        query.setParameter("email", email);
        Long id = (Long) query.getSingleResult();
        return  Optional.ofNullable(id);
    }


    @Override
    public boolean userExists(String email) {
        Query query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email");
        query.setParameter("email", email);

        return ((Long) query.getSingleResult()) > 0;
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public void changePassword(String email, String password) {
        Query query = em.createQuery("UPDATE User SET password = :password WHERE email = :email");
        query.setParameter("password", password);
        query.setParameter("email", email);
        query.executeUpdate();
    }

    @Override
    public long getUsersCount() {
        Query query = em.createQuery("SELECT COUNT(u) FROM User u");
        return (Long) query.getSingleResult();
    }


    @Override
    public List<User> getVisibleUsers(int page, int pageSize) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.visibility = :visible", User.class);
        query.setParameter("visible", Visibility.VISIBLE.getValue());

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<User> getVisibleUsersByCategory(Category category, int page, int pageSize) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.visibility = :visible AND u.category = :category", User.class);
        query.setParameter("visible", Visibility.VISIBLE.getValue());
        query.setParameter("category", category);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }


    @Override
    public List<User> getVisibleUsersByNameLike(String term, int page, int pageSize) {
        term = term.replace("_", "\\_");
        term = term.replace("%", "\\%");
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.visibility = :visible AND LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'", User.class);
        query.setParameter("visible", Visibility.VISIBLE.getValue());
        query.setParameter("term", term);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<User> getVisibleUsersByLocationLike(String location, int page, int pageSize) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.visibility = :visible AND LOWER(u.location) LIKE LOWER(CONCAT('%', :location, '%'))", User.class);
        query.setParameter("visible", Visibility.VISIBLE.getValue());
        query.setParameter("location", location);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }


    private void filterQueryAppendConditions(StringBuilder queryStringBuilder,Category category, String location, String educationLevel, String skillDescription){
        if(category != null)
            queryStringBuilder.append(" AND u.category = :category");
        if(!location.isEmpty())
            queryStringBuilder.append(" AND LOWER(u.location) LIKE LOWER(CONCAT('%', :location, '%'))");
        if(!educationLevel.isEmpty())
            queryStringBuilder.append(" AND u.education = :education");
        if(!skillDescription.isEmpty())
            queryStringBuilder.append(" AND EXISTS (SELECT usk FROM UserSkill usk JOIN usk.skill sk WHERE usk.user = u AND sk.description LIKE :skillDescription)");
    }

    private void filterQueryAppendConditions(StringBuilder queryStringBuilder, Category category, String educationLevel, String term,
                                             String location, String skillDescription, Integer minExpYears, Integer maxExpYears){
        if(category != null)
            queryStringBuilder.append(" AND u.category = :category");
        if(!educationLevel.isEmpty())
            queryStringBuilder.append(" AND u.education = :education");
        if(!location.isEmpty())
            queryStringBuilder.append(" AND LOWER(u.location) LIKE LOWER(CONCAT('%', :location, '%'))");
        if(!skillDescription.isEmpty())
            queryStringBuilder.append(" AND EXISTS (SELECT usk FROM UserSkill usk JOIN usk.skill sk WHERE usk.user = u AND sk.description LIKE :skillDescription)");
        if(!term.isEmpty()) {
            queryStringBuilder.append(" AND (EXISTS (SELECT usk1 FROM UserSkill usk1 JOIN usk1.skill sk1 WHERE usk1.user = u ")
                    .append("AND LOWER(sk1.description) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\')")
                    .append(" OR LOWER(u.location) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'")
                    .append(" OR LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\')");
        }
        if(minExpYears != null || maxExpYears != null){
            queryStringBuilder.append(" GROUP BY u.id HAVING");
            if(minExpYears != null)
                queryStringBuilder.append(" SUM(COALESCE(exp.yearTo, YEAR(CURRENT_DATE)) - COALESCE(exp.yearFrom,YEAR(CURRENT_DATE))) >= :minExpYears");
            if(minExpYears != null && maxExpYears != null)
                queryStringBuilder.append(" AND");
            if(maxExpYears != null)
                queryStringBuilder.append(" SUM(COALESCE(exp.yearTo, YEAR(CURRENT_DATE)) - COALESCE(exp.yearFrom, YEAR(CURRENT_DATE))) <= :maxExpYears");
        }
    }

    private void filterQuerySetParameters(Query query, Category category, String location, String educationLevel, String skillDescription){
        query.setParameter("visible", Visibility.VISIBLE.getValue());
        if(category != null)
            query.setParameter("category", category);
        if(!location.isEmpty())
            query.setParameter("location", location);
        if(!educationLevel.isEmpty())
            query.setParameter("education", educationLevel);
        if(!skillDescription.isEmpty())
            query.setParameter("skillDescription", skillDescription);
    }

    private void filterQuerySetParameters(Query query, Category category, String educationLevel, String term, String location,
                                          String skillDescription, Integer minExpYears, Integer maxExpYears){
        query.setParameter("visible", Visibility.VISIBLE.getValue());
        if(category != null)
            query.setParameter("category", category);
        if(!educationLevel.isEmpty())
            query.setParameter("education", educationLevel);
        if(!term.isEmpty())
            query.setParameter("term", term);
        if(!location.isEmpty())
            query.setParameter("location", location);
        if(!skillDescription.isEmpty())
            query.setParameter("skillDescription", skillDescription);
        if(minExpYears != null)
            query.setParameter("minExpYears", Long.valueOf(minExpYears));
        if(maxExpYears != null)
            query.setParameter("maxExpYears", Long.valueOf(maxExpYears));
    }

    @Override
    public List<User> getUsersListByFilters(Category category, String location, String educationLevel, String skillDescription, int page, int pageSize) {
        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT u FROM User u WHERE visibilidad = :visible");
        filterQueryAppendConditions(queryStringBuilder, category, location, educationLevel, skillDescription);

        TypedQuery<User> query = em.createQuery(queryStringBuilder.toString(), User.class);
        filterQuerySetParameters(query, category, location, educationLevel, skillDescription);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<User> getUsersListByFilters(Category category, String educationLevel, String term, Integer minExpYears, Integer maxExpYears,
                                     String location, String skillDescription, int page, int pageSize) {
        term = term.replace("_", "\\_");
        term = term.replace("%", "\\%");

        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT u FROM User u");

        if(minExpYears != null || maxExpYears != null){
            queryStringBuilder.append(" LEFT JOIN u.experiences exp");
        }

        queryStringBuilder.append(" WHERE visibilidad = :visible");
        filterQueryAppendConditions(queryStringBuilder, category, educationLevel, term, location, skillDescription, minExpYears, maxExpYears);

        TypedQuery<User> query = em.createQuery(queryStringBuilder.toString(), User.class);
        filterQuerySetParameters(query, category, educationLevel, term, location, skillDescription, minExpYears, maxExpYears);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getUsersCountByFilters(Category category, String location, String educationLevel, String skillDescription) {
        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT COUNT(u) FROM User u WHERE visibilidad = :visible");
        filterQueryAppendConditions(queryStringBuilder, category, location, educationLevel, skillDescription);

        Query query = em.createQuery(queryStringBuilder.toString());
        filterQuerySetParameters(query, category, location, educationLevel, skillDescription);

        return (Long) query.getSingleResult();
    }

    @Override
    public long getUsersCountByFilters(Category category, String educationLevel, String term, Integer minExpYears, Integer maxExpYears,
                                     String location, String skillDescription) {
        term = term.replace("_", "\\_");
        term = term.replace("%", "\\%");

        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT COUNT(DISTINCT u) FROM User u");

        if(minExpYears != null || maxExpYears != null){
            queryStringBuilder.append(" LEFT JOIN u.experiences exp");
        }

        queryStringBuilder.append(" WHERE visibilidad = :visible");
        filterQueryAppendConditions(queryStringBuilder, category, educationLevel, term, location, skillDescription, minExpYears, maxExpYears);

        Query query = em.createQuery(queryStringBuilder.toString());
        filterQuerySetParameters(query, category, educationLevel, term, location, skillDescription, minExpYears, maxExpYears);

        long result;
        try {
            result = (Long) query.getSingleResult();
        } catch (NoResultException e) {
            result = 0;
        }

        return result;
    }


    @Override
    public void updateName(long userID, String newName) {
        Query query = em.createQuery("UPDATE User SET name = :newName WHERE id = :userID");
        query.setParameter("newName", newName);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    @Override
    public void updateDescription(long userID, String newDescription) {
        Query query = em.createQuery("UPDATE User SET description = :newDescription WHERE id = :userID");
        query.setParameter("newDescription", newDescription);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    @Override
    public void updateLocation(long userID, String newLocation) {
        Query query = em.createQuery("UPDATE User SET location = :newLocation WHERE id = :userID");
        query.setParameter("newLocation", newLocation);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    @Override
    public void updateCurrentPosition(long userID, String newPosition) {
        Query query = em.createQuery("UPDATE User SET currentPosition = :newPosition WHERE id = :userID");
        query.setParameter("newPosition", newPosition);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    @Override
    public void updateCategory(long userID, Category newCategory) {
        Query query = em.createQuery("UPDATE User SET category = :newCategory WHERE id = :userID");
        query.setParameter("newCategory", newCategory);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    @Override
    public void updateEducationLevel(long userID, String newEducationLevel) {
        Query query = em.createQuery("UPDATE User SET education = :newEducationLevel WHERE id = :userID");
        query.setParameter("newEducationLevel", newEducationLevel);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    @Override
    public void updateVisibility(long userID, Visibility visibility) {
        Query query = em.createQuery("UPDATE User SET visibility = :visibility WHERE id = :userID");
        query.setParameter("visibility", visibility.getValue());
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    @Override
    public void updateUserProfileImage(long userID, Image image) {
        Query query = em.createQuery("UPDATE User SET image = :image WHERE id = :userID");
        query.setParameter("image", image);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    @Override
    public long getVisibleUsersCount() {
        Query query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.visibility = :visible");
        return (Long) query.getSingleResult();
    }
}