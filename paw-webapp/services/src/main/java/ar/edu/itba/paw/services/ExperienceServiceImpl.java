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
    public Experience create(User user, Month monthFrom, Integer yearFrom, Month monthTo, Integer yearTo, String enterpriseName, String position, String description) {

        DateHelper.validateDate(monthFrom,yearFrom, monthTo, yearTo);

        return experienceDao.create(user, monthFrom.getNumber(), yearFrom, monthTo != null ? monthTo.getNumber() : null,
                yearTo, enterpriseName, position, description);
    }

    @Override
    public Optional<Experience> findById(long experienceId) {
        return experienceDao.findById(experienceId);
    }


    @Override
    public List<Experience> findByUser(User user) {
        return experienceDao.findByUser(user);
    }

    @Override
    public void deleteExperience(long experienceId) {
        experienceDao.deleteExperience(experienceId);
    }
}
