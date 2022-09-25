package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String email, String password, String name, String location, String categoryName, String currentPosition, String description, String education) {
        return userDao.create(email, passwordEncoder.encode(password), name, location, categoryName, currentPosition, description, education);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public Optional<User> findById(long userId) {
        return userDao.findById(userId);
    }

    @Override
    public boolean userExists(String email) {
        return userDao.userExists(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void changePassword(String email, String password) {
        userDao.changePassword(email, passwordEncoder.encode(password));
    }

    @Override
    public Optional<Integer> getUsersCount() {
        return userDao.getUsersCount();
    }

    @Override
    public List<User> getUsersList(int page, int pageSize) {
        return userDao.getUsersList(page, pageSize);
    }

    @Override
    public List<User> getUsersListByCategory(int page, int pageSize, int categoryId) {
        return userDao.getUsersListByCategory(page, pageSize, categoryId);
    }

    @Override
    public List<User> getUsersListByName(int page, int pageSize, String term) {
        return userDao.getUsersListByName(page, pageSize, term);
    }
}
