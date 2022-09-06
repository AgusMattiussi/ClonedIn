package ar.edu.itba.paw.persistence;

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
    private static final String USER_SKILL_TABLE = "aptitudUsuario";

    private static final String ID = "id";
    private static final String SKILL_ID = "idAptitud";
    private static final String USER_ID = "idUsuario";

    private static final String DESCRIPTION = "descripcion";

    private static final RowMapper<Skill> SKILL_MAPPER = ((resultSet, rowNum) ->
            new Skill(resultSet.getLong(ID),
                    resultSet.getString(DESCRIPTION)));

    private final JdbcTemplate template;

    private final SimpleJdbcInsert skillInsert;
    private final SimpleJdbcInsert userSkillInsert;

    @Autowired
    public SkillJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.skillInsert = new SimpleJdbcInsert(ds)
                .withTableName(SKILL_TABLE)
                .usingGeneratedKeyColumns(ID);
        this.userSkillInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_SKILL_TABLE);
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
        return template.query("SELECT * FROM " + SKILL_TABLE + " WHERE " + ID + " = ?",
                new Object[]{ id }, SKILL_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Skill> findByDescription(String description) {
        return template.query("SELECT * FROM " + SKILL_TABLE + " WHERE " + DESCRIPTION + " = ?",
                new Object[]{ description.toLowerCase() }, SKILL_MAPPER).stream().findFirst();
    }

    @Override
    public Skill findByDescriptionOrCreate(String description) {
        Optional<Skill> optSkill = findByDescription(description);
        return optSkill.orElse(create(description));
    }

    @Override
    public List<Skill> getAllSkills() {
        List<Skill> allSkills = template.query("SELECT * FROM " + SKILL_TABLE, SKILL_MAPPER);
        // Fixme: Es necesario?
        if(allSkills == null)
            return new ArrayList<>();
        return allSkills;
    }

    @Override
    public boolean addSkillToUser(String skillDescription, long userID) {
        Skill skill = findByDescriptionOrCreate(skillDescription);
        return addSkillToUser(skill.getId(), userID);
    }

    @Override
    public boolean addSkillToUser(long skillID, long userID) {
        final Map<String, Object> values = new HashMap<>();
        values.put(SKILL_ID, skillID);
        values.put(USER_ID, userID);

        return userSkillInsert.execute(values) > 0;
    }
}
