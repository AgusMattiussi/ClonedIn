package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    User create(String email, String password);

    Optional<User> findByEmail(String email);

    Optional<User> findById(long userId);
}
