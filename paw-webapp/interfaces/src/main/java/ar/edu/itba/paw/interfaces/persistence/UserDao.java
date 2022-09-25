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

    /* TODO:
        - findByLocation
        - findByCategory
        - findByCurrentPosition
        - findByDescription
        - findByEducation
     */
}
