package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.interfaces.services.UserSkillService;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class UserSkillServiceImpl implements UserSkillService {

    private final UserSkillDao userSkillDao;

    @Autowired
    public UserSkillServiceImpl(UserSkillDao userSkillDao){
        this.userSkillDao = userSkillDao;
    }


    @Override
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
    public List<Skill> getSkillsForUser(User user) {
        return userSkillDao.getSkillsForUser(user);
    }

    @Override
    public void deleteSkillFromUser(long userID, long skillID) {
        userSkillDao.deleteSkillFromUser(userID, skillID);
    }

    @Override
    public void deleteSkillFromUser(User user, Skill skill) {
        userSkillDao.deleteSkillFromUser(user, skill);
    }
}
