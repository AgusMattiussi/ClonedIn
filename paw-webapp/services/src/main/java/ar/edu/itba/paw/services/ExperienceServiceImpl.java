package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.interfaces.services.ExperienceService;
import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
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
    public Experience create(User user, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        return experienceDao.create(user, monthFrom, yearFrom, monthTo, yearTo, enterpriseName, position, description);
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
