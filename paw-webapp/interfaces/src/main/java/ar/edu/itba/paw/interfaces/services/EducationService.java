package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Month;

import java.util.List;
import java.util.Optional;

public interface EducationService {

    Education add(User user, Month monthFrom, Integer yearFrom, Month monthTo, Integer yearTo, String title,
                  String institutionName, String description);

    Optional<Education> findById(long educationID);

    List<Education> findByUser(User user, int page, int pageSize);

    long getEducationCountForUser(User user);

    void deleteEducation(long educationId);
}
