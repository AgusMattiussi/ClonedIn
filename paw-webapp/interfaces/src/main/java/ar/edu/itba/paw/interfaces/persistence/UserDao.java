package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.EducationLevel;
import ar.edu.itba.paw.models.enums.UserSorting;
import ar.edu.itba.paw.models.enums.Visibility;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(String email, String password, String name, String location, Category category, String currentPosition,
                String description, EducationLevel education);

    void delete(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(long userId);

    Optional<Long> getIdForEmail(String email);

    boolean userExists(String email);

    List<User> getAllUsers();

    void changePassword(String email, String password);

    long getUsersCount();

    long getUsersCountByFilters(Category category, String location, EducationLevel educationLevel, String skillDescription);

    long getUsersCountByFilters(Category category, EducationLevel educationLevel, String term, Integer minExpYears, Integer maxExpYears,
                                     String location, String skillDescription);

    List<User> getVisibleUsers(int page, int pageSize);

    List<User> getVisibleUsersByCategory(Category category, int page, int pageSize);

    List<User> getVisibleUsersByNameLike(String term, int page, int pageSize);

    List<User> getVisibleUsersByLocationLike(String location, int page, int pageSize);

    List<User> getUsersListByFilters(Category category, String location, EducationLevel educationLevel, String skillDescription, int page, int pageSize);

    List<User> getUsersListByFilters(Category category, EducationLevel educationLevel, String term, Integer minExpYears, Integer maxExpYears,
                                     String location, String skillDescription, UserSorting sortBy, int page, int pageSize);

    void updateName(long userID, String newName);

    void updateDescription(long userID, String newDescription);

    void updateLocation(long userID, String newLocation);

    void updateCurrentPosition(long userID, String newPosition);

    void updateCategory(long userID, Category newCategory);

    void updateEducationLevel(long userID, EducationLevel newEducationLevel);

    void updateVisibility(long userID, Visibility visibility);

    //TODO: warning return never used
    long updateUserProfileImage(User user, Image image);

    long getVisibleUsersCount();
}
