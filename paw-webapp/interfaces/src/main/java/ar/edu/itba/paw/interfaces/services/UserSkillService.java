package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;
import ar.edu.itba.paw.models.utils.PaginatedResource;

import java.util.List;

public interface UserSkillService {

    UserSkill addSkillToUser(Skill skill, User user);

    boolean alreadyExists(Skill skill, User user);

    List<User> getUsersWithSkill(Skill skill);

    PaginatedResource<Skill> getSkillsForUser(long userId, int page, int pageSize);

    long getSkillCountForUser(User user);

    void deleteSkillFromUser(long userID, long skillID);

    void deleteSkillFromUser(User user, Skill skill);

}
