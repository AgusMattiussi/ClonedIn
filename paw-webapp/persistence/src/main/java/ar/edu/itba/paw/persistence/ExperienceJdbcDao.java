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
    private static final String MONTH_FROM = "mesDesde";
    private static final String YEAR_FROM = "anioDesde";
    private static final String MONTH_TO = "mesHasta";
    private static final String YEAR_TO = "anioHasta";
    private static final String ENTERPRISE_NAME = "empresa";
    private static final String POSITION = "posicion";
    private static final String DESCRIPTION = "descripcion";

    private static final RowMapper<Experience> EXPERIENCE_MAPPER = (resultSet, rowNum) ->
            new Experience(resultSet.getLong(ID),
                    resultSet.getLong(USER_ID),
                    resultSet.getInt(MONTH_FROM),
                    resultSet.getInt(YEAR_FROM),
                    resultSet.getInt(MONTH_TO),
                    resultSet.getInt(YEAR_TO),
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

    private boolean isMonthValid(int month){
        return month >= 1 && month <= 12;
    }

    private boolean isYearValid(int year){
        return year >= 1900 && year <= 2100;
    }

    private boolean isDateValid(int monthFrom, int yearFrom, int monthTo, int yearTo){
        if(!isMonthValid(monthTo) || !isMonthValid(monthFrom) || !isYearValid(yearTo) || !isYearValid(yearFrom))
            return false;
        return yearTo > yearFrom || (yearTo == yearFrom && monthTo >= monthFrom);
    }

    @Override
    public Experience create(long userId, int monthFrom, int yearFrom, Integer monthTo, Integer yearTo, String enterpriseName, String position, String description) {
        if(monthTo == null && yearTo != null || monthTo != null && yearTo == null)
            throw new InvalidParameterException(" monthTo y yearTo no pueden ser null simultaneamente");

        if(monthTo != null && yearTo != null) {
            if (!isDateValid(monthFrom, yearFrom, monthTo, yearTo))
                throw new InvalidParameterException("La fecha" + monthFrom + "/" + yearFrom +
                        " - " + monthTo + "/" + yearTo + " es incorrecta");
        }

        final Map<String, Object> values = new HashMap<>();
        values.put(USER_ID, userId);
        values.put(MONTH_FROM, monthFrom);
        values.put(YEAR_FROM, yearFrom);
        values.put(MONTH_TO, monthTo);
        values.put(YEAR_TO, yearTo);
        values.put(ENTERPRISE_NAME, enterpriseName);
        values.put(POSITION, position);
        values.put(DESCRIPTION, description);

        Number experienceId = insert.executeAndReturnKey(values);

        return new Experience(experienceId.longValue(), userId, monthFrom, yearFrom, monthTo, yearTo, enterpriseName, position, description);
    }

    @Override
    public Optional<Experience> findById(long experienceId) {
        return template.query("SELECT * FROM experiencia WHERE id = ?",
                new Object[]{ experienceId }, EXPERIENCE_MAPPER).stream().findFirst();
    }

    @Override
    public List<Experience> findByUserId(long userID) {
        return template.query("SELECT * FROM experiencia WHERE idUsuario = ?",
                new Object[]{ userID }, EXPERIENCE_MAPPER);
    }

    @Override
    public void deleteExperience(long experienceId) {
        template.update("DELETE FROM experiencia WHERE id = ?", experienceId);
    }
}
