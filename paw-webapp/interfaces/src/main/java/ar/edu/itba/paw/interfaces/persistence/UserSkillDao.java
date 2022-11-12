package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;

import java.util.List;

public interface UserSkillDao {

    UserSkill addSkillToUser(Skill skill, User user);

    boolean alreadyExists(Skill skill, User user);

    List<User> getUsersWithSkill(Skill skill);

    List<Skill> getSkillsForUser(User user);

    void deleteSkillFromUser(long userID, long skillID);

    void deleteSkillFromUser(User user, Skill skill);
}
