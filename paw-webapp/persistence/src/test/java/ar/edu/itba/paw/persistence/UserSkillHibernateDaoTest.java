package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.models.ids.UserSkillId;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserSkillHibernateDaoTest {

    private static final String USER_SKILL_TABLE = "aptitudUsuario";
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
    public static final String NEW_SKILL_DESC = "testskill";
    public static final String NEW_SKILL_DESC2 = "testskill2";
    public static final String NEW_SKILL_DESC3 = "testskill3";
    public static final String NON_EXISTING_SKILL = "nonexistingskill";
    public static final String TEST_USER_EMAIL = "johnlennon@gmail.com";
    public static final String EMPTY_FIELD = "-";
    public static final String TEST_CATEGORY_DESC = "testCategory";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserSkillDao userSkillDao;


    private Skill testSkill;
    private Skill testSkill2;
    private Skill testSkill3;
    private Category testCategory;
    private User testUser;
    private UserSkill testUserSkill;

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    @Before
    public void setUp() {
        testSkill = new Skill(NEW_SKILL_DESC);
        em.persist(testSkill);

        testSkill2 = new Skill(NEW_SKILL_DESC2);
        em.persist(testSkill2);

        testSkill3 = new Skill(NEW_SKILL_DESC3);
        em.persist(testSkill3);

        testCategory = new Category(TEST_CATEGORY_DESC);
        em.persist(testCategory);

        testUser = new User(TEST_USER_EMAIL, EMPTY_FIELD, EMPTY_FIELD, EMPTY_FIELD, testCategory, EMPTY_FIELD,
                EMPTY_FIELD, EMPTY_FIELD, Visibility.VISIBLE.getValue(), null);
        em.persist(testUser);

        testUserSkill = new UserSkill(testUser, testSkill);
        em.persist(testUserSkill);
    }

    @After
    public void restore(){
        em.remove(testSkill);
        em.remove(testSkill2);
        em.remove(testCategory);
        em.remove(testUser);
        em.remove(testUserSkill);
    }

    @Test
    public void testGetSkillsForUser() {
        final List<Skill> skillList = userSkillDao.getSkillsForUser(testUser);

        assertEquals(1, skillList.size());
        assertEquals(testSkill, skillList.get(0));
    }

    @Test
    public void testGetUsersWithSkill() {
        final List<User> userList = userSkillDao.getUsersWithSkill(testSkill);

        assertEquals(1, userList.size());
        assertEquals(testUser, userList.get(0));
    }


    @Test
    public void testAddSkillToUser() {
        final UserSkill added = userSkillDao.addSkillToUser(testSkill2, testUser);

        UserSkill found = em.find(UserSkill.class, new UserSkillId(testUser.getId(), testSkill2.getId()));

        assertNotNull(found);
        assertEquals(added.getSkill(), found.getSkill());
        assertEquals(added.getUser(), found.getUser());
    }


    @Test
    public void testAlreadyExists(){
        final boolean exists = userSkillDao.alreadyExists(testSkill, testUser);

        assertTrue(exists);
    }

    @Test
    public void testDeleteSkillFromUser() {
        userSkillDao.deleteSkillFromUser(testUser, testSkill);
        UserSkill found = em.find(UserSkill.class, new UserSkillId(testUser.getId(), testSkill2.getId()));

        assertNull(found);
    }
}
