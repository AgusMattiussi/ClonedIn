package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.exceptions.CategoryNotFoundException;
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

    private CategoryDao categoryDao;

    public static final String[] EDUCATION_LEVELS = new String[] {"Primario", "Secundario", "Terciario", "Graduado", "Posgrado"};
    public static final Set<String> educationLevelsSet = new HashSet<>(Arrays.asList(EDUCATION_LEVELS));

    protected final RowMapper<User> USER_MAPPER = (resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(CATEGORY_ID_FK);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new User(resultSet.getLong(ID),
                resultSet.getString(EMAIL),
                resultSet.getString(PASSWORD),
                resultSet.getString(NAME),
                resultSet.getString(LOCATION),
                category,
                resultSet.getString(CURRENT_POSITION),
                resultSet.getString(DESCRIPTION),
                resultSet.getString(EDUCATION));
    };

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = (rs, rowNum) -> rs.getInt("count");

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;


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
        long categoryID = 0;
        Category category = null;
        if(categoryName != null) {
            category = categoryDao.findByName(categoryName).orElseThrow(CategoryNotFoundException::new);
            categoryID = category.getId();
        }

        if(!educationLevelsSet.contains(education))
            education = "No-especificado";

        final Map<String, Object> values = new HashMap<>();
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(NAME, name);
        values.put(LOCATION, location);
        values.put(CURRENT_POSITION, currentPosition);
        values.put(DESCRIPTION, description);
        values.put(EDUCATION, education);
        values.put(CATEGORY_ID_FK, categoryID);

        Number userId = insert.executeAndReturnKey(values);

        return new User(userId.longValue(), email, password, name, location, category, currentPosition, description, education);
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
    public boolean userExists(String email) {
        return findByEmail(email).isPresent();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = template.query("SELECT * FROM " + USER_TABLE, USER_MAPPER);
        // Fixme: Es necesario?
        if(allUsers == null)
            return new ArrayList<>();
        return allUsers;
    }

    @Override
    public Optional<Integer> getUsersCount() {
        return template.query("SELECT COUNT(*) as count FROM " +  USER_TABLE, COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<User> getUsersList(int page, int pageSize) {
        return template.query("SELECT * FROM " +  USER_TABLE + " OFFSET ? LIMIT ? ",
                new Object[]{ pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public List<User> getUsersListByCategory(int page, int pageSize, int categoryId) {
        return template.query("SELECT * FROM " +  USER_TABLE + " WHERE " + CATEGORY_ID_FK + " = ?" + " OFFSET ? LIMIT ? ",
                new Object[]{ categoryId, pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public List<User> getUsersListByName(int page, int pageSize, String term) {
        return template.query("SELECT * FROM " +  USER_TABLE + " WHERE " + NAME + " ILIKE CONCAT('%', ?, '%')" + " OFFSET ? LIMIT ? ",
                new Object[]{ term, pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public List<User> getUsersListByLocation(int page, int pageSize, String location) {
        return template.query("SELECT * FROM " +  USER_TABLE + " WHERE " + LOCATION + " ILIKE CONCAT('%', ?, '%')" + " OFFSET ? LIMIT ? ",
                new Object[]{ location, pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public List<User> getUsersListByFilters(int page, int pageSize, String categoryId, String location, String educationLevel) {
        StringBuilder filterQuery = new StringBuilder();
        filterQuery.append("SELECT * FROM ").append(USER_TABLE);



        if(!categoryId.isEmpty())
            filterQuery.append(" WHERE ").append(CATEGORY_ID_FK).append(" = ").append(categoryId);


        if(!location.isEmpty()) {
            if (!categoryId.isEmpty())
                filterQuery.append(" AND ");
            else
                filterQuery.append(" WHERE ");
            filterQuery.append(LOCATION).append(" ILIKE CONCAT('%', '").append(location).append("', '%')");
        }

        if(!educationLevel.isEmpty()) {
            if (categoryId.isEmpty() && location.isEmpty())
                filterQuery.append(" WHERE ");
            else
                filterQuery.append(" AND ");
            filterQuery.append(EDUCATION).append(" ILIKE CONCAT('%', '").append(educationLevel).append("', '%')");
        }

        filterQuery.append(" OFFSET ? LIMIT ? ");

        return template.query(filterQuery.toString(),
                new Object[]{ pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public void changePassword(String email, String password) {
        template.update("UPDATE " + USER_TABLE + " SET " + PASSWORD + " = ? WHERE " + EMAIL + " = ?", new Object[] {password, email});
    }
}
