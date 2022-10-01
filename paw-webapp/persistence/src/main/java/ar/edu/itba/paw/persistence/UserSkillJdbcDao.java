package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;

@Repository
public class UserSkillJdbcDao implements UserSkillDao {

    private static final String USER_SKILL_TABLE = "aptitudUsuario";
    private static final String SKILL_ID = "idAptitud";
    private static final String USER_ID = "idUsuario";
    private final SkillDao skillDao;
    private final UserDao userDao;
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    @Autowired
    public UserSkillJdbcDao(final DataSource ds, final SkillDao skillDao, final UserDao userDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(USER_SKILL_TABLE);
        this.skillDao = skillDao;
        this.userDao = userDao;
    }

    @Override
    public boolean addSkillToUser(String skillDescription, long userID) {
        // Optional<Skill> skill = skillDao.findByDescription(skillDescription);
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

    private List<Long> getUserIDsWithSkill(long skillID){
        return template.query("SELECT " + USER_ID + " FROM " + USER_SKILL_TABLE + " WHERE " + SKILL_ID + " = ?",
                new Object[]{ skillID }, (resultSet, rowNum) ->
            resultSet.getLong(USER_ID));
    }

    @Override
    public List<User> getUsersWithSkill(long skillID) {
        List<Long> userIDs = getUserIDsWithSkill(skillID);
        List<User> userList = new ArrayList<>();

        for (Long uid : userIDs) {
            Optional<User> currentUser = userDao.findById(uid);
            currentUser.ifPresent(userList::add);
        }

        return userList;
    }

    @Override
    public List<User> getUsersWithSkill(String skillDescription){
        Optional<Skill> skill = skillDao.findByDescription(skillDescription);
        return skill.isPresent() ? getUsersWithSkill(skill.get().getId()) : new ArrayList<>();
    }

    private List<Long> getSkillIDsForUser(long userID){
        return template.query("SELECT " + SKILL_ID + " FROM " + USER_SKILL_TABLE + " WHERE " + USER_ID + " = ?",
                new Object[]{ userID }, (resultSet, rowNum) ->
            resultSet.getLong(SKILL_ID));
    }

    @Override
    public List<Skill> getSkillsForUser(long userID) {
        List<Long> skillIDs = getSkillIDsForUser(userID);
        List<Skill> skillList = new ArrayList<>();

        for (Long uid : skillIDs) {
            Optional<Skill> currentSkill = skillDao.findById(uid);
            currentSkill.ifPresent(skillList::add);
        }

        return skillList;
    }

    @Override
    public void deleteSkillFromUser(long userID, long skillID) {
        template.update("DELETE FROM " +  USER_SKILL_TABLE + " WHERE " + USER_ID + " = ?" + " AND " + SKILL_ID + " = ?",
                userID, skillID);
    }
}
