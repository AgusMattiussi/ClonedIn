package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;

import java.util.List;

public interface UserSkillService {

    boolean addSkillToUser(String skillDescription, long userID);

    boolean addSkillToUser(long skillID, long userID);

    List<User> getUsersWithSkill(String skillDescription);

    List<User> getUsersWithSkill(long skillID);

    List<Skill> getSkillsForUser(long userID);

    /* TODO:
    *   - removeSkillFromUser()
    *   - hasSkill()
    * */
}
