package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Enterprise create(String email, String name, String password, String location, String categoryName, String workers, Integer year, String link, String description) {
        return enterpriseDao.create(email, name, passwordEncoder.encode(password), location, categoryName, workers, year, link, description);
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
    public void updateCategory(long enterpriseID, String newCategoryName) {
        enterpriseDao.updateCategory(enterpriseID, newCategoryName);
    }

    @Override
    public void updateEnterpriseInformation(Enterprise enterprise, String newName, String newDescription, String newLocation, String newCategoryName) {
        long enterpriseID = enterprise.getId();
        updateName(enterpriseID, newName.isEmpty()? enterprise.getName() : newName);
        updateDescription(enterpriseID, newDescription.isEmpty()? enterprise.getDescription() : newDescription);
        updateLocation(enterpriseID, newLocation.isEmpty()? enterprise.getLocation() : newLocation);
        updateCategory(enterpriseID, newCategoryName.isEmpty()? enterprise.getCategory().getName() : newCategoryName);
    }

    @Override
    public void updateProfileImage(long enterpriseID, byte[] imageBytes) {
        Image newImage = imageService.uploadImage(imageBytes);
        enterpriseDao.updateEnterpriseProfileImage(enterpriseID, newImage.getId());
    }

    @Override
    public Optional<Image> getProfileImage(int imageId) {
        if(imageId == 1) {
            return imageService.getImage(1);
        }
        return imageService.getImage(imageId);
    }
}
