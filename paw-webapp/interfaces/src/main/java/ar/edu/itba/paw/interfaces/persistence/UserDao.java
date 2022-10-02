package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(String email, String password, String name, String location, String categoryName, String currentPosition, String description, String education);

    Optional<User> findByEmail(String email);

    Optional<User> findById(long userId);

    boolean userExists(String email);

    List<User> getAllUsers();

    void changePassword(String email, String password);

    Optional<Integer> getUsersCount();

    List<User> getUsersList(int page, int pageSize);

    List<User> getUsersListByCategory(int page, int pageSize, int categoryId);

    List<User> getUsersListByName(int page, int pageSize, String term);

    List<User> getUsersListByLocation(int page, int pageSize, String location);

    List<User> getUsersListByFilters(int page, int pageSize, String categoryId, String location, String educationLevel);

    void updateName(long userID, String newName);

    /*void updateDescription(long userID, String newDescription);

    void updateLocation(long userID, String newLocation);

    void updateCurrentPosition(long userID, String newPosition);*/



    /* TODO:
        - findByLocation
        - findByCategory
        - findByCurrentPosition
        - findByDescription
        - findByEducation
     */
}
