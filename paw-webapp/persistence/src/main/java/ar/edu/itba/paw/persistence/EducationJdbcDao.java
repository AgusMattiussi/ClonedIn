package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.models.Education;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EducationJdbcDao implements EducationDao {


    private static final String EDUCATION_TABLE = "educacion";
    private static final String ID = "id";
    private static final String USER_ID = "idUsuario";
    private static final String FROM = "fechaDesde";
    private static final String TO = "fechaHasta";
    private static final String TITLE = "titulo";
    private static final String INSTITUTION_NAME = "institucion";
    private static final String DESCRIPTION = "descripcion";

    private static final RowMapper<Education> EDUCATION_MAPPER = (resultSet, rowNum) ->
            new Education(resultSet.getLong(ID),
                    resultSet.getLong(USER_ID),
                    resultSet.getDate(FROM),
                    resultSet.getDate(TO),
                    resultSet.getString(TITLE),
                    resultSet.getString(INSTITUTION_NAME),
                    resultSet.getString(DESCRIPTION));

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    @Autowired
    public EducationJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(EDUCATION_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public Education add(long userId, Date from, Date to, String title, String institutionName, String description) {
        final Map<String, Object> values = new HashMap<>();
        values.put(USER_ID, userId);
        //FIXME: Cambiar esto para manejar fechas de verdad
        values.put(FROM, from);
        values.put(TO, to);
        values.put(TITLE, title);
        values.put(INSTITUTION_NAME, institutionName);
        values.put(DESCRIPTION, description);

        Number educationId = insert.executeAndReturnKey(values);

        return new Education(educationId.longValue(), userId, from, to, title, institutionName, description);
    }

    @Override
    public Optional<Education> findById(long educationId) {
        return template.query("SELECT * FROM " +  EDUCATION_TABLE + " WHERE " + ID + " = ?",
                new Object[]{ educationId }, EDUCATION_MAPPER).stream().findFirst();
    }

    @Override
    public List<Education> findByUserId(long userID) {
        return template.query("SELECT * FROM " +  EDUCATION_TABLE + " WHERE " + USER_ID + " = ?",
                new Object[]{ userID }, EDUCATION_MAPPER);
    }
}
