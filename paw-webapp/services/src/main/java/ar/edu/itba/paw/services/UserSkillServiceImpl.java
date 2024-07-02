package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.interfaces.services.SkillService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.UserSkillService;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary
@Service
public class UserSkillServiceImpl implements UserSkillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSkillServiceImpl.class);

    @Autowired
    private UserSkillDao userSkillDao;
    @Autowired
    private UserService userService;
    @Autowired
    private SkillService skillService;

    @Override
    @Transactional
    public UserSkill addSkillToUser(String skillDescription, long userId) {
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - addSkillToUser", userId);
            return new UserNotFoundException(userId);
        });
        Skill skill = skillService.findByDescriptionOrCreate(skillDescription);

        if(this.alreadyExists(skill, user)) {
            LOGGER.error("User with ID={} already has skill '{}'", userId, skillDescription);
            throw new IllegalArgumentException(String.format("User with ID=%d already has skill '%s'", userId, skillDescription));
        }

        return userSkillDao.addSkillToUser(skill, user);
    }


    @Override
    public boolean alreadyExists(Skill skill, User user) {
        return userSkillDao.alreadyExists(skill, user);
    }

    @Override
    public List<User> getUsersWithSkill(Skill skill) {
        return userSkillDao.getUsersWithSkill(skill);
    }

    @Override
    @Transactional
    public PaginatedResource<Skill> getSkillsForUser(String descriptionLike, long userId, int page, int pageSize) {
        User user = userService.findById(userId).orElseThrow(() -> {
            LOGGER.error("User with id {} was not found - getSkillsForUser", userId);
            return new UserNotFoundException(userId);
        });

        List<Skill> skills =  userSkillDao.getSkillsForUser(descriptionLike, user, page-1, pageSize);
        long skillCount = this.getSkillCountForUser(descriptionLike, user);
        long maxPages = (long) Math.ceil((double) skillCount / pageSize);

        return new PaginatedResource<>(skills, page, maxPages);
    }

    @Override
    public long getSkillCountForUser(String descriptionLike, User user) {
        return userSkillDao.getSkillCountForUser(descriptionLike, user);
    }

    @Override
    @Transactional
    public void deleteSkillFromUser(long userID, long skillID) {
        userSkillDao.deleteSkillFromUser(userID, skillID);
        LOGGER.debug("Skill with id {} was deleted from user with id {}", skillID, userID);
    }

    @Override
    public void deleteSkillFromUser(User user, Skill skill) {
        userSkillDao.deleteSkillFromUser(user, skill);
        LOGGER.debug("Skill with id {} was deleted from user with id {}", skill.getId(), user.getId());
    }
}
