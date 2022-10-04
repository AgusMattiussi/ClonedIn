package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Experience;

import java.util.List;
import java.util.Optional;

public interface ExperienceService {
    Experience create(long userId, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description);

    Optional<Experience> findById(long experienceId);

    List<Experience> findByUserId(long userID);

    void deleteExperience(long experienceId);
}
