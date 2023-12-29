package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Primary
@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseDao enterpriseDao;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;


    @Autowired
    public EnterpriseServiceImpl(EnterpriseDao enterpriseDao, PasswordEncoder passwordEncoder, ImageService imageService) {
        this.enterpriseDao = enterpriseDao;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
    }

    @Override
    public Enterprise create(String email, String name, String password, String location, Category category, String workers, Integer year, String link, String description) {
        return enterpriseDao.create(email, name, passwordEncoder.encode(password), location, category, workers, year, link, description);
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
    public Optional<Long> getIdForEmail(String email) {
        return enterpriseDao.getIdForEmail(email);
    }

    @Override
    public boolean enterpriseExists(String email) {
        return enterpriseDao.enterpriseExists(email);
    }

    @Override
    public void changePassword(String email, String password) {
        enterpriseDao.changePassword(email, passwordEncoder.encode(password));
    }

    @Override
    public void updateName(long enterpriseID, String newName) {
        enterpriseDao.updateName(enterpriseID, newName);
    }

    @Override
    public void updateDescription(long enterpriseID, String newDescription) {
        enterpriseDao.updateDescription(enterpriseID, newDescription);
    }

    @Override
    public void updateWorkers(long enterpriseID, String newWorkers) {
        enterpriseDao.updateWorkers(enterpriseID, newWorkers);
    }

    @Override
    public void updateYear(long enterpriseID, Integer newYear) {
        enterpriseDao.updateYear(enterpriseID, newYear);
    }

    @Override
    public void updateLink(long enterpriseID, String newLink) {
        enterpriseDao.updateLink(enterpriseID, newLink);
    }

    @Override
    public void updateLocation(long enterpriseID, String newLocation) {
        enterpriseDao.updateLocation(enterpriseID, newLocation);
    }

    @Override
    public void updateCategory(long enterpriseID, Category category) {
        enterpriseDao.updateCategory(enterpriseID, category);
    }

    @Override
    public void updateEnterpriseInformation(long enterpriseID, String newName, String newDescription, String newLocation, Category newCategory, String newLink, Integer newYear, String newWorkers) {
        if(!newName.isEmpty()) {
            updateName(enterpriseID, newName);
        }

        if(!newDescription.isEmpty()) {
            updateDescription(enterpriseID, newDescription);
        }

        if(!newLocation.isEmpty()) {
            updateLocation(enterpriseID, newLocation);
        }

        if(newCategory != null) {
            updateCategory(enterpriseID, newCategory);
        }

        if(!newLink.isEmpty()) {
            updateLink(enterpriseID, newLink);
        }

        if(newYear != null) {
            updateYear(enterpriseID, newYear);
        }

        if(!newWorkers.isEmpty()) {
            updateWorkers(enterpriseID, newWorkers);
        }

    }

    @Override
    public void updateProfileImage(Enterprise enterprise, Image image) {
        enterpriseDao.updateEnterpriseProfileImage(enterprise, image);
    }

    @Override
    public Optional<Image> getProfileImage(int imageId) {
        if(imageId == 1) {
            return imageService.getImage(1);
        }
        return imageService.getImage(imageId);
    }

    @Override
    public Map<Long, Boolean> getUserContactMap(Set<Contact> contacts) {
        Map<Long, Boolean> toReturn = new HashMap<>();
        for (Contact contact : contacts) {
            toReturn.putIfAbsent(contact.getUser().getId(), true);
        }
        return toReturn;
    }
}
