package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.utils.PaginatedResource;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    Skill create (String description);

    Optional<Skill> findById(long id);

    Optional<Skill> findByDescription(String description);

    Skill findByDescriptionOrCreate(String description);

    List<Skill> findMultipleByDescriptionOrCreate(List<String> description);

    PaginatedResource<Skill> getAllSkills(int page, int pageSize);

    long getSkillCount();

}
