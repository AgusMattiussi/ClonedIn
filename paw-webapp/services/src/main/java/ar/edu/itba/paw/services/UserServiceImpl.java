package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Image;
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
    private final ImageService imageService;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, final ImageService imageService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
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

    @Override
    public List<User> getUsersListByLocation(int page, int pageSize, String location) {
        return userDao.getUsersListByLocation(page, pageSize, location);
    }

    @Override
    public List<User> getUsersListByFilters(int page, int pageSize, String categoryId, String location, String educationLevel) {
        return userDao.getUsersListByFilters(page, pageSize, categoryId, location, educationLevel);
    }

    @Override
    public void updateName(long userID, String newName) {
        userDao.updateName(userID, newName);
    }

    @Override
    public void updateDescription(long userID, String newDescription) {
        userDao.updateDescription(userID, newDescription);
    }

    @Override
    public void updateLocation(long userID, String newLocation) {
        userDao.updateLocation(userID, newLocation);
    }

    @Override
    public void updateCurrentPosition(long userID, String newPosition) {
        userDao.updateCurrentPosition(userID, newPosition);
    }

    @Override
    public void updateCategory(long userID, String newCategoryName) {
        userDao.updateCategory(userID, newCategoryName);
    }

    @Override
    public void updateEducationLevel(long userID, String newEducationLevel) {
        userDao.updateEducationLevel(userID, newEducationLevel);
    }

    @Override
    public void updateUserInformation(User user, String newName, String newDescription, String newLocation, String newPosition,
                                      String newCategoryName, String newEducationLevel) {
        long userID = user.getId();
        updateName(userID, newName.isEmpty()? user.getName() : newName);
        updateDescription(userID, newDescription.isEmpty()? user.getDescription() : newDescription);
        updateLocation(userID, newLocation.isEmpty()? user.getLocation() : newLocation);
        updateCurrentPosition(userID, newPosition.isEmpty()? user.getCurrentPosition() : newPosition);
        updateCategory(userID, newCategoryName.isEmpty()? user.getCategory().getName() : newCategoryName);
        updateEducationLevel(userID, newEducationLevel.isEmpty()? user.getEducation() : newEducationLevel);
    }

    @Override
    public void updateProfileImage(long userId, byte[] imageBytes) {
        Image newImage = imageService.uploadImage(imageBytes);
        userDao.updateUserProfileImage(userId, newImage.getId());
    }

    @Override
    public Optional<Image> getProfileImage(int imageId) {
        if(imageId == 1) {
            return imageService.getImage(1);
        }
        return imageService.getImage(imageId);
    }
}
