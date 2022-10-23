package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.interfaces.services.UserSkillService;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
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
    public void addSkillToUser(Skill skill, User user) {
        userSkillDao.addSkillToUser(skill, user);
    }

    @Override
    public boolean alreadyExists(String skillDescription, long userID) {
        return userSkillDao.alreadyExists(skillDescription, userID);
    }

    @Override
    public boolean alreadyExists(long skillID, long userID) {
        return userSkillDao.alreadyExists(skillID, userID);
    }

    @Override
    public List<User> getUsersWithSkill(String skillDescription) {
        return userSkillDao.getUsersWithSkill(skillDescription);
    }

    @Override
    public List<User> getUsersWithSkill(long skillID) {
        return userSkillDao.getUsersWithSkill(skillID);
    }

    @Override
    public List<Skill> getSkillsForUser(long userID) {
        return userSkillDao.getSkillsForUser(userID);
    }

    @Override
    public void deleteSkillFromUser(long userID, long skillID) {
        userSkillDao.deleteSkillFromUser(userID, skillID);
    }
}
