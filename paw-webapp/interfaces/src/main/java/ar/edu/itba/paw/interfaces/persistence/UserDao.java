package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(String email, String password, String name, String location, Category category, String currentPosition, String description, String education);

    Optional<User> findByEmail(String email);

    Optional<User> findById(long userId);

    boolean userExists(String email);

    List<User> getAllUsers();

    void changePassword(String email, String password);

    long getUsersCount();

    Integer getUsersCountByFilters(String categoryId, String location, String educationLevel);

    List<User> getVisibleUsers(int page, int pageSize);

    List<User> getVisibleUsersByCategory(Category category, int page, int pageSize);

    List<User> getVisibleUsersByNameLike(String term, int page, int pageSize);

    List<User> getVisibleUsersByLocationLike(String location, int page, int pageSize);

    List<User> getUsersListByFilters(int page, int pageSize, String categoryId, String location, String educationLevel);

    void updateName(long userID, String newName);

    void updateDescription(long userID, String newDescription);

    void updateLocation(long userID, String newLocation);

    void updateCurrentPosition(long userID, String newPosition);

    void updateCategory(long userID, Category newCategory);

    void updateEducationLevel(long userID, String newEducationLevel);

    void updateVisibility(long userID, int visibility);
    
    void updateUserProfileImage(long userId, long imageId);

}
