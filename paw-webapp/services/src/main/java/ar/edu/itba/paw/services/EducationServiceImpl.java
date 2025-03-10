package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.interfaces.services.EducationService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
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
public class EducationServiceImpl implements EducationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EducationServiceImpl.class);

    @Autowired
    private EducationDao educationDao;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Education add(long userId, Integer fromMonthNum, Integer yearFrom, Integer toMonthNum, Integer yearTo, String title,
                  String institutionName, String description) {

        DateHelper.validateDate(fromMonthNum, yearFrom, toMonthNum, yearTo);

        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - add", userId);
            return new UserNotFoundException(userId);
        });

        Education education = educationDao.add(user, fromMonthNum, yearFrom, toMonthNum,
                yearTo, title, institutionName, description);

        LOGGER.debug("A new education was registered under id: {}", education.getId());
        LOGGER.info("A new education was registered");

        return education;
    }

    @Override
    public Optional<Education> findById(long educationID) {
        return educationDao.findById(educationID);
    }

    @Override
    @Transactional
    public PaginatedResource<Education> findByUser(long userId, int page, int pageSize) {
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - findByUser", userId);
            return new UserNotFoundException(userId);
        });

        List<Education> educations = educationDao.findByUser(user, page-1, pageSize);
        long educationCount = this.getEducationCountForUser(user);
        long maxPages = (long) Math.ceil((double) educationCount / pageSize);

        return new PaginatedResource<>(educations, page, maxPages);
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

    @Override
    public boolean isEducationOwner(long educationId, long userId) {
        return educationDao.isEducationOwner(educationId, userId);
    }
}
