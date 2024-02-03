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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class ExperienceServiceImpl implements ExperienceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExperienceServiceImpl.class);

    @Autowired
    private ExperienceDao experienceDao;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Experience create(long userId, Integer fromMonthNum, Integer yearFrom, Integer toMonthNum, Integer yearTo,
                      String enterpriseName, String position, String description) {
        Month monthFrom = Month.fromNumber(fromMonthNum);
        Month monthTo = toMonthNum != null ? Month.fromNumber(toMonthNum) : null;

        DateHelper.validateDate(monthFrom,yearFrom, monthTo, yearTo);

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - create", userId);
            return new UserNotFoundException(userId);
        });

        Experience experience = experienceDao.create(user, monthFrom.getNumber(), yearFrom, monthTo != null ? monthTo.getNumber() : null,
                yearTo, enterpriseName, position, description);

        LOGGER.debug("A new experience was registered under id: {}", experience.getId());
        LOGGER.info("A new experience was registered");

        return experience;
    }

    @Override
    public Optional<Experience> findById(long experienceId) {
        return experienceDao.findById(experienceId);
    }


    @Override
    @Transactional
    public PaginatedResource<Experience> findByUser(long userId, int page, int pageSize) {
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - findByUser", userId);
            return new UserNotFoundException(userId);
        });

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
        LOGGER.debug("Experience with id {} was deleted", experienceId);
    }
}
