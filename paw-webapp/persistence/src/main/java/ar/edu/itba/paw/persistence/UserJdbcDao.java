package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
    private static final String VISIBILITY = "visibilidad";
    private static final int DEFAULT_VISIBILITY=1;
    private static final String IMAGE_ID = "idImagen";
    private static final long DEFAULT_IMAGE_ID = 1;
    private static final int UNEXISTING_CATEGORY_ID = 99;



    private CategoryDao categoryDao;

    public static final String[] EDUCATION_LEVELS = new String[] {"Primario", "Secundario", "Terciario", "Graduado", "Posgrado"};
    public static final Set<String> educationLevelsSet = new HashSet<>(Arrays.asList(EDUCATION_LEVELS));

    private final RowMapper<User> USER_MAPPER = (resultSet, rowNum) -> {
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
                resultSet.getString(EDUCATION),
                resultSet.getInt(VISIBILITY),
                resultSet.getLong(IMAGE_ID));
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

        int visibility = DEFAULT_VISIBILITY;

        long imageId = DEFAULT_IMAGE_ID;

        final Map<String, Object> values = new HashMap<>();
        values.put(EMAIL, email);
        values.put(PASSWORD, password);
        values.put(NAME, name);
        values.put(LOCATION, location);
        values.put(CURRENT_POSITION, currentPosition);
        values.put(DESCRIPTION, description);
        values.put(EDUCATION, education);
        values.put(CATEGORY_ID_FK, categoryID);
        values.put(VISIBILITY, visibility);
        values.put(IMAGE_ID, null);

        Number userId = insert.executeAndReturnKey(values);

        return new User(userId.longValue(), email, password, name, location, category, currentPosition, description, education, visibility, null);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return template.query("SELECT * FROM usuario WHERE email = ?",
                new Object[]{ email }, USER_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findById(final long userId) {
        return template.query("SELECT * FROM usuario WHERE id = ?",
                new Object[]{ userId }, USER_MAPPER).stream().findFirst();
    }

    @Override
    public boolean userExists(String email) {
        return template.queryForObject("SELECT COUNT(*) FROM usuario WHERE email = ?", new Object[]{email} ,Integer.class) > 0;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = template.query("SELECT * FROM usuario", USER_MAPPER);
        return allUsers;
    }

    @Override
    public Optional<Integer> getUsersCount() {
        return template.query("SELECT COUNT(*) as count FROM usuario", COUNT_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<User> getUsersList(int page, int pageSize) {
        return template.query("SELECT * FROM usuario OFFSET ? LIMIT ? ",
                new Object[]{ pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public List<User> getUsersListByCategory(int page, int pageSize, int categoryId) {
        return template.query("SELECT * FROM usuario WHERE idRubro = ? OFFSET ? LIMIT ? ",
                new Object[]{ categoryId, pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public List<User> getUsersListByName(int page, int pageSize, String term) {
        return template.query("SELECT * FROM usuario WHERE visibilidad=1 AND nombre ILIKE CONCAT('%', ?, '%') OFFSET ? LIMIT ? ",
                new Object[]{ term, pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public List<User> getUsersListByLocation(int page, int pageSize, String location) {
        return template.query("SELECT * FROM usuario WHERE ubicacion ILIKE CONCAT('%', ?, '%') OFFSET ? LIMIT ? ",
                new Object[]{ location, pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public List<User> getUsersListByFilters(int page, int pageSize, String categoryId, String location, String educationLevel) {
        int catId;
        try {
            catId = Integer.parseInt(categoryId);
        } catch (NumberFormatException exception){
            catId = UNEXISTING_CATEGORY_ID;
        }

        Object[] sanitizedInputs = new Object[]{catId, location, educationLevel};

        StringBuilder filterQuery = new StringBuilder();
        filterQuery.append("SELECT * FROM usuario WHERE visibilidad=1");

        if(!categoryId.isEmpty())
            filterQuery.append(" AND idRubro = '").append(sanitizedInputs[0]).append("'");

        if(!location.isEmpty())
            filterQuery.append(" AND ubicacion ILIKE CONCAT('%', '").append(sanitizedInputs[1]).append("', '%')");

        if(!educationLevel.isEmpty())
            filterQuery.append(" AND educacion ILIKE CONCAT('%', '").append(sanitizedInputs[2]).append("', '%')");
        
        filterQuery.append(" ORDER BY id OFFSET ? LIMIT ?");

        return template.query(filterQuery.toString(),
                new Object[]{ pageSize * page, pageSize }, USER_MAPPER);
    }

    @Override
    public void updateName(long userID, String newName) {
        template.update("UPDATE usuario SET nombre = ? WHERE id = ?", newName, userID);
    }

    @Override
    public void updateDescription(long userID, String newDescription) {
        template.update("UPDATE usuario SET descripcion = ? WHERE id = ?", newDescription, userID);
    }

    @Override
    public void updateLocation(long userID, String newLocation) {
        template.update("UPDATE usuario SET ubicacion = ? WHERE id = ?", newLocation, userID);
    }

    @Override
    public void updateCurrentPosition(long userID, String newPosition) {
        template.update("UPDATE usuario SET posicionActual = ? WHERE id = ?", newPosition, userID);
    }

    @Override
    public void updateCategory(long userID, String newCategoryName) {
        try {
            Category category = categoryDao.findByName(newCategoryName).orElseThrow(CategoryNotFoundException::new);
            template.update("UPDATE usuario SET idRubro = ? WHERE id = ?", category.getId(), userID);
        } catch (CategoryNotFoundException exception) {

        }
    }

    @Override
    public void updateEducationLevel(long userID, String newEducationLevel) {
        if(educationLevelsSet.contains(newEducationLevel))
            template.update("UPDATE usuario SET educacion = ? WHERE id = ?", newEducationLevel, userID);
    }

    @Override
    public void updateVisibility(long userID, int visibility) {
        template.update("UPDATE usuario SET visibilidad = ? WHERE id = ?", new Object[]{visibility, userID});
    }

    @Override
    public void updateUserProfileImage(long userId, long imageId) {
        template.update("UPDATE usuario SET idImagen = ? WHERE id = ?", imageId, userId);
    }

    @Override
    public void changePassword(String email, String password) {
        template.update("UPDATE usuario SET contrasenia = ? WHERE email = ?", password, email);
    }
}
