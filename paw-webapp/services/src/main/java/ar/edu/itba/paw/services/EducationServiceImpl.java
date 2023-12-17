package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.interfaces.services.EducationService;
import ar.edu.itba.paw.models.Education;
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
public class EducationServiceImpl implements EducationService {

    private final EducationDao educationDao;

    @Autowired
    public EducationServiceImpl(EducationDao educationDao) {
        this.educationDao = educationDao;
    }

    @Override
    public Education add(User user, Month monthFrom, Integer yearFrom, Month monthTo, Integer yearTo, String title, String institutionName, String description) {
        DateHelper.validateDate(monthFrom,yearFrom, monthTo, yearTo);

        return educationDao.add(user,  monthFrom.getNumber(), yearFrom, monthTo != null ? monthTo.getNumber() : null,
                yearTo, title, institutionName, description);
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
