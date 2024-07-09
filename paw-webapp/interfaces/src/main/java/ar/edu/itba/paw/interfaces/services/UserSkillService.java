package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import java.util.List;

public interface UserSkillService {

    UserSkill addSkillToUser(String skillDescription, long userId);

    boolean alreadyExists(Skill skill, User user);

    List<User> getUsersWithSkill(Skill skill);

    PaginatedResource<Skill> getSkillsForUser(String descriptionLike, long userId, int page, int pageSize);

    long getSkillCountForUser(String descriptionLike, User user);

    void deleteSkillFromUser(long userID, long skillID);
}
