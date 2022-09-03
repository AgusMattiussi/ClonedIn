package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SkillJdbcDao implements SkillDao {

    private static final String SKILL_TABLE = "aptitud";

    private static final String ID = "id";

    private static final String DESCRIPTION = "descripcion";

    private static final RowMapper<Skill> SKILL_MAPPER = ((resultSet, rowNum) ->
            new Skill(resultSet.getLong(ID),
                    resultSet.getString(DESCRIPTION)));

    private final JdbcTemplate template;

    private final SimpleJdbcInsert insert;

    @Autowired
    public SkillJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(SKILL_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public Skill create(String description) {
        final Map<String, Object> values = new HashMap<>();
        values.put(DESCRIPTION, description);

        Number skillId = insert.executeAndReturnKey(values);

        return new Skill(skillId.longValue(), description);
    }

    @Override
    public Optional<Skill> findById(long id) {
        return template.query("SELECT * FROM " + SKILL_TABLE + " WHERE " + ID + " = ?",
                new Object[]{ id }, SKILL_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Skill> findByDescription(String description) {
        return template.query("SELECT * FROM " + SKILL_TABLE + " WHERE " + DESCRIPTION + " = ?",
                new Object[]{ description }, SKILL_MAPPER).stream().findFirst();
    }
}
