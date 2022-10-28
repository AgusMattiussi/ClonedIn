package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;

import java.util.Optional;

public interface EnterpriseService {

    Enterprise create(String email, String name, String password, String location, String categoryName, String workers, Integer year, String link, String description);

    Optional<Enterprise> findByEmail(String email);

    Optional<Enterprise> findById(long enterpriseId);

    boolean enterpriseExists(String email);

    void changePassword(String email, String password);

    void updateName(long enterpriseID, String newName);

    void updateDescription(long enterpriseID, String newDescription);

    void updateWorkers(long enterpriseID, String newWorkers);

    void updateYear(long enterpriseID, Integer newYear);

    void updateLink(long enterpriseID, String newLink);

    void updateLocation(long enterpriseID, String newLocation);

    void updateCategory(long enterpriseID, String newCategoryName);

    void updateEnterpriseInformation(Enterprise enterprise, String newName, String newDescription, String newLocation, String newCategoryName);

    void updateProfileImage(long enterpriseID, byte[] imageId);

    Optional<Image> getProfileImage(int imageId);
}
