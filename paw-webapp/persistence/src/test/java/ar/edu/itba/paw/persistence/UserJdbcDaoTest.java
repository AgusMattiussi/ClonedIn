package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class UserJdbcDaoTest {

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

    private static final String TEST_NAME = "John Doe";
    private static final String TEST_EMAIL = "johndoe@gmail.com";
    private static final String TEST_PASSWORD = "pass123";
    private static final String TEST_LOCATION = "Calle Falsa 123";
    private static final long TEST_CATEGORY_ID_FK = 1;
    private static final String TEST_CURRENT_POSITION = "CEO de PAW";
    private static final String TEST_DESCRIPTION = "Un tipo muy laburante";
    private static final String TEST_EDUCATION = "Licenciado en la Universidad de la Calle";

    private static final long FIRST_ID = 1;
    private static final String EXISTING_NAME = "John Lennon";
    private static final String EXISTING_EMAIL = "johnlennon@gmail.com";
    private static final String EXISTING_PASSWORD = "imagineAPassword";

    @Autowired
    private UserJdbcDao dao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        //JdbcTestUtils.deleteFromTables(jdbctemplate, CATEGORY_TABLE);
    }

    @Test
    public void testCreate() {
        final User newUser = dao.create(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_ID_FK, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION) ;

        Assert.assertNotNull(newUser);
        Assert.assertEquals(TEST_EMAIL, newUser.getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newUser.getPassword());
        Assert.assertEquals(TEST_NAME, newUser.getName());
        Assert.assertEquals(TEST_LOCATION, newUser.getLocation());
        Assert.assertEquals(TEST_CATEGORY_ID_FK, newUser.getCategoryId_fk());
        Assert.assertEquals(TEST_CURRENT_POSITION, newUser.getCurrentPosition());
        Assert.assertEquals(TEST_DESCRIPTION, newUser.getDescription());
        Assert.assertEquals(TEST_EDUCATION, newUser.getEducation());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, USER_TABLE, EMAIL + " = '" + TEST_EMAIL + "'"));
    }



    @Test
    public void testFindById() {
        final Optional<User> newUser = dao.findById(FIRST_ID);

        Assert.assertTrue(newUser.isPresent());
        Assert.assertEquals(FIRST_ID, newUser.get().getId());
        Assert.assertEquals(EXISTING_EMAIL, newUser.get().getEmail());
        Assert.assertEquals(EXISTING_NAME, newUser.get().getName());
    }

    @Test
    public void testFindByEmail() {
        final Optional<User> newUser = dao.findByEmail(EXISTING_EMAIL);

        Assert.assertTrue(newUser.isPresent());
        Assert.assertEquals(FIRST_ID, newUser.get().getId());
        Assert.assertEquals(EXISTING_EMAIL, newUser.get().getEmail());
        Assert.assertEquals(EXISTING_NAME, newUser.get().getName());
    }

    @Test
    public void testGetAllUsers() {
        final User u1 = dao.create("naruto@gmail.com", TEST_PASSWORD, "Naruto", TEST_LOCATION, TEST_CATEGORY_ID_FK, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        final User u2 = dao.create("sasuke@gmail.com", TEST_PASSWORD, "Sasuke", TEST_LOCATION, TEST_CATEGORY_ID_FK, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        final User u3 = dao.create("sakura@gmail.com", TEST_PASSWORD, "Sakura", TEST_LOCATION, TEST_CATEGORY_ID_FK, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);

        final List<User> allUsers = dao.getAllUsers();
        //Tenemos en cuenta el insert inicial
        Assert.assertEquals(3 + 1, allUsers.size());
        Assert.assertTrue(allUsers.contains(u1));
        Assert.assertTrue(allUsers.contains(u2));
        Assert.assertTrue(allUsers.contains(u3));
    }

}
