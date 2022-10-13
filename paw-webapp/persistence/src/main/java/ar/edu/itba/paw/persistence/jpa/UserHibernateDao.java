package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class UserHibernateDao implements UserDao {

    //FIXME: Cambiar esto
    public static final Category DEFAULT_CATEGORY = new Category(0L, "testcategory");
    public static final int DEFAULT_VISIBILITY = 1;


    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String email, String password, String name, String location, String categoryName, String currentPosition, String description, String education) {
        final User user = new User(email, password, name, location, DEFAULT_CATEGORY, currentPosition, description, education, DEFAULT_VISIBILITY, null);
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }



    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    @Override
    public boolean userExists(String email) {
        return false;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void changePassword(String email, String password) {

    }

    @Override
    public Optional<Integer> getUsersCount() {
        return Optional.empty();
    }

    @Override
    public List<User> getUsersList(int page, int pageSize) {
        return null;
    }

    @Override
    public List<User> getUsersListByCategory(int page, int pageSize, int categoryId) {
        return null;
    }

    @Override
    public List<User> getUsersListByName(int page, int pageSize, String term) {
        return null;
    }

    @Override
    public List<User> getUsersListByLocation(int page, int pageSize, String location) {
        return null;
    }

    @Override
    public List<User> getUsersListByFilters(int page, int pageSize, String categoryId, String location, String educationLevel) {
        return null;
    }

    @Override
    public void updateName(long userID, String newName) {

    }

    @Override
    public void updateDescription(long userID, String newDescription) {

    }

    @Override
    public void updateLocation(long userID, String newLocation) {

    }

    @Override
    public void updateCurrentPosition(long userID, String newPosition) {

    }

    @Override
    public void updateCategory(long userID, String newCategoryName) {

    }

    @Override
    public void updateEducationLevel(long userID, String newEducationLevel) {

    }

    @Override
    public void updateVisibility(long userID, int visibility) {

    }

    @Override
    public void updateUserProfileImage(long userId, long imageId) {

    }
}