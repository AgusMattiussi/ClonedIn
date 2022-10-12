package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class SkillJdbcDao implements SkillDao {

    private static final String SKILL_TABLE = "aptitud";

    private static final String ID = "id";

    private static final String DESCRIPTION = "descripcion";

    protected static final RowMapper<Skill> SKILL_MAPPER = ((resultSet, rowNum) ->
            new Skill(resultSet.getLong(ID),
                    resultSet.getString(DESCRIPTION)));

    private final JdbcTemplate template;

    private final SimpleJdbcInsert skillInsert;

    @Autowired
    public SkillJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.skillInsert = new SimpleJdbcInsert(ds)
                .withTableName(SKILL_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public Skill create(String description) {
        final Map<String, Object> values = new HashMap<>();
        values.put(DESCRIPTION, description.toLowerCase());

        Number skillId = skillInsert.executeAndReturnKey(values);

        return new Skill(skillId.longValue(), description.toLowerCase());
    }

    @Override
    public Optional<Skill> findById(long id) {
        return template.query("SELECT * FROM aptitud WHERE id = ?",
                new Object[]{ id }, SKILL_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Skill> findByDescription(String description) {
        return template.query("SELECT * FROM aptitud WHERE descripcion = ?",
                new Object[]{ description.toLowerCase() }, SKILL_MAPPER).stream().findFirst();
    }

    @Override
    public Skill findByDescriptionOrCreate(String description) {
        Optional<Skill> optSkill = findByDescription(description);

        if(optSkill.isPresent())
            return optSkill.get();
        return create(description);
    }

    @Override
    public List<Skill> getAllSkills() {
        return template.query("SELECT * FROM aptitud ORDER BY descripcion DESC", SKILL_MAPPER);
    }

}
