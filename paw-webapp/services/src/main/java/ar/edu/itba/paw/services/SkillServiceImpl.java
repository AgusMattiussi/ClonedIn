package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.interfaces.services.SkillService;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class SkillServiceImpl implements SkillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillServiceImpl.class);

    private final SkillDao skillDao;

    @Autowired
    public SkillServiceImpl(SkillDao skillDao){
        this.skillDao = skillDao;
    }

    @Override
    public Skill create(String description) {
        Skill skill = skillDao.create(description);
        LOGGER.debug("A new skill was registered under id: {}", skill.getId());
        LOGGER.info("A new skill was registered");
        return skill;
    }

    @Override
    public Optional<Skill> findById(long id) {
        return skillDao.findById(id);
    }

    @Override
    public Optional<Skill> findByDescription(String description) {
        return skillDao.findByDescription(description);
    }

    @Override
    public Skill findByDescriptionOrCreate(String description) {
        return skillDao.findByDescriptionOrCreate(description);
    }

    @Override
    public List<Skill> findMultipleByDescriptionOrCreate(List<String> skillDescriptions) {
        List<Skill> skills = new ArrayList<>();

        for(String skillDesc : skillDescriptions) {
            if(skillDesc != null && !skillDesc.isEmpty()) {
                Skill skill = skillDao.findByDescriptionOrCreate(skillDesc);
                skills.add(skill);
            }
        }

        return skills;
    }

    @Override
    public PaginatedResource<Skill> getAllSkills(int page, int pageSize) {
        List<Skill> skills = skillDao.getAllSkills(page-1, pageSize);
        long skillCount = this.getSkillCount();
        long maxPages = skillCount / pageSize + skillCount % pageSize;

        return new PaginatedResource<>(skills, page, maxPages);
    }

    @Override
    public long getSkillCount() {
        return skillDao.getSkillCount();
    }
}
