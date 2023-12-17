package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Month;

import java.util.List;
import java.util.Optional;

public interface ExperienceService {
    Experience create(User user, Month monthFrom, Integer yearFrom, Month monthTo, Integer yearTo, String enterpriseName, String position, String description);

    Optional<Experience> findById(long experienceId);

    List<Experience> findByUser(User user);

    void deleteExperience(long experienceId);
}
