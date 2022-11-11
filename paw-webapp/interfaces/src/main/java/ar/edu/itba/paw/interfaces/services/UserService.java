package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(String email, String password, String name, String location, Category category, String currentPosition, String description, String education);

    Optional<User> findByEmail(String email);

    Optional<User> findById(long userId);

    public Optional<Long> getIdForEmail(String email);

    boolean userExists(String email);

    List<User> getAllUsers();

    void changePassword(String email, String password);

    long getUsersCount();

    long getUsersCountByFilters(Category category, String location, String educationLevel, String skillDescription);

    long getUsersCountByFilters(Category category, String educationLevel, String term);

    List<User> getVisibleUsers(int page, int pageSize);

    List<User> getVisibleUsersByCategory(Category category, int page, int pageSize);

    List<User> getVisibleUsersByNameLike(String term, int page, int pageSize);

    List<User> getVisibleUsersByLocationLike(String location, int page, int pageSize);

    List<User> getUsersListByFilters(Category category, String location, String educationLevel, String skillDescription, int page, int pageSize);

    List<User> getUsersListByFilters(Category category, String educationLevel, String term, int page, int pageSize);

    void updateName(long userID, String newName);

    void updateDescription(long userID, String newDescription);

    void updateLocation(long userID, String newLocation);

    void updateCurrentPosition(long userID, String newPosition);

    void updateCategory(long userID, Category newCategory);

    void updateEducationLevel(long userID, String newEducationLevel);

    void updateUserInformation(long userID, String newName, String newDescription, String newLocation, String newPosition,
                               Category newCategory, String newEducationLevel);

    void hideUserProfile(long userID);

    void showUserProfile(long userID);
    
    void updateProfileImage(long userId, Image image);

}
