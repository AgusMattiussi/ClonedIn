package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.interfaces.services.ExperienceService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Month;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.utils.DateHelper;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class ExperienceServiceImpl implements ExperienceService {

    @Autowired
    private ExperienceDao experienceDao;
    @Autowired
    private UserService userService;

    @Override
    public Experience create(User user, String monthFromString, Integer yearFrom, String monthToString, Integer yearTo, String enterpriseName, String position, String description) {
        Month monthFrom = Month.fromString(monthFromString);
        Month monthTo = Month.fromString(monthToString);

        DateHelper.validateDate(monthFrom,yearFrom, monthTo, yearTo);

        return experienceDao.create(user, monthFrom.getNumber(), yearFrom, monthTo != null ? monthTo.getNumber() : null,
                yearTo, enterpriseName, position, description);
    }

    @Override
    public Optional<Experience> findById(long experienceId) {
        return experienceDao.findById(experienceId);
    }


    @Override
    @Transactional
    public PaginatedResource<Experience> findByUser(long userId, int page, int pageSize) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<Experience> experiences = experienceDao.findByUser(user, page - 1, pageSize);
        long experienceCount = this.getExperienceCountForUser(user);
        long maxPages = experienceCount/pageSize + 1;

        return new PaginatedResource<>(experiences, page, maxPages);
    }

    @Override
    public long getExperienceCountForUser(User user) {
        return experienceDao.getExperienceCountForUser(user);
    }

    @Override
    @Transactional
    public void deleteExperience(long experienceId) {
        experienceDao.deleteExperience(experienceId);
    }
}
