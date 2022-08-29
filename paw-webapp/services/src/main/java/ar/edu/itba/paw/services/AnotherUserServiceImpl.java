package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Nombre por defecto es anotherUserServiceImpl
 * Hago referencia al mismo mediante la annotation @Qualifier
 */

@Service("fooUserService")
public class AnotherUserServiceImpl implements UserService {
    @Override
    public User register(String email, String password) {
        return null;
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.empty();
    }
}
