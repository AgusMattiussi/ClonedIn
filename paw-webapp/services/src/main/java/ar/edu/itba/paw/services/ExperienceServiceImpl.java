package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.interfaces.services.ExperienceService;
import ar.edu.itba.paw.models.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    public Experience create(long userId, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        return experienceDao.create(userId, monthFrom, yearFrom, monthTo, yearTo, enterpriseName, position, description);
    }

    @Override
    public Optional<Experience> findById(long experienceId) {
        return experienceDao.findById(experienceId);
    }

    @Override
    public List<Experience> findByUserId(long userID) {
        return experienceDao.findByUserId(userID);
    }

    @Override
    public void deleteExperience(long experienceId) {
        experienceDao.deleteExperience(experienceId);
    }
}
