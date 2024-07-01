package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.enums.EmployeeRanges;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.EnterpriseNotFoundException;
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
public class EnterpriseServiceImpl implements EnterpriseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseServiceImpl.class);

    @Autowired
    private EnterpriseDao enterpriseDao;
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
    public Enterprise create(String email, String name, String password, String location, String categoryName, EmployeeRanges workers,
                             Integer year, String link, String description) {
        Category category = categoryService.findByName(categoryName).orElseThrow(() -> {
            LOGGER.error("Category with name {} was not found - create", categoryName);
            return new CategoryNotFoundException(categoryName);
        });

        Enterprise enterprise = enterpriseDao.create(email, name, passwordEncoder.encode(password), location, category,
                workers, year, link, description);

        LOGGER.debug("A new enterprise was registered under id: {}", enterprise.getId());
        LOGGER.info("A new enterprise was registered");

        emailService.sendRegisterEnterpriseConfirmationEmail(email, name, LocaleContextHolder.getLocale());
        LOGGER.debug("A registered enterprise confirmation email was sent to {}", email);

        return enterprise;
    }

    @Override
    public Optional<Enterprise> findByEmail(String email) {
        return enterpriseDao.findByEmail(email);
    }

    @Override
    @Transactional
    public Optional<Enterprise> findById(long enterpriseId) {
        return enterpriseDao.findById(enterpriseId);
    }

    @Override
    @Transactional
    public Optional<Long> getIdForEmail(String email) {
        return enterpriseDao.getIdForEmail(email);
    }

    @Override
    public boolean enterpriseExists(String email) {
        return enterpriseDao.enterpriseExists(email);
    }

    @Override
    @Transactional
    public void changePassword(String email, String password) {
        enterpriseDao.changePassword(email, passwordEncoder.encode(password));
        LOGGER.debug("Password for enterprise with email {} was changed", email);
    }

    @Override
    @Transactional
    public void updateName(long enterpriseID, String newName) {
        enterpriseDao.updateName(enterpriseID, newName);
        LOGGER.debug("Name for enterprise with id {} was changed", enterpriseID);
    }

    @Override
    @Transactional
    public void updateDescription(long enterpriseID, String newDescription) {
        enterpriseDao.updateDescription(enterpriseID, newDescription);
        LOGGER.debug("Description for enterprise with id {} was changed", enterpriseID);
    }

    @Override
    @Transactional
    public void updateWorkers(long enterpriseID, EmployeeRanges newWorkers) {
        enterpriseDao.updateWorkers(enterpriseID, newWorkers);
        LOGGER.debug("Workers for enterprise with id {} were changed", enterpriseID);
    }

    @Override
    @Transactional
    public void updateYear(long enterpriseID, Integer newYear) {
        enterpriseDao.updateYear(enterpriseID, newYear);
        LOGGER.debug("Year for enterprise with id {} was changed", enterpriseID);
    }

    @Override
    @Transactional
    public void updateLink(long enterpriseID, String newLink) {
        enterpriseDao.updateLink(enterpriseID, newLink);
        LOGGER.debug("Link for enterprise with id {} was changed", enterpriseID);
    }

    @Override
    @Transactional
    public void updateLocation(long enterpriseID, String newLocation) {
        enterpriseDao.updateLocation(enterpriseID, newLocation);
        LOGGER.debug("Location for enterprise with id {} was changed", enterpriseID);
    }

    @Override
    @Transactional
    public void updateCategory(long enterpriseID, Category category) {
        enterpriseDao.updateCategory(enterpriseID, category);
        LOGGER.debug("Category for enterprise with id {} was changed", enterpriseID);
    }

    @Override
    @Transactional
    public void updateEnterpriseInformation(long enterpriseID, String newName, String newDescription, String newLocation,
                                            String newCategory, String newLink, Integer newYear, EmployeeRanges newWorkers) {

        if(newName != null && !newName.isEmpty()) {
            updateName(enterpriseID, newName);
        }

        if(newDescription != null && !newDescription.isEmpty()) {
            updateDescription(enterpriseID, newDescription);
        }

        if(newLocation != null && !newLocation.isEmpty()) {
            updateLocation(enterpriseID, newLocation);
        }

        if(newCategory != null && !newCategory.isEmpty()) {
            Category category = categoryService.findByName(newCategory).orElseThrow(() -> {
                LOGGER.error("Category with name {} was not found - updateEnterpriseInformation", newCategory);
                return new CategoryNotFoundException(newCategory);
            });
            updateCategory(enterpriseID, category);
        }

        if(newLink != null && !newLink.isEmpty()) {
            updateLink(enterpriseID, newLink);
        }

        if(newYear != null) {
            updateYear(enterpriseID, newYear);
        }

        if(newWorkers != null) {
            updateWorkers(enterpriseID, newWorkers);
        }

    }

    @Override
    @Transactional
    public void updateProfileImage(Enterprise enterprise, Image image) {
        enterpriseDao.updateEnterpriseProfileImage(enterprise, image);
        LOGGER.debug("Profile image for enterprise with id {} was changed", enterprise.getId());
    }

    @Override
    @Transactional
    public void updateProfileImage(long enterpriseId, byte[] bytes) {
        Enterprise enterprise = this.findById(enterpriseId).orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));
        Image image = imageService.uploadImage(bytes);
        this.updateProfileImage(enterprise, image);
    }

    @Override
    @Transactional
    public Optional<Image> getProfileImage(long enterpriseId) {
        Enterprise enterprise = this.findById(enterpriseId)
                .orElseThrow(() -> new EnterpriseNotFoundException(enterpriseId));

        Image image = enterprise.getImage();
        if(image != null) {
            // This is done to avoid lazy loading exceptions
            image.getBytes();
        }

        return Optional.ofNullable(image);
    }

    @Override
    public Map<Long, Boolean> getUserContactMap(Set<Contact> contacts) {
        Map<Long, Boolean> toReturn = new HashMap<>();
        for (Contact contact : contacts) {
            toReturn.putIfAbsent(contact.getUser().getId(), true);
        }
        return toReturn;
    }

    @Override
    public PaginatedResource<Enterprise> getEnterpriseListByFilters(String categoryName, String location, EmployeeRanges workers,
                                                                    String enterpriseName, String term, int page, int pageSize) {

        Category category = categoryName != null ? categoryService.findByName(categoryName).orElseThrow(() -> {
            LOGGER.error("Category with name {} was not found - getEnterpriseListByFilters", categoryName);
            return new CategoryNotFoundException(categoryName);
        }) : null;

        List<Enterprise> enterprises = enterpriseDao.getEnterpriseListByFilters(category, location, workers, enterpriseName,
                term, page-1, pageSize);
        long enterpriseCount = this.getEnterpriseCountByFilters(category, location, workers, enterpriseName, term);
        long maxPages = (long) Math.ceil((double) enterpriseCount / pageSize);

        return new PaginatedResource<>(enterprises, page, maxPages);
    }

    @Override
    public long getEnterpriseCountByFilters(Category category, String location, EmployeeRanges workers, String enterpriseName, String term) {
        return enterpriseDao.getEnterpriseCountByFilters(category, location, workers, enterpriseName, term);
    }


}
