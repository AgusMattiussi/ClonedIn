package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.models.Experience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ExperienceJdbcDao implements ExperienceDao {
    private static final String EXPERIENCE_TABLE = "experiencia";
    private static final String ID = "id";
    private static final String USER_ID = "idUsuario";
    private static final String FROM = "fechaDesde";
    private static final String TO = "fechaHasta";
    private static final String ENTERPRISE_NAME = "empresa";
    private static final String POSITION = "posicion";
    private static final String DESCRIPTION = "descripcion";

    private static final RowMapper<Experience> EXPERIENCE_MAPPER = (resultSet, rowNum) ->
            new Experience(resultSet.getLong(ID),
                    resultSet.getLong(USER_ID),
                    resultSet.getDate(FROM),
                    resultSet.getDate(TO),
                    resultSet.getString(ENTERPRISE_NAME),
                    resultSet.getString(POSITION),
                    resultSet.getString(DESCRIPTION));

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    @Autowired
    public ExperienceJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(EXPERIENCE_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public Experience create(long userId, Date from, Date to, String enterpriseName, String position, String description) {
        if(to != null && to.before(from))
            throw new InvalidParameterException("La fecha 'hasta' no puede ser anterior a la fecha 'desde'");

        final Map<String, Object> values = new HashMap<>();
        values.put(USER_ID, userId);
        values.put(FROM, from);
        values.put(TO, to);
        values.put(ENTERPRISE_NAME, enterpriseName);
        values.put(POSITION, position);
        values.put(DESCRIPTION, description);

        Number experienceId = insert.executeAndReturnKey(values);

        return new Experience(experienceId.longValue(), userId, from, to, enterpriseName, position, description);
    }

    @Override
    public Optional<Experience> findById(long experienceId) {
        return template.query("SELECT * FROM " +  EXPERIENCE_TABLE + " WHERE " + ID + " = ?",
                new Object[]{ experienceId }, EXPERIENCE_MAPPER).stream().findFirst();
    }

    @Override
    public List<Experience> findByUserId(long userID) {
        return template.query("SELECT * FROM " +  EXPERIENCE_TABLE + " WHERE " + USER_ID + " = ?",
                new Object[]{ userID }, EXPERIENCE_MAPPER);
    }
}
