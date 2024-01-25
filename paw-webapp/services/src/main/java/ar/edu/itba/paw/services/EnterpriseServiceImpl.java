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
        Category category = categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        Enterprise enterprise = enterpriseDao.create(email, name, passwordEncoder.encode(password), location, category,
                workers, year, link, description);

        emailService.sendRegisterEnterpriseConfirmationEmail(email, name, LocaleContextHolder.getLocale());
        return enterprise;
    }

    @Override
    public Optional<Enterprise> findByEmail(String email) {
        return enterpriseDao.findByEmail(email);
    }

    @Override
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
    }

    @Override
    @Transactional
    public void updateName(long enterpriseID, String newName) {
        enterpriseDao.updateName(enterpriseID, newName);
    }

    @Override
    @Transactional
    public void updateDescription(long enterpriseID, String newDescription) {
        enterpriseDao.updateDescription(enterpriseID, newDescription);
    }

    @Override
    @Transactional
    public void updateWorkers(long enterpriseID, EmployeeRanges newWorkers) {
        enterpriseDao.updateWorkers(enterpriseID, newWorkers);
    }

    @Override
    @Transactional
    public void updateYear(long enterpriseID, Integer newYear) {
        enterpriseDao.updateYear(enterpriseID, newYear);
    }

    @Override
    @Transactional
    public void updateLink(long enterpriseID, String newLink) {
        enterpriseDao.updateLink(enterpriseID, newLink);
    }

    @Override
    @Transactional
    public void updateLocation(long enterpriseID, String newLocation) {
        enterpriseDao.updateLocation(enterpriseID, newLocation);
    }

    @Override
    @Transactional
    public void updateCategory(long enterpriseID, Category category) {
        enterpriseDao.updateCategory(enterpriseID, category);
    }

    @Override
    @Transactional
    public void updateEnterpriseInformation(long enterpriseID, String newName, String newDescription, String newLocation,
                                            String newCategory, String newLink, Integer newYear, EmployeeRanges newWorkers) {


        if( newName != null && !newName.isEmpty()) {
            updateName(enterpriseID, newName);
        }

        if(newDescription != null && !newDescription.isEmpty()) {
            updateDescription(enterpriseID, newDescription);
        }

        if(newLocation != null && !newLocation.isEmpty()) {
            updateLocation(enterpriseID, newLocation);
        }

        if(newCategory != null && !newCategory.isEmpty()) {
            Category category = categoryService.findByName(newCategory)
                .orElseThrow(() -> new CategoryNotFoundException(newCategory));
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

        return Optional.ofNullable(enterprise.getImage());
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

        Category category = categoryName != null ? categoryService.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName)) : null;

        List<Enterprise> enterprises = enterpriseDao.getEnterpriseListByFilters(category, location, workers, enterpriseName,
                term, page-1, pageSize);
        long enterpriseCount = this.getEnterpriseCountByFilters(category, location, workers, enterpriseName, term);
        long maxPages = enterpriseCount / pageSize + 1;

        return new PaginatedResource<>(enterprises, page, maxPages);
    }

    @Override
    public long getEnterpriseCountByFilters(Category category, String location, EmployeeRanges workers, String enterpriseName, String term) {
        return enterpriseDao.getEnterpriseCountByFilters(category, location, workers, enterpriseName, term);
    }


}
