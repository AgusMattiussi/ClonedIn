package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Experience;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ExperienceDao {
    Experience create(long userId, Date from, Date to, String enterpriseName, String position, String description);

    Optional<Experience> findById(long experienceId);

    List<Experience> findByUserId(long userID);
}
