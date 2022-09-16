package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Education;

import java.sql.Date;
import java.util.Optional;

public interface EducationService {

    Education add(long userId, Date from, Date to, String title, String institutionName, String description);

    Optional<Education> findById(long educationID);

    Optional<Education> findByUserId(long userID);
}
