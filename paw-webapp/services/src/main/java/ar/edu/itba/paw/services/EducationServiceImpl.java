package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.interfaces.services.EducationService;
import ar.edu.itba.paw.models.Education;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    public Education add(long userId, Date from, Date to, String title, String institutionName, String description) {
        return educationDao.add(userId, from, to, title, institutionName, description);
    }

    @Override
    public Optional<Education> findById(long educationID) {
        return educationDao.findById(educationID);
    }

    @Override
    public List<Education> findByUserId(long userID) {
        return educationDao.findByUserId(userID);
    }
}
