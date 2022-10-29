package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;

import java.util.Optional;

public interface EnterpriseService {
    Enterprise create(String email, String name, String password, String location, String categoryName, String description);

    Optional<Enterprise> findByEmail(String email);

    Optional<Enterprise> findById(long enterpriseId);

    boolean enterpriseExists(String email);

    void changePassword(String email, String password);

    void updateName(long enterpriseID, String newName);

    void updateDescription(long enterpriseID, String newDescription);

    void updateLocation(long enterpriseID, String newLocation);

    void updateCategory(long enterpriseID, Category newCategory);

    void updateEnterpriseInformation(long enterpriseID, String newName, String newDescription, String newLocation, Category newCategory);

    void updateProfileImage(long enterpriseID, Image image);

    Optional<Image> getProfileImage(int imageId);
}
