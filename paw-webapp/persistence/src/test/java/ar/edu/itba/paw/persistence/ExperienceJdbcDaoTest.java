package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Experience;
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
import java.sql.Date;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class ExperienceJdbcDaoTest {
    private static final String EXPERIENCE_TABLE = "experiencia";
    private static final String ID = "id";
    private static final String USER_ID = "idUsuario";
    private static final String FROM = "fechaDesde";
    private static final String TO = "fechaHasta";
    private static final String ENTERPRISE_NAME = "empresa";
    private static final String POSITION = "posicion";
    private static final String DESCRIPTION = "descripcion";

    private static final String TEST_USER_EMAIL = "johnlennon@gmail.com";
    private static final Date NEW_FROM = Date.valueOf("1972-09-09");
    private static final Date NEW_TO = Date.valueOf("1974-05-31");;
    private static final String NEW_ENTERPRISE_NAME = "Empresa 1";
    private static final String NEW_POSITION = "Hokage";
    private static final String NEW_DESCRIPTION = "El admin de la aldea";

    private static final long FIRST_ID = 1;
    private static final long EXISTING_USER_ID = 1;

    private static final String EXISTING_ENTERPRISE_NAME = "Paw Inc.";
    private static final Date EXISTING_FROM = Date.valueOf("2011-11-11");
    private static final String EXISTING_POSITION = "Ceo de Paw Inc.";
    private static final String EXISTING_DESCRIPTION = "Era el CEO :)";


    @Autowired
    private ExperienceJdbcDao dao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    private User testUser;

    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        testUser = userDao.findByEmail(TEST_USER_EMAIL).get();
    }

    @Test
    public void testCreate() {
        final Experience newExperience = dao.create(testUser.getId(), NEW_FROM, NEW_TO, NEW_ENTERPRISE_NAME, NEW_POSITION, NEW_DESCRIPTION);

        Assert.assertNotNull(newExperience);
        Assert.assertEquals(1, newExperience.getUserId());
        Assert.assertEquals(NEW_FROM, newExperience.getFrom());
        Assert.assertEquals(NEW_TO, newExperience.getTo());
        Assert.assertEquals(NEW_ENTERPRISE_NAME, newExperience.getEnterpriseName());
        Assert.assertEquals(NEW_POSITION, newExperience.getPosition());
        Assert.assertEquals(NEW_DESCRIPTION, newExperience.getDescription());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, EXPERIENCE_TABLE, POSITION + " = '" + NEW_POSITION + "'"));
    }

    @Test
    public void testFindById() {
        final Optional<Experience> newExperience = dao.findById(FIRST_ID);

        Assert.assertTrue(newExperience.isPresent());
        Assert.assertEquals(FIRST_ID, newExperience.get().getId());
        Assert.assertEquals(EXISTING_USER_ID, newExperience.get().getUserId());
        Assert.assertEquals(EXISTING_FROM, newExperience.get().getFrom());
        Assert.assertNull(newExperience.get().getTo());
        Assert.assertEquals(EXISTING_ENTERPRISE_NAME, newExperience.get().getEnterpriseName());
        Assert.assertEquals(EXISTING_POSITION, newExperience.get().getPosition());
        Assert.assertEquals(EXISTING_DESCRIPTION, newExperience.get().getDescription());
    }
}
