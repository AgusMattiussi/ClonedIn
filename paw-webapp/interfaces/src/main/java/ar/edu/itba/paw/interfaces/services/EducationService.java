package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Month;
import ar.edu.itba.paw.models.utils.PaginatedResource;

import java.util.List;
import java.util.Optional;

public interface EducationService {

    Education add(long userId, String monthFromString, Integer yearFrom, String monthToString, Integer yearTo, String title,
                  String institutionName, String description);

    Optional<Education> findById(long educationID);

    PaginatedResource<Education> findByUser(long userId, int page, int pageSize);

    long getEducationCountForUser(User user);

    void deleteEducation(long educationId);
}
