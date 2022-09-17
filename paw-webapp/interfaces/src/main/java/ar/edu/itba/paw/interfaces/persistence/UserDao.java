package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(String email, String password, String name, String location, String categoryName, String currentPosition, String description, String education);

    Optional<User> findByEmail(String email);

    Optional<User> findById(long userId);

    List<User> getAllUsers();

    Optional<Integer> getUsersCount();

    List<User> getUsersList(int page, int pageSize);



    /* TODO:
        - findByLocation
        - findByCategory
        - findByCurrentPosition
        - findByDescription
        - findByEducation
     */
}
