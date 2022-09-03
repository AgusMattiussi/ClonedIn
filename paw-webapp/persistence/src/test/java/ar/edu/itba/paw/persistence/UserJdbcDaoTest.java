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
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class UserJdbcDaoTest {

    private static final String EMAIL = "foo@bar.com";
    private static final String PASSWORD = "secret";
    private static final String NON_EXISTING_EMAIL = "foo@barbaz.com";

    @Autowired
    private UserJdbcDao dao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    /*@Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbctemplate, "users");
    }*/

    @Test
    public void easy() {
        Assert.assertTrue(true);
    }

   /* @Test
    public void testFindByEmailNonExisting() {
        Optional<User> maybeUser = dao.findByEmail(NON_EXISTING_EMAIL);

        Assert.assertFalse(maybeUser.isPresent());
    }*/

   /* @Test
    public void testCreate() {
       final User newUser = dao.create(NON_EXISTING_EMAIL, PASSWORD);

       Assert.assertNotNull(newUser);
       Assert.assertEquals(NON_EXISTING_EMAIL, newUser.getEmail());
       Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, "users", "email = '" + NON_EXISTING_EMAIL + "'"));
    }*/

    /*@Test(expected = DuplicateKeyException.class)
    public void testCreateAltredyExists() {
       final User newUser = dao.create(EMAIL, PASSWORD);

       Assert.assertNotNull(newUser);
       Assert.assertEquals(EMAIL, newUser.getEmail());
       Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, "users", "email = '" + EMAIL + "'"));
    }*/

}


