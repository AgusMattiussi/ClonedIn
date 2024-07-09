package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.EducationLevel;
import ar.edu.itba.paw.models.enums.UserSorting;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Primary
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public User create(String email, String password, String name, String location, String categoryName, String currentPosition,
                       String description, EducationLevel education) {
        Category category = categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        User user = userDao.create(email, passwordEncoder.encode(password), name, location, category, currentPosition, description, education);

        emailService.sendRegisterUserConfirmationEmail(user, LocaleContextHolder.getLocale());

        LOGGER.debug("A new user was registered under id: {}", user.getId());
        LOGGER.info("A new user was registered");

        return user;
    }

    @Override
    @Transactional
    public void delete(long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - delete", userId);
            return new UserNotFoundException(userId);
        });
        userDao.delete(user);
        LOGGER.debug("User with id {} was deleted", userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    @Transactional
    public Optional<User> findById(long userId) {
        return userDao.findById(userId);
    }

    @Override
    @Transactional
    public Optional<User> findById(long userId, boolean getYearsOfExperience) {
        Optional<User> optUser = findById(userId);

        if(getYearsOfExperience) {
            // This is done to avoid lazy initialization exceptions
            optUser.ifPresent(User::getYearsOfExperience);
        }

        return optUser;
    }

    @Override
    @Transactional
    public Optional<Long> getIdForEmail(String email) {
        return userDao.getIdForEmail(email);
    }

    @Override
    public boolean userExists(String email) {
        return userDao.userExists(email);
    }

    @Override
    public void changePassword(String email, String password) {
        userDao.changePassword(email, passwordEncoder.encode(password));
        LOGGER.debug("Password for user with email {} was changed", email);
    }

    @Override
    @Transactional
    public long getUsersCountByFilters(Category category, EducationLevel educationLevel, String term, Integer minExpYears, Integer maxExpYears,
                                     String location, Long skillId, String skillDescription) {
        return userDao.getUsersCountByFilters(category, educationLevel, term, minExpYears, maxExpYears, location, skillId, skillDescription);
    }

    @Override
    @Transactional
    public PaginatedResource<User> getUsersListByFilters(String categoryName, EducationLevel educationLevel, String term, Integer minExpYears,
                                                         Integer maxExpYears, String location, Long skillId, String skillDescription, UserSorting sortBy,
                                                         int page, int pageSize) {

        Category category = categoryName != null ? categoryService.findByName(categoryName).orElseThrow(() -> {
            LOGGER.error("Category with name {} was not found - getUsersListByFilters", categoryName);
            return new CategoryNotFoundException(categoryName);
        }) : null;

        List<User> users = userDao.getUsersListByFilters(category, educationLevel, term, minExpYears, maxExpYears,
                                     location, skillId, skillDescription, sortBy, page-1, pageSize);

        // This is done to avoid lazy loading exceptions when calculating the experience years
        users.forEach(User::getYearsOfExperience);

        final long userCount = this.getUsersCountByFilters(category, educationLevel, term, minExpYears, maxExpYears,
                                     location, skillId, skillDescription);
        long maxPages = (long) Math.ceil((double) userCount / pageSize);

        return new PaginatedResource<>(users, page, maxPages);
    }

    @Override
    public void updateName(long userID, String newName) {
        userDao.updateName(userID, newName);
        LOGGER.debug("Name for user with id {} was updated", userID);
    }

    @Override
    public void updateDescription(long userID, String newDescription) {
        userDao.updateDescription(userID, newDescription);
        LOGGER.debug("Description for user with id {} was updated", userID);
    }

    @Override
    public void updateLocation(long userID, String newLocation) {
        userDao.updateLocation(userID, newLocation);
        LOGGER.debug("Location for user with id {} was updated", userID);
    }

    @Override
    public void updateCurrentPosition(long userID, String newPosition) {
        userDao.updateCurrentPosition(userID, newPosition);
        LOGGER.debug("Current position for user with id {} was updated", userID);
    }

    @Override
    public void updateCategory(long userID, Category newCategory) {
        userDao.updateCategory(userID, newCategory);
        LOGGER.debug("Category for user with id {} was updated", userID);
    }

    @Override
    public void updateEducationLevel(long userID, EducationLevel newEducationLevel) {
        userDao.updateEducationLevel(userID, newEducationLevel);
        LOGGER.debug("Education level for user with id {} was updated", userID);
    }

    @Override
    @Transactional
    public void updateUserInformation(long userID, String newName, String newDescription, String newLocation, String newPosition,
                                      String newCategory, EducationLevel newEducationLevel, Visibility newVisibility) {
        if(newName != null && !newName.isEmpty()) {
            updateName(userID, newName);
        }

        if(newDescription != null && !newDescription.isEmpty()) {
            updateDescription(userID, newDescription);
        }

        if(newLocation != null && !newLocation.isEmpty()) {
            updateLocation(userID, newLocation);
        }

        if(newPosition != null && !newPosition.isEmpty()) {
            updateCurrentPosition(userID, newPosition);
        }

        if(newCategory != null) {
            Category category = categoryService.findByName(newCategory).orElseThrow(() -> {
                LOGGER.error("Category with name {} was not found - updateUserInformation", newCategory);
                return new CategoryNotFoundException(newCategory);
            });
            updateCategory(userID, category);
        }

        if(newEducationLevel != null) {
            updateEducationLevel(userID, newEducationLevel);
        }

        if(newVisibility != null) {
            updateVisibility(userID, newVisibility);
        }
    }

    @Override
    public void hideUserProfile(long userID) {
        updateVisibility(userID, Visibility.INVISIBLE);
        LOGGER.debug("User with id {} hid their profile", userID);
    }

    @Override
    public void showUserProfile(long userID) {
        updateVisibility(userID, Visibility.VISIBLE);
        LOGGER.debug("User with id {} showed their profile", userID);
    }

    private void updateVisibility(long userID, Visibility visibility) {
        userDao.updateVisibility(userID, visibility);
        LOGGER.debug("Visibility for user with id {} was updated", userID);
    }

    @Override
    @Transactional
    public void updateProfileImage(long userId, byte[] imageBytes) {
        User user = this.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - updateProfileImage", userId);
            return new UserNotFoundException(userId);
        });
        Image image = imageService.uploadImage(imageBytes);

        userDao.updateUserProfileImage(user, image);
        LOGGER.debug("Profile image for user with id {} was updated", userId);
    }

    @Override
    @Transactional
    public Optional<Image> getProfileImage(long userId) {
        User user = this.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - getProfileImage", userId);
            return new UserNotFoundException(userId);
        });

        Image image = user.getImage();
        if(image != null) {
            // This is done to avoid lazy loading exceptions
            image.getBytes();
        }

        return Optional.ofNullable(image);
    }
}
