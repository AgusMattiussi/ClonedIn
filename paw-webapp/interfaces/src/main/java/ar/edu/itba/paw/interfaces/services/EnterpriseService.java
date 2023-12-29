package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Contact;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface EnterpriseService {

    Enterprise create(String email, String name, String password, String location, Category category, String workers, Integer year, String link, String description);

    Optional<Enterprise> findByEmail(String email);

    Optional<Enterprise> findById(long enterpriseId);

    Optional<Long> getIdForEmail(String email);

    boolean enterpriseExists(String email);

    void changePassword(String email, String password);

    void updateName(long enterpriseID, String newName);

    void updateDescription(long enterpriseID, String newDescription);

    void updateWorkers(long enterpriseID, String newWorkers);

    void updateYear(long enterpriseID, Integer newYear);

    void updateLink(long enterpriseID, String newLink);

    void updateLocation(long enterpriseID, String newLocation);

    void updateCategory(long enterpriseID, Category newCategory);

    void updateEnterpriseInformation(long enterpriseID, String newName, String newDescription, String newLocation, Category newCategory, String newLink, Integer newYear, String newWorkers);

    void updateProfileImage(Enterprise enterprise, Image image);

    Optional<Image> getProfileImage(int imageId);

    Map<Long, Boolean> getUserContactMap(Set<Contact> contacts);
}
