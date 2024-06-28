package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
import java.util.List;
import java.util.Optional;

public interface ExperienceDao {

    Experience create(User user, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description);

    Optional<Experience> findById(long experienceId);

    List<Experience> findByUser(User user, int page, int pageSize);

    long getExperienceCountForUser(User user);

    void deleteExperience(long experienceId);
}
