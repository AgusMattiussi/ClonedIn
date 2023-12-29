package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface EducationDao {

    Education add(User user, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description);

    Optional<Education> findById(long educationID);

    List<Education> findByUser(User user, int page, int pageSize);

    long getEducationCountForUser(User user);

    void deleteEducation(long educationId);

}
