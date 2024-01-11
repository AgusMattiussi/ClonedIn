package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.interfaces.services.ExperienceService;
import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Month;
import ar.edu.itba.paw.models.helpers.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class ExperienceServiceImpl implements ExperienceService {
    private final ExperienceDao experienceDao;

    @Autowired
    public ExperienceServiceImpl(ExperienceDao experienceDao) {
        this.experienceDao = experienceDao;
    }

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
    public List<Experience> findByUser(User user, int page, int pageSize) {
        return experienceDao.findByUser(user, page, pageSize);
    }

    @Override
    public long getExperienceCountForUser(User user) {
        return experienceDao.getExperienceCountForUser(user);
    }

    @Override
    public void deleteExperience(long experienceId) {
        experienceDao.deleteExperience(experienceId);
    }
}
