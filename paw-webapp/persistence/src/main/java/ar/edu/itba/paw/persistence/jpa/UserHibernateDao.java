package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.ImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class UserHibernateDao implements UserDao {

    private static final int DEFAULT_VISIBILITY = 1;
    private static final int UNEXISTING_CATEGORY_ID = 99;
    private static final Image DEFAULT_IMAGE = null;

    @PersistenceContext
    private EntityManager em;
    private final CategoryDao categoryDao;
    private final ImageDao imageDao;

    @Autowired
    public UserHibernateDao(CategoryDao categoryDao, ImageDao imageDao) {
        this.categoryDao = categoryDao;
        this.imageDao = imageDao;
    }

    @Override
    public User create(String email, String password, String name, String location, Category category, String currentPosition, String description, String education) {
        final User user = new User(email, password, name, location, category, currentPosition, description, education, DEFAULT_VISIBILITY, DEFAULT_IMAGE);
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
    public boolean userExists(String email) {
        Query query = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email");
        query.setParameter("email", email);

        return ((BigDecimal) query.getSingleResult()).longValue() > 0;
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
        return ((BigDecimal) query.getSingleResult()).longValue();
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
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.visibility = :visible AND LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%'))", User.class);
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

    @Override
    public List<User> getUsersListByFilters(int page, int pageSize, String categoryId, String location, String educationLevel) {
        StringBuilder filterQuery = new StringBuilder();
        filterQuery.append("SELECT * FROM usuario WHERE visibilidad=1");

        filterQuery = buildFilterQuery(filterQuery, categoryId, location, educationLevel);
        filterQuery.append(" ORDER BY id OFFSET :offset LIMIT :limit ");

        Query query = em.createNativeQuery(filterQuery.toString(), User.class);
        query.setParameter("offset", pageSize * page);
        query.setParameter("limit", pageSize);
        return (List<User>) query.getResultList();
    }

    @Override
    public Integer getUsersCountByFilters(String categoryId, String location, String educationLevel) {
        StringBuilder filterQuery = new StringBuilder();
        filterQuery.append("SELECT COUNT(*) FROM usuario WHERE visibilidad=1");

        filterQuery = buildFilterQuery(filterQuery, categoryId, location, educationLevel);

        Query query = em.createNativeQuery(filterQuery.toString());
        BigInteger bi = (BigInteger) query.getSingleResult();
        return bi.intValue();
    }

    private StringBuilder buildFilterQuery(StringBuilder query, String categoryId, String location, String educationLevel){
        int catId;
        try {
            catId = Integer.parseInt(categoryId);
        } catch (NumberFormatException exception){
            catId = UNEXISTING_CATEGORY_ID;
        }
        Object[] sanitizedInputs = new Object[]{catId, location, educationLevel};

        if(!categoryId.isEmpty())
            query.append(" AND idRubro = '").append(sanitizedInputs[0]).append("'");

        if(!location.isEmpty())
            query.append(" AND ubicacion ILIKE CONCAT('%', '").append(sanitizedInputs[1]).append("', '%')");

        if(!educationLevel.isEmpty())
            query.append(" AND educacion ILIKE CONCAT('%', '").append(sanitizedInputs[2]).append("', '%')");

        return query;
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

    //FIXME: Cambiar a Category newCategory?
    @Override
    public void updateCategory(long userID, String newCategoryName) {
        Category category = categoryDao.findByName(newCategoryName).orElseThrow(CategoryNotFoundException::new);

        Query query = em.createQuery("UPDATE User SET category = :newCategory WHERE id = :userID");
        query.setParameter("newCategory", category);
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
    public void updateVisibility(long userID, int visibility) {
        Query query = em.createQuery("UPDATE User SET visibility = :visibility WHERE id = :userID");
        query.setParameter("visibility", visibility);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }

    // FIXME: Image o imageID?
    @Override
    public void updateUserProfileImage(long userID, long imageId) {
        Image image = imageDao.getImage(imageId).orElseThrow(ImageNotFoundException::new);

        Query query = em.createQuery("UPDATE User SET image = :image WHERE id = :userID");
        query.setParameter("image", image);
        query.setParameter("userID", userID);
        query.executeUpdate();
    }
}