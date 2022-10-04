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
import java.util.List;
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
    public static final int NEW_MONTH_FROM = 11;
    public static final int NEW_YEAR_FROM = 2000;
    public static final Integer NEW_MONTH_TO = 12;
    public static final Integer NEW_YEAR_TO = 2004;
    private static final String NEW_ENTERPRISE_NAME = "Empresa 1";
    private static final String NEW_POSITION = "Hokage";
    private static final String NEW_DESCRIPTION = "El admin de la aldea";

    private static final long FIRST_ID = 1;
    private static final long EXISTING_USER_ID = 1;

    private static final String EXISTING_ENTERPRISE_NAME = "Paw Inc.";
    public static final int EXISTING_MONTH_FROM = 11;
    public static final int EXISTING_YEAR_FROM = 2011;
    public static final Integer EXISTING_MONTH_TO = 12;
    public static final Integer EXISTING_YEAR_TO = 2012;
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
        final Experience newExperience = dao.create(testUser.getId(), NEW_MONTH_FROM, NEW_YEAR_FROM, NEW_MONTH_TO, NEW_YEAR_TO,
                NEW_ENTERPRISE_NAME, NEW_POSITION, NEW_DESCRIPTION);

        Assert.assertNotNull(newExperience);
        Assert.assertEquals(1, newExperience.getUserId());
        Assert.assertEquals(NEW_MONTH_FROM, newExperience.getMonthFrom());
        Assert.assertEquals(NEW_YEAR_FROM, newExperience.getYearFrom());
        Assert.assertEquals(NEW_MONTH_TO, newExperience.getMonthTo());
        Assert.assertEquals(NEW_YEAR_TO, newExperience.getYearTo());
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
        Assert.assertEquals(EXISTING_MONTH_FROM, newExperience.get().getMonthFrom());
        Assert.assertEquals(EXISTING_YEAR_FROM, newExperience.get().getYearFrom());
        Assert.assertEquals(EXISTING_MONTH_TO, newExperience.get().getMonthTo());
        Assert.assertEquals(EXISTING_YEAR_TO, newExperience.get().getYearTo());
        Assert.assertEquals(EXISTING_ENTERPRISE_NAME, newExperience.get().getEnterpriseName());
        Assert.assertEquals(EXISTING_POSITION, newExperience.get().getPosition());
        Assert.assertEquals(EXISTING_DESCRIPTION, newExperience.get().getDescription());
    }

    @Test
    public void testFindByUserID() {
        final List<Experience> experienceList = dao.findByUserId(testUser.getId());

        Assert.assertNotNull(experienceList);
        Assert.assertEquals(1, experienceList.size());
        Assert.assertEquals(EXISTING_USER_ID, experienceList.get(0).getUserId());
        Assert.assertEquals(EXISTING_MONTH_FROM, experienceList.get(0).getMonthFrom());
        Assert.assertEquals(EXISTING_YEAR_FROM, experienceList.get(0).getYearFrom());
        Assert.assertEquals(EXISTING_MONTH_TO, experienceList.get(0).getMonthTo());
        Assert.assertEquals(EXISTING_YEAR_TO, experienceList.get(0).getYearTo());
        Assert.assertEquals(EXISTING_ENTERPRISE_NAME, experienceList.get(0).getEnterpriseName());
        Assert.assertEquals(EXISTING_POSITION, experienceList.get(0).getPosition());
        Assert.assertEquals(EXISTING_DESCRIPTION, experienceList.get(0).getDescription());
    }

    @Test
    public void testDeleteExperience(){
        final Experience newExperience = dao.create(testUser.getId(), NEW_MONTH_FROM, NEW_YEAR_FROM, NEW_MONTH_TO, NEW_YEAR_TO,
                NEW_ENTERPRISE_NAME, NEW_POSITION, NEW_DESCRIPTION);
        Optional<Experience> foundExperience = dao.findById(newExperience.getId());
        Assert.assertTrue(foundExperience.isPresent());
        dao.deleteExperience(newExperience.getId());
        foundExperience = dao.findById(newExperience.getId());
        Assert.assertFalse(foundExperience.isPresent());
    }
}
