package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface EducationService {

    Education add(User user, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description);

    Optional<Education> findById(long educationID);

    List<Education> findByUserId(long userID);

    void deleteEducation(long educationId);
}
