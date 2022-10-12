package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

import static ar.edu.itba.paw.persistence.jdbc.SkillJdbcDao.SKILL_MAPPER;

@Repository
public class UserSkillJdbcDao implements UserSkillDao {

    private static final String USER_SKILL_TABLE = "aptitudUsuario";
    private static final String SKILL_ID = "idAptitud";
    private static final String USER_ID = "idUsuario";
    private static final String USER_TABLE_ID = "id";
    private static final String USER_TABLE_NAME = "nombre";
    private static final String USER_TABLE_EMAIL = "email";
    private static final String USER_TABLE_PASSWORD = "contrasenia";
    private static final String USER_TABLE_LOCATION = "ubicacion";
    private static final String USER_TABLE_CATEGORY_ID_FK = "idRubro";
    private static final String USER_TABLE_CURRENT_POSITION = "posicionActual";
    private static final String USER_TABLE_DESCRIPTION = "descripcion";
    private static final String USER_TABLE_EDUCATION = "educacion";
    private static final String USER_TABLE_VISIBILITY = "visibilidad";
    private static final String IMAGE_ID = "idImagen";


    private final SkillDao skillDao;
    private final UserDao userDao;
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    private CategoryDao categoryDao;
    private final RowMapper<User> USER_MAPPER = (resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(USER_TABLE_CATEGORY_ID_FK);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new User(resultSet.getLong(USER_TABLE_ID),
                resultSet.getString(USER_TABLE_EMAIL),
                resultSet.getString(USER_TABLE_PASSWORD),
                resultSet.getString(USER_TABLE_NAME),
                resultSet.getString(USER_TABLE_LOCATION),
                category,
                resultSet.getString(USER_TABLE_CURRENT_POSITION),
                resultSet.getString(USER_TABLE_DESCRIPTION),
                resultSet.getString(USER_TABLE_EDUCATION),
                resultSet.getInt(USER_TABLE_VISIBILITY),
                resultSet.getLong(IMAGE_ID));
    };

    @Autowired
    public UserSkillJdbcDao(final DataSource ds, final SkillDao skillDao, final UserDao userDao, final CategoryDao categoryDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(USER_SKILL_TABLE);
        this.skillDao = skillDao;
        this.userDao = userDao;
        this.categoryDao = categoryDao;
    }

    @Override
    public boolean addSkillToUser(String skillDescription, long userID) {
        Skill skill = skillDao.findByDescriptionOrCreate(skillDescription);
        if(skill != null)
            return addSkillToUser(skill.getId(), userID);
        return false;
    }

    @Override
    public boolean addSkillToUser(long skillID, long userID) {
        final Map<String, Object> values = new HashMap<>();
        values.put(USER_ID, userID);
        values.put(SKILL_ID, skillID);

        return insert.execute(values) > 0;
    }

    @Override
    public boolean alreadyExists(long skillID, long userID) {
        return template.queryForObject("SELECT COUNT(*) FROM aptitudUsuario WHERE idAptitud = ? AND idUsuario = ?",
                new Object[]{skillID, userID}, Integer.class) > 0;
    }

    @Override
    public boolean alreadyExists(String skillDescription, long userID) {
       Optional<Skill> skill = skillDao.findByDescription(skillDescription);
       if(!skill.isPresent())
           return false;
       return alreadyExists(skill.get().getId(), userID);
    }

    private List<Long> getUserIDsWithSkill(long skillID){
        return template.query("SELECT idUsuario FROM aptitudUsuario WHERE idAptitud = ?",
                new Object[]{ skillID }, (resultSet, rowNum) ->
            resultSet.getLong(USER_ID));
    }

    @Override
    public List<User> getUsersWithSkill(long skillID) {
        return template.query("SELECT * FROM usuario u JOIN aptitudUsuario au ON u.id = au.idUsuario WHERE au.idAptitud = ?",
                new Object[]{skillID}, USER_MAPPER);
    }

    @Override
    public List<User> getUsersWithSkill(String skillDescription){
        Optional<Skill> skill = skillDao.findByDescription(skillDescription);
        return skill.isPresent() ? getUsersWithSkill(skill.get().getId()) : new ArrayList<>();
    }

    private List<Long> getSkillIDsForUser(long userID){
        return template.query("SELECT idAptitud FROM aptitudUsuario WHERE idUsuario = ?",
                new Object[]{ userID }, (resultSet, rowNum) ->
            resultSet.getLong(SKILL_ID));
    }

    @Override
    public List<Skill> getSkillsForUser(long userID) {

        return template.query("SELECT a.id, a.descripcion FROM usuario u JOIN aptitudUsuario au ON u.id = au.idUsuario " +
                        "JOIN aptitud a ON a.id = au.idAptitud WHERE au.idUsuario = ?",
                new Object[]{userID}, SKILL_MAPPER);
    }

    @Override
    public void deleteSkillFromUser(long userID, long skillID) {
        template.update("DELETE FROM aptitudUsuario WHERE idUsuario = ? AND idAptitud = ?",
                userID, skillID);
    }
}
