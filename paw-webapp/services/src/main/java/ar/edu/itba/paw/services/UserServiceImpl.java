package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.User;
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
    public User create(String email, String password, String name, String location, String categoryName, String currentPosition, String description, String education) {
        Category category = categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        User user = userDao.create(email, passwordEncoder.encode(password), name, location, category, currentPosition, description, education);

        emailService.sendRegisterUserConfirmationEmail(user, LocaleContextHolder.getLocale());

        LOGGER.debug("A new user was registered under id: {}", user.getId());
        LOGGER.info("A new user was registered");

        return user;
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
    public Optional<Long> getIdForEmail(String email) {
        return userDao.getIdForEmail(email);
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
    public long getUsersCount() {
        return userDao.getUsersCount();
    }

    @Override
    public long getVisibleUsersCount() {
        return userDao.getVisibleUsersCount();
    }

    @Override
    public long getUsersCountByFilters(Category category, String location, String educationLevel, String skillDescription) {
        return userDao.getUsersCountByFilters(category, location, educationLevel, skillDescription);
    }

    @Override
    public long getUsersCountByFilters(Category category, String educationLevel, String term, Integer minExpYears, Integer maxExpYears,
                                     String location, String skillDescription) {
        return userDao.getUsersCountByFilters(category, educationLevel, term, minExpYears, maxExpYears, location, skillDescription);
    }

    @Override
    public List<User> getVisibleUsers(int page, int pageSize) {
        return userDao.getVisibleUsers(page, pageSize);
    }

    @Override
    public List<User> getVisibleUsersByCategory(Category category, int page, int pageSize) {
        return userDao.getVisibleUsersByCategory(category, page,pageSize);
    }

    @Override
    public List<User> getVisibleUsersByNameLike(String term, int page, int pageSize) {
        return userDao.getVisibleUsersByNameLike(term, page, pageSize);
    }

    @Override
    public List<User> getVisibleUsersByLocationLike(String location, int page, int pageSize) {
        return userDao.getVisibleUsersByLocationLike(location, page, pageSize);
    }

    @Override
    public List<User> getUsersListByFilters(Category category, String location, String educationLevel, String skillDescription, int page, int pageSize) {
        return userDao.getUsersListByFilters(category, location, educationLevel, skillDescription, page, pageSize);
    }

    @Override
    public PaginatedResource<User> getUsersListByFilters(String categoryName, String educationLevel, String term, Integer minExpYears, Integer maxExpYears,
                                                         String location, String skillDescription, int page, int pageSize) {

        Category category = categoryService.findByName(categoryName).orElse(null);

        List<User> users = userDao.getUsersListByFilters(category, educationLevel, term, minExpYears, maxExpYears,
                                     location, skillDescription, page-1, pageSize);

        final long userCount = this.getUsersCountByFilters(category, educationLevel, term, minExpYears, maxExpYears,
                                     location, skillDescription);
        long maxPages = userCount/pageSize + 1;

        return new PaginatedResource<>(users, page, maxPages);
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
    public void updateCategory(long userID, Category newCategory) {
        userDao.updateCategory(userID, newCategory);
    }

    @Override
    public void updateEducationLevel(long userID, String newEducationLevel) {
        userDao.updateEducationLevel(userID, newEducationLevel);
    }

    @Override
    @Transactional
    public void updateUserInformation(long userID, String newName, String newDescription, String newLocation, String newPosition,
                                      String newCategory, String newEducationLevel, Visibility newVisibility) {

        Category category = newCategory != null? categoryService.findByName(newCategory)
                .orElseThrow(() -> new CategoryNotFoundException(newCategory)) : null;

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

        if(category != null) {
            updateCategory(userID, category);
        }

        if(newEducationLevel != null && !newEducationLevel.isEmpty()) {
            updateEducationLevel(userID, newEducationLevel);
        }

        if(newVisibility != null) {
            updateVisibility(userID, newVisibility);
        }
    }

    @Override
    public void hideUserProfile(long userID) {
        updateVisibility(userID, Visibility.INVISIBLE);
    }

    @Override
    public void showUserProfile(long userID) {
        updateVisibility(userID, Visibility.VISIBLE);
    }

    private void updateVisibility(long userID, Visibility visibility) {
        userDao.updateVisibility(userID, visibility);
    }

    @Override
    @Transactional
    public void updateProfileImage(long userId, byte[] imageBytes) {
        User user = this.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Image image = imageService.uploadImage(imageBytes);

        userDao.updateUserProfileImage(user, image);
    }

    @Override
    @Transactional
    public Optional<Image> getProfileImage(long userId) {
        User user = this.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return Optional.ofNullable(user.getImage());
    }

    @Override
    public Map<Long, Boolean> getUserContactMap(Set<Contact> contacts) {
        Map<Long, Boolean> toReturn = new HashMap<>();
        for (Contact contact : contacts) {
            toReturn.putIfAbsent(contact.getJobOffer().getId(), true);
        }
        return toReturn;
    }

}
