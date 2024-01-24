package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.UserSkillService;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary
@Service
public class UserSkillServiceImpl implements UserSkillService {

    @Autowired
    private UserSkillDao userSkillDao;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserSkill addSkillToUser(Skill skill, User user) {
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
    public PaginatedResource<Skill> getSkillsForUser(long userId, int page, int pageSize) {
        User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        List<Skill> skills =  userSkillDao.getSkillsForUser(user, page-1, pageSize);
        long skillCount = this.getSkillCountForUser(user);
        long maxPages = skillCount/pageSize + 1;

        return new PaginatedResource<>(skills, page, maxPages);
    }

    @Override
    public long getSkillCountForUser(User user) {
        return userSkillDao.getSkillCountForUser(user);
    }

    @Override
    @Transactional
    public void deleteSkillFromUser(long userID, long skillID) {
        userSkillDao.deleteSkillFromUser(userID, skillID);
    }

    @Override
    public void deleteSkillFromUser(User user, Skill skill) {
        userSkillDao.deleteSkillFromUser(user, skill);
    }
}
