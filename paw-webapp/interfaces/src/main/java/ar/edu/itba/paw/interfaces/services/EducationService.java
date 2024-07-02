package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import java.util.Optional;

public interface EducationService {

    Education add(long userId, Integer fromMonthNum, Integer yearFrom, Integer toMonthNum, Integer yearTo, String title,
                  String institutionName, String description);

    Optional<Education> findById(long educationID);

    PaginatedResource<Education> findByUser(long userId, int page, int pageSize);

    long getEducationCountForUser(User user);

    void deleteEducation(long educationId);

    boolean isEducationOwner(long educationId, long userId);
}
