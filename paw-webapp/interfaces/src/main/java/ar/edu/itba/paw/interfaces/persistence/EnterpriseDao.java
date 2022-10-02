package ar.edu.itba.paw.interfaces.persistence;


import ar.edu.itba.paw.models.Enterprise;

import java.util.Optional;

public interface EnterpriseDao {
    Enterprise create(String email, String name, String password,  String location, String categoryName, String description);

    Optional<Enterprise> findByEmail(String email);

    Optional<Enterprise> findById(long enterpriseId);

    void changePassword(String email, String password);

    boolean enterpriseExists(String email);

    void updateName(long enterpriseID, String newName);

    void updateDescription(long enterpriseID, String newDescription);

    void updateLocation(long enterpriseID, String newLocation);

    void updateCategory(long enterpriseID, String newCategoryName);

    /* TODO:
        - findByLocation
        - findByCategory
        - findByCurrentPosition
        - findByDescription
     */
}
