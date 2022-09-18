package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.models.Enterprise;
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
public class EnterpriseJdbcDao implements EnterpriseDao {
    private static final String ENTERPRISE_TABLE = "empresa";
    private static final String ID = "id";
    private static final String NAME = "nombre";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "contrasenia";
    private static final String LOCATION = "ubicacion";
    private static final String CATEGORY_ID_FK = "idRubro";
    private static final String DESCRIPTION = "descripcion";

    protected static final RowMapper<Enterprise> ENTERPRISE_MAPPER = (resultSet, rowNum) ->
            new Enterprise(resultSet.getLong(ID),
                    resultSet.getString(NAME),
                    resultSet.getString(EMAIL),
                    resultSet.getString(PASSWORD),
                    resultSet.getString(LOCATION),
                    resultSet.getLong(CATEGORY_ID_FK),
                    resultSet.getString(DESCRIPTION));

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    @Autowired
    public EnterpriseJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(ENTERPRISE_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    @Override
    public Enterprise create(String email, String name, String password, String location, long categoryId_fk, String description) {
        final Map<String, Object> values = new HashMap<>();
        values.put(NAME, name);
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(LOCATION, location);
        //TODO: Chequear si esta validacion se hace aca
        values.put(CATEGORY_ID_FK, categoryId_fk != 0 ? categoryId_fk : null);
        values.put(DESCRIPTION, description);

        Number enterpriseId = insert.executeAndReturnKey(values);

        return new Enterprise(enterpriseId.longValue(), name, email, password, location, categoryId_fk, description);
    }

    @Override
    public Optional<Enterprise> findByEmail(final String email) {
        return template.query("SELECT * FROM " +  ENTERPRISE_TABLE + " WHERE " + EMAIL + " = ?",
                new Object[]{ email }, ENTERPRISE_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Enterprise> findById(final long enterpriseId) {
        return template.query("SELECT * FROM " +  ENTERPRISE_TABLE + " WHERE " + ID + " = ?",
                new Object[]{ enterpriseId }, ENTERPRISE_MAPPER).stream().findFirst();
    }

    @Override
    public void changePassword(String email, String password) {
        template.update("UPDATE " + ENTERPRISE_TABLE + " SET " + PASSWORD + " = ? WHERE " + EMAIL + " = ?", new Object[] {password, email});
    }

    @Override
    public boolean enterpriseExists(String email) {
        return findByEmail(email).isPresent();
    }
}
