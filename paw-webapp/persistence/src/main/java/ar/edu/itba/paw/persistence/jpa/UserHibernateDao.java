package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
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
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class UserHibernateDao implements UserDao {

    private static final int DEFAULT_VISIBILITY = 1;
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
        final TypedQuery<User> query = em.createQuery("FROM User AS u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }


    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }


    /*TypedQuery<Long> query = em.createQuery("SELECT COUNT(u) FROM User AS u WHERE u.id = :id", Long.class);
        return query.getSingleResult() > 0;*/

    @Override
    public boolean userExists(String email) {
//        Query query = em.createNativeQuery("SELECT COUNT(*) FROM usuario WHERE email = :email", Integer.class);
//        query.setParameter("email", email);
//        return ((Integer) query.getSingleResult()) > 0;
        return findByEmail(email).isPresent();
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("FROM User u", User.class).getResultList();
    }

    //FIXME: Ojo, esta hasheada?
    @Override
    public void changePassword(String email, String password) {
        Query query = em.createQuery("UPDATE User SET password = :password WHERE email = :email");
        query.setParameter("password", password);
        query.setParameter("email", email);
        query.executeUpdate();
    }

    @Override
    public Integer getUsersCount() {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM usuario");
        BigInteger bi = (BigInteger) query.getSingleResult();
        return bi.intValue();
    }

    @Override
    public List<User> getUsersList(int page, int pageSize) {
        Query query = em.createNativeQuery("SELECT * FROM usuario WHERE visibilidad=1 OFFSET :offset LIMIT :limit", User.class);
        query.setParameter("offset", pageSize * page);
        query.setParameter("limit", pageSize);
        return (List<User>) query.getResultList();
    }

    @Override
    public List<User> getUsersListByCategory(int page, int pageSize, int categoryId) {
        Query query = em.createNativeQuery("SELECT * FROM usuario WHERE idRubro = :categoryId OFFSET :offset LIMIT :limit", User.class);
        query.setParameter("offset", pageSize * page);
        query.setParameter("limit", pageSize);
        query.setParameter("categoryId", categoryId);
        return (List<User>) query.getResultList();
    }


    @Override
    public List<User> getUsersListByName(int page, int pageSize, String name) {
        Query query = em.createNativeQuery("SELECT * FROM usuario WHERE visibilidad=1 AND nombre " +
                "ILIKE CONCAT('%', :name, '%') OFFSET :offset LIMIT :limit ", User.class);
        query.setParameter("offset", pageSize * page);
        query.setParameter("limit", pageSize);
        query.setParameter("name", name);
        return (List<User>) query.getResultList();
    }

    @Override
    public List<User> getUsersListByLocation(int page, int pageSize, String location) {
        Query query = em.createNativeQuery("SELECT * FROM usuario WHERE visibilidad=1 AND ubicacion " +
                "ILIKE CONCAT('%', :location, '%') OFFSET :offset LIMIT :limit ", User.class);
        query.setParameter("offset", pageSize * page);
        query.setParameter("limit", pageSize);
        query.setParameter("location", location);
        return (List<User>) query.getResultList();
    }

    //TODO: Una vez que funcione todo, resolver los filtros
    @Override
    public List<User> getUsersListByFilters(int page, int pageSize, String categoryId, String location, String educationLevel) {
        return getUsersList(page, pageSize);
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