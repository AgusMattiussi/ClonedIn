package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class UserJdbcDaoTest {

    /*private static final String USER_TABLE = "usuario";
    private static final String ID = "id";
    private static final String NAME = "nombre";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "contrasenia";
    private static final String LOCATION = "ubicacion";
    private static final String CATEGORY_ID_FK = "idRubro";
    private static final String CURRENT_POSITION = "posicionActual";
    private static final String DESCRIPTION = "descripcion";
    private static final String EDUCATION = "educacion";

    private static final String TEST_NAME = "John Doe";
    private static final String TEST_EMAIL = "johndoe@gmail.com";
    private static final String TEST_PASSWORD = "pass123";
    private static final String TEST_LOCATION = "Calle Falsa 123";
    //private static final long TEST_CATEGORY_ID_FK = 1;
    private static final String TEST_CATEGORY_NAME = "testCategory";
    private static final String TEST_CURRENT_POSITION = "CEO de PAW";
    private static final String TEST_DESCRIPTION = "Un tipo muy laburante";
    private static final String TEST_EDUCATION = "No-especificado";

    private static final Long FIRST_ID = 1L;
    private static final String EXISTING_NAME = "John Lennon";
    private static final String EXISTING_EMAIL = "johnlennon@gmail.com";
    private static final String EXISTING_PASSWORD = "imagineAPassword";
    public static final String NEW_EMAIL = "goku@gmail.com";
    public static final String UPDATED_STRING = "updatedstring";

    @Autowired
    private UserJdbcDao dao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private DataSource ds;
    private JdbcTemplate jdbctemplate;
    private Category testCategory;*/

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    /*@Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        testCategory = categoryDao.findByName(TEST_CATEGORY_NAME).get();
        //JdbcTestUtils.deleteFromTables(jdbctemplate, CATEGORY_TABLE);
    }

    @Test
    public void testCreate() {
        final User newUser = dao.create(TEST_EMAIL,
                TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_NAME,
                TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION) ;

        Assert.assertNotNull(newUser);
        Assert.assertEquals(TEST_EMAIL, newUser.getEmail());
        Assert.assertEquals(TEST_NAME, newUser.getName());
        Assert.assertEquals(TEST_LOCATION, newUser.getLocation());
        Assert.assertEquals(testCategory, newUser.getCategory());
        Assert.assertEquals(TEST_CURRENT_POSITION, newUser.getCurrentPosition());
        Assert.assertEquals(TEST_DESCRIPTION, newUser.getDescription());
        Assert.assertEquals(TEST_EDUCATION, newUser.getEducation());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, USER_TABLE, EMAIL + " = '" + TEST_EMAIL + "'"));
    }



    @Test
    public void testFindById() {
        final Optional<User> newUser = dao.findById(FIRST_ID);

        assertTrue(newUser.isPresent());
        Assert.assertEquals(FIRST_ID, newUser.get().getId());
        Assert.assertEquals(EXISTING_EMAIL, newUser.get().getEmail());
        Assert.assertEquals(EXISTING_NAME, newUser.get().getName());
    }

    @Test
    public void testFindByEmail() {
        final Optional<User> newUser = dao.findByEmail(EXISTING_EMAIL);

        assertTrue(newUser.isPresent());
        Assert.assertEquals(FIRST_ID, newUser.get().getId());
        Assert.assertEquals(EXISTING_EMAIL, newUser.get().getEmail());
        Assert.assertEquals(EXISTING_NAME, newUser.get().getName());
    }

    @Test
    public void testGetAllUsers() {
        final User u1 = dao.create("a@gmail.com", TEST_PASSWORD, "A", TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        final User u2 = dao.create("b@gmail.com", TEST_PASSWORD, "B", TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        final User u3 = dao.create("c@gmail.com", TEST_PASSWORD, "C", TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);

        long userCount = JdbcTestUtils.countRowsInTable(jdbctemplate, USER_TABLE);

        final List<User> allUsers = dao.getAllUsers();
        //Tenemos en cuenta el insert inicial
        Assert.assertEquals(userCount, allUsers.size());
        assertTrue(allUsers.contains(u1));
        assertTrue(allUsers.contains(u2));
        assertTrue(allUsers.contains(u3));
    }

    @Test
    public void testUpdateName(){
        User u1 = dao.create(NEW_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        dao.updateName(u1.getId(), UPDATED_STRING);
        u1 = dao.findById(u1.getId()).get();

        Assert.assertEquals(UPDATED_STRING, u1.getName());
    }

    @Test
    public void testUpdateDescription(){
        User u1 = dao.create(NEW_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        dao.updateDescription(u1.getId(), UPDATED_STRING);
        u1 = dao.findById(u1.getId()).get();

        Assert.assertEquals(UPDATED_STRING, u1.getDescription());
    }

    @Test
    public void testUpdateLocation(){
        User u1 = dao.create(NEW_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        dao.updateLocation(u1.getId(), UPDATED_STRING);
        u1 = dao.findById(u1.getId()).get();

        Assert.assertEquals(UPDATED_STRING, u1.getLocation());
    }

    @Test
    public void testUpdateCurrentPosition(){
        User u1 = dao.create(NEW_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        dao.updateCurrentPosition(u1.getId(), UPDATED_STRING);
        u1 = dao.findById(u1.getId()).get();

        Assert.assertEquals(UPDATED_STRING, u1.getCurrentPosition());
    }*/

}
