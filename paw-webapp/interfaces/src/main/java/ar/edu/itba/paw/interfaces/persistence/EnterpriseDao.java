package ar.edu.itba.paw.interfaces.persistence;


import ar.edu.itba.paw.models.Enterprise;

import java.util.Optional;

public interface EnterpriseDao {
    Enterprise create(String email, String name, String password,  String location, long categoryId_fk, String description);

    Optional<Enterprise> findByEmail(String email);

    Optional<Enterprise> findById(long enterpriseId);

    void changePassword(String email, String password);

    /* TODO:
        - findByLocation
        - findByCategory
        - findByCurrentPosition
        - findByDescription
     */
}
