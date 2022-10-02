package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.models.Education;
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
public class EducationJdbcDao implements EducationDao {


    private static final String EDUCATION_TABLE = "educacion";
    private static final String ID = "id";
    private static final String USER_ID = "idUsuario";
    private static final String MONTH_FROM = "mesDesde";
    private static final String YEAR_FROM = "anioDesde";
    private static final String MONTH_TO = "mesHasta";
    private static final String YEAR_TO = "anioHasta";
    private static final String TITLE = "titulo";
    private static final String INSTITUTION_NAME = "institucion";
    private static final String DESCRIPTION = "descripcion";

    private static final RowMapper<Education> EDUCATION_MAPPER = (resultSet, rowNum) ->
            new Education(resultSet.getLong(ID),
                    resultSet.getLong(USER_ID),
                    resultSet.getInt(MONTH_FROM),
                    resultSet.getInt(YEAR_FROM),
                    resultSet.getInt(MONTH_TO),
                    resultSet.getInt(YEAR_TO),
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

    @SuppressWarnings("DuplicatedCode")
    @Override
    public Education add(long userId, int monthFrom, int yearFrom, int monthTo, int yearTo, String title, String institutionName, String description) {
        if(!isDateValid(monthFrom, yearFrom, monthTo, yearTo))
            throw new InvalidParameterException("La fecha" + monthFrom+ "/" + yearFrom +
                    " - " + monthTo + "/" + yearTo +  " es incorrecta");

        final Map<String, Object> values = new HashMap<>();
        values.put(USER_ID, userId);
        values.put(MONTH_FROM, monthFrom);
        values.put(YEAR_FROM, yearFrom);
        values.put(MONTH_TO, monthTo);
        values.put(YEAR_TO, yearTo);
        values.put(TITLE, title);
        values.put(INSTITUTION_NAME, institutionName);
        values.put(DESCRIPTION, description);

        Number educationId = insert.executeAndReturnKey(values);

        return new Education(educationId.longValue(), userId, monthFrom, yearFrom, monthTo, yearTo, title, institutionName, description);
    }

    @Override
    public Optional<Education> findById(long educationId) {
        return template.query("SELECT * FROM educacion WHERE id = ?",
                new Object[]{ educationId }, EDUCATION_MAPPER).stream().findFirst();
    }

    @Override
    public List<Education> findByUserId(long userID) {
        return template.query("SELECT * FROM educacion WHERE idUsuario = ? ORDER BY anioDesde DESC, mesDesde DESC",
                new Object[]{ userID }, EDUCATION_MAPPER);
    }

    @Override
    public void deleteEducation(long educationId) {
       template.update("DELETE FROM educacion WHERE id = ?", educationId);
    }
}
