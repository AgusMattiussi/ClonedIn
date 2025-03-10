package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.EducationLevel;
import ar.edu.itba.paw.models.enums.UserSorting;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import java.util.Optional;

public interface UserService {

    User create(String email, String password, String name, String location, String categoryName, String currentPosition,
                String description, EducationLevel education);

    void delete(long userId);

    Optional<User> findByEmail(String email);

    Optional<User> findById(long userId);

    Optional<User> findById(long userId, boolean getYearsOfExperience);

    Optional<Long> getIdForEmail(String email);

    boolean userExists(String email);

    void changePassword(String email, String password);

    long getUsersCountByFilters(Category category, EducationLevel educationLevel, String term, Integer minExpYears, Integer maxExpYears,
                                String location, Long skillId, String skillDescription);

    PaginatedResource<User> getUsersListByFilters(String categoryName, EducationLevel educationLevel, String term, Integer minExpYears,
                                                  Integer maxExpYears, String location, Long skillId, String skillDescription, UserSorting sortBy,
                                                  int page, int pageSize);

    void updateName(long userID, String newName);

    void updateDescription(long userID, String newDescription);

    void updateLocation(long userID, String newLocation);

    void updateCurrentPosition(long userID, String newPosition);

    void updateCategory(long userID, Category newCategory);

    void updateEducationLevel(long userID, EducationLevel newEducationLevel);

    void updateUserInformation(long userID, String newName, String newDescription, String newLocation, String newPosition,
                               String newCategory, EducationLevel newEducationLevel, Visibility visibility);

    void hideUserProfile(long userID);

    void showUserProfile(long userID);
    
    void updateProfileImage(long userId, byte[] image);

    Optional<Image> getProfileImage(long userId);

}
