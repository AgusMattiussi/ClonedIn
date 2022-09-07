package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.models.Experience;

import java.sql.Date;
import java.util.Optional;

public interface ExperienceService {
    Experience create(long userId, Date from, Date to, String enterpriseName, String position, String description);

    Optional<Experience> findById(long experienceId);

    //TODO: Cambiar a List<Experience>
    Optional<Experience> findByUserId(long userID);
}
