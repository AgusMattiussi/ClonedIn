package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.interfaces.services.EducationService;
import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Month;
import ar.edu.itba.paw.models.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Education add(User user, String monthFromString, Integer yearFrom, String monthToString, Integer yearTo, String title, String institutionName, String description) {
        Month monthFrom = Month.fromString(monthFromString);
        Month monthTo = Month.fromString(monthToString);

        DateHelper.validateDate(monthFrom, yearFrom, monthTo, yearTo);

        return educationDao.add(user, monthFrom.getNumber(), yearFrom, monthTo.getNumber(),
                yearTo, title, institutionName, description);
    }

    @Override
    public Optional<Education> findById(long educationID) {
        return educationDao.findById(educationID);
    }

    @Override
    public List<Education> findByUser(User user, int page, int pageSize) {
        return educationDao.findByUser(user, page, pageSize);
    }

    @Override
    public long getEducationCountForUser(User user) {
        return educationDao.getEducationCountForUser(user);
    }


    @Override
    @Transactional
    public void deleteEducation(long educationId) {
        educationDao.deleteEducation(educationId);
    }
}
