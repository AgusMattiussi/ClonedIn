package ar.edu.itba.paw.interfaces.persistence;


import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.enums.EmployeeRanges;

import java.util.List;
import java.util.Optional;

public interface EnterpriseDao {
    Enterprise create(String email, String name, String password,  String location, Category category, EmployeeRanges workers,
                      Integer year, String link, String description);

    Optional<Enterprise> findByEmail(String email);

    Optional<Enterprise> findById(long enterpriseId);

    Optional<Long> getIdForEmail(String email);

    void changePassword(String email, String password);

    boolean enterpriseExists(String email);

    void updateName(long enterpriseID, String newName);

    void updateWorkers(long enterpriseID, EmployeeRanges newWorkers);

    void updateYear(long enterpriseID, Integer newYear);

    void updateLink(long enterpriseID, String newLink);

    void updateDescription(long enterpriseID, String newDescription);

    void updateLocation(long enterpriseID, String newLocation);

    void updateCategory(long enterpriseID, Category newCategory);

    void updateEnterpriseProfileImage(Enterprise enterprise, Image image);

    List<Enterprise> getEnterpriseListByFilters(Category category, String location, EmployeeRanges workers, String enterpriseName,
                                                String term, int page, int pageSize);
}
