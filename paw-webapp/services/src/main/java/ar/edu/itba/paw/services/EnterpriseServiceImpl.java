package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.services.EnterpriseService;
import ar.edu.itba.paw.models.Enterprise;
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

    @Autowired
    public EnterpriseServiceImpl(EnterpriseDao enterpriseDao, PasswordEncoder passwordEncoder) {
        this.enterpriseDao = enterpriseDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Enterprise create(String email, String name, String password, String location, String categoryName, String description) {
        return enterpriseDao.create(email, name, password, location, categoryName, description);
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
}
