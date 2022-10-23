package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface UserSkillDao {

    void addSkillToUser(Skill skill, User user);

    boolean alreadyExists(String skillDescription, long userID);

    boolean alreadyExists(long skillID, long userID);

    List<User> getUsersWithSkill(String skillDescription);

    List<User> getUsersWithSkill(long skillID);

    List<Skill> getSkillsForUser(long userID);

    void deleteSkillFromUser(long userID, long skillID);
}
