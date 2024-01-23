package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Month;
import ar.edu.itba.paw.models.utils.PaginatedResource;

import java.util.List;
import java.util.Optional;

public interface ExperienceService {
    Experience create(User user, String monthFromString, Integer yearFrom, String monthToString, Integer yearTo, String enterpriseName, String position, String description);

    Optional<Experience> findById(long experienceId);

    PaginatedResource<Experience> findByUser(long userId, int page, int pageSize);

    long getExperienceCountForUser(User user);

    void deleteExperience(long experienceId);
}
