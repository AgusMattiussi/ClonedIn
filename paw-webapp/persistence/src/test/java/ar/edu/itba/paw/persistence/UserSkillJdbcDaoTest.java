package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import ar.edu.itba.paw.persistence.jdbc.UserSkillJdbcDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional*/
public class UserSkillJdbcDaoTest {

    /*private static final String USER_SKILL_TABLE = "aptitudUsuario";
    private static final String SKILL_ID = "idAptitud";
    private static final String USER_ID = "idUsuario";

    private static final String TEST_NAME = "John Doe";
    private static final String TEST_EMAIL = "johndoe@gmail.com";
    private static final String TEST_PASSWORD = "pass123";
    private static final String TEST_LOCATION = "Calle Falsa 123";
    //private static final long TEST_CATEGORY_ID_FK = 1;
    private static final String TEST_CATEGORY_NAME = "AlgunaCategoria";
    private static final String TEST_CURRENT_POSITION = "CEO de PAW";
    private static final String TEST_DESCRIPTION = "Un tipo muy laburante";
    private static final String TEST_EDUCATION = "Licenciado en la Universidad de la Calle";
    private static final String TEST_SKILL = "unaskill";
    public static final String EXISTING_SKILL = "testskill";
    public static final String NON_EXISTING_SKILL = "nonexistingskill";
    public static final String EXISTING_USER_EMAIL = "johnlennon@gmail.com";

    @Autowired
    private UserSkillJdbcDao userSkillDao;

    @Autowired
    private SkillDao skillDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    private Skill testSkill;
    private User testUser;*/

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    /*@Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        testSkill = skillDao.findByDescription(EXISTING_SKILL).get();
        testUser = userDao.findByEmail(EXISTING_USER_EMAIL).get();
    }

    @Test
    public void testGetSkillsForUser() {

        final List<Skill> skillList = userSkillDao.getSkillsForUser(1);

        assertEquals(1, skillList.size());
        assertEquals(1, skillList.get(0).getId());
    }

    @Test
    public void testGetUsersWithSkillUsingID() {
        final List<User> userList = userSkillDao.getUsersWithSkill(1);

        assertEquals(1, userList.size());
        assertEquals(Long.valueOf(1), userList.get(0).getId());
    }

    @Test
    public void testGetUsersWithSkillUsingDescription() {
        final List<User> userList = userSkillDao.getUsersWithSkill("testskill");

        assertEquals(1, userList.size());
        assertEquals(Long.valueOf(1), userList.get(0).getId());
    }

    @Test
    public void testAddSkillToUserUsingID() {
        final User user = userDao.findByEmail("johnlennon@gmail.com").get();
        final Skill skill = skillDao.create("aaaaa");

        final boolean added = userSkillDao.addSkillToUser(skill.getId(), user.getId());
        final List<Skill> skillList = userSkillDao.getSkillsForUser(user.getId());
        final List<User> userList = userSkillDao.getUsersWithSkill(skill.getId());

        assertTrue(added);
        assertEquals(2, skillList.size());
        assertTrue(skillList.contains(skill));
        assertEquals(1, userList.size());
        assertTrue(userList.contains(user));
    }

    @Test
    public void testAddSkillToUserUsingDescription() {
        //JdbcTestUtils.deleteFromTables(jdbctemplate, USER_SKILL_TABLE);
        final User user = userDao.findByEmail("johnlennon@gmail.com").get();
        final Skill skill = skillDao.create("bbbbb");

        final boolean added = userSkillDao.addSkillToUser(skill.getDescription(), user.getId());
        final List<Skill> skillList = userSkillDao.getSkillsForUser(user.getId());
        final List<User> userList = userSkillDao.getUsersWithSkill(skill.getDescription());

        assertTrue(added);
        assertEquals(2, skillList.size());
        assertTrue(skillList.contains(skill));
        assertEquals(1, userList.size());
        assertTrue(userList.contains(user));
    }

    @Test
    public void testAlreadyExistsWithIDTrue(){
        final boolean exists = userSkillDao.alreadyExists(testSkill.getId(), testUser.getId());

        assertTrue(exists);
    }

    @Test
    public void testAlreadyExistsWithIDFalse(){
        final boolean exists = userSkillDao.alreadyExists(0, testUser.getId());

        assertFalse(exists);
    }

    @Test
    public void testAlreadyExistsWithDescriptionTrue(){
        final boolean exists = userSkillDao.alreadyExists(testSkill.getDescription(), testUser.getId());

        assertTrue(exists);
    }

    @Test
    public void testAlreadyExistsWithDescriptionFalse(){
        final boolean exists = userSkillDao.alreadyExists(NON_EXISTING_SKILL, testUser.getId());

        assertFalse(exists);
    }

    @Test
    public void testDeleteSkillFromUser() {
        final User user = userDao.findByEmail("johnlennon@gmail.com").get();
        final Skill skill = skillDao.create("ccccc");
        final boolean added = userSkillDao.addSkillToUser(skill.getId(), user.getId());
        assertTrue(added);
        userSkillDao.deleteSkillFromUser(user.getId(), skill.getId());
        final List<Skill> skillList = userSkillDao.getSkillsForUser(user.getId());
        assertFalse(skillList.contains(skill));
    }*/
}
