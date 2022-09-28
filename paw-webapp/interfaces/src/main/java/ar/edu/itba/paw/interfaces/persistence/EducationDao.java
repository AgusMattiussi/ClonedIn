package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Education;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface EducationDao {

    Education add(long userId, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description);

    Optional<Education> findById(long educationID);

    List<Education> findByUserId(long userID);
}
