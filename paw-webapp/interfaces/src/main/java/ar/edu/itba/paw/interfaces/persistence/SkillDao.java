package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillDao {
    Skill create (String description);

    Optional<Skill> findById(long id);

    Optional<Skill> findByDescription(String description);

    Skill findByDescriptionOrCreate(String description);

    List<Skill> getAllSkills();

    boolean addSkillToUser(String skillDescription, long userID);
    boolean addSkillToUser(long skillID, long userID);

}
