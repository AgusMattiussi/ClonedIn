package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.interfaces.services.EducationService;
import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class EducationServiceImpl implements EducationService {

    private final EducationDao educationDao;

    @Autowired
    public EducationServiceImpl(EducationDao educationDao) {
        this.educationDao = educationDao;
    }

    @Override
    public Education add(User user, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description) {
        return educationDao.add(user, monthFrom, yearFrom, monthTo, yearTo, title, institutionName, description);
    }

    @Override
    public Optional<Education> findById(long educationID) {
        return educationDao.findById(educationID);
    }

    @Override
    public List<Education> findByUser(User user) {
        return educationDao.findByUser(user);
    }

    @Override
    public void deleteEducation(long educationId) {
        educationDao.deleteEducation(educationId);
    }
}
