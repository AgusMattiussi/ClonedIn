package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface UserSkillService {

    void addSkillToUser(Skill skill, User user);

    boolean alreadyExists(String skillDescription, long userID);

    boolean alreadyExists(long skillID, long userID);

    boolean alreadyExists(Skill skill, User user);

    List<User> getUsersWithSkill(String skillDescription);

    List<User> getUsersWithSkill(long skillID);

    List<User> getUsersWithSkill(Skill skill);

    List<Skill> getSkillsForUser(long userID);

    List<Skill> getSkillsForUser(User user);

    void deleteSkillFromUser(long userID, long skillID);

}
