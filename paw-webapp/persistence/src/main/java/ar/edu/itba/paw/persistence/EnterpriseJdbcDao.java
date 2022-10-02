package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.persistence.exceptions.CategoryNotFoundException;
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

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;
    private CategoryDao categoryDao;

    protected final RowMapper<Enterprise> ENTERPRISE_MAPPER = (resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(CATEGORY_ID_FK);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new Enterprise(resultSet.getLong(ID),
                resultSet.getString(NAME),
                resultSet.getString(EMAIL),
                resultSet.getString(PASSWORD),
                resultSet.getString(LOCATION),
                category,
                resultSet.getString(DESCRIPTION));
    };



    @Autowired
    public EnterpriseJdbcDao(final DataSource ds, final CategoryDao categoryDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(ENTERPRISE_TABLE)
                .usingGeneratedKeyColumns(ID);
        this.categoryDao = categoryDao;
    }

    @Override
    public Enterprise create(String email, String name, String password, String location, String categoryName, String description) {
        Category category = categoryDao.findByName(categoryName).orElseThrow(CategoryNotFoundException::new);

        final Map<String, Object> values = new HashMap<>();
        values.put(NAME, name);
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(LOCATION, location);
        values.put(DESCRIPTION, description);
        values.put(CATEGORY_ID_FK, category.getId());

        Number enterpriseId = insert.executeAndReturnKey(values);

        return new Enterprise(enterpriseId.longValue(), name, email, password, location, category, description);
    }

    @Override
    public Optional<Enterprise> findByEmail(final String email) {
        return template.query("SELECT * FROM empresa WHERE email = ?",
                new Object[]{ email }, ENTERPRISE_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Enterprise> findById(final long enterpriseId) {
        return template.query("SELECT * FROM empresa WHERE id = ?",
                new Object[]{ enterpriseId }, ENTERPRISE_MAPPER).stream().findFirst();
    }

    @Override
    public void changePassword(String email, String password) {
        template.update("UPDATE empresa SET contrasenia = ? WHERE email = ?", new Object[] {password, email});
    }

    @Override
    public boolean enterpriseExists(String email) {
        return template.queryForObject("SELECT COUNT(*) FROM empresa WHERE email = ?", new Object[]{ email }, Integer.class) > 0;
    }

    @Override
    public void updateName(long userID, String newName) {
        template.update("UPDATE empresa SET nombre = ? WHERE id = ?", new Object[] {newName, userID});
    }

    @Override
    public void updateDescription(long userID, String newDescription) {
        template.update("UPDATE empresa SET descripcion = ? WHERE id = ?", new Object[] {newDescription, userID});
    }

    @Override
    public void updateLocation(long userID, String newLocation) {
        template.update("UPDATE empresa SET ubicacion = ? WHERE id = ?", new Object[] {newLocation, userID});
    }

    @Override
    public void updateCategory(long enterpriseID, String newCategoryName) {
        Category category = categoryDao.findByName(newCategoryName).orElseThrow(CategoryNotFoundException::new);
        template.update("UPDATE empresa SET idRubro = ? WHERE id = ?", new Object[] {category.getId(), enterpriseID});
    }
}
