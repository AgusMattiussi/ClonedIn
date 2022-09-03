package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Skill;

import java.util.Optional;

public interface SkillService {
    Skill create (String description);

    Optional<Skill> findById(long id);

    Optional<Skill> findByDescription(String description);
}
