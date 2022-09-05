package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserJdbcDao implements UserDao {

    private static final String USER_TABLE = "usuario";
    private static final String ID = "id";
    private static final String NAME = "nombre";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "contrasenia";
    private static final String LOCATION = "ubicacion";
    private static final String CATEGORY_ID_FK = "idRubro";
    private static final String CURRENT_POSITION = "posicionActual";
    private static final String DESCRIPTION = "descripcion";
    private static final String EDUCATION = "educacion";

    private static final RowMapper<User> USER_MAPPER = (resultSet, rowNum) ->
            new User(resultSet.getLong(ID),
                    resultSet.getString(EMAIL),
                    resultSet.getString(PASSWORD),
                    resultSet.getString(NAME),
                    resultSet.getString(LOCATION),
                    resultSet.getLong(CATEGORY_ID_FK),
                    resultSet.getString(CURRENT_POSITION),
                    resultSet.getString(DESCRIPTION),
                    resultSet.getString(EDUCATION));

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    private final CategoryDao categoryDao;


    @Autowired
    public UserJdbcDao(final DataSource ds, final CategoryDao categoryDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns(ID);
        this.categoryDao = categoryDao;
    }

    @Override
    public User create(String email, String password, String name, String location, String categoryName, String currentPosition, String description, String education) {
        final Map<String, Object> values = new HashMap<>();
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(NAME, name);
        values.put(LOCATION, location);
        values.put(CURRENT_POSITION, currentPosition);
        values.put(DESCRIPTION, description);
        values.put(EDUCATION, education);

        //TODO: Chequear si esta validacion se hace aca
        Optional<Category> optCategory = categoryDao.findByName(categoryName);
        long categoryId_fk = optCategory.map(Category::getId).orElse(0L);
        values.put(CATEGORY_ID_FK, optCategory.isPresent() ? categoryId_fk : null);

        Number userId = insert.executeAndReturnKey(values);

        return new User(userId.longValue(), email, password, name, location, categoryId_fk, currentPosition, description, education);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return template.query("SELECT * FROM " +  USER_TABLE + " WHERE " + EMAIL + " = ?",
                new Object[]{ email }, USER_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findById(final long userId) {
        return template.query("SELECT * FROM " +  USER_TABLE + " WHERE " + ID + " = ?",
                new Object[]{ userId }, USER_MAPPER).stream().findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = template.query("SELECT * FROM " + USER_TABLE, USER_MAPPER);
        // Fixme: Es necesario?
        if(allUsers == null)
            return new ArrayList<>();
        return allUsers;
    }
}
