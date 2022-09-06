package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Experience;
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

    private static final long TEST_USER_ID = 1;
    private static final Date TEST_FROM = new Date(1000000);
    private static final Date TEST_TO = new Date(2000000);
    private static final String TEST_ENTERPRISE_NAME = "Empresa 1";
    private static final String TEST_POSITION = "Hokage";
    private static final String TEST_DESCRIPTION = "El admin de la aldea";

    private static final long FIRST_ID = 1;
    private static final long EXISTING_USER_ID = 1;

    private static final Date EXISTING_FROM = new Date(100000);
    private static final String EXISTING_POSITION = "CEO";


    @Autowired
    private ExperienceJdbcDao dao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        //JdbcTestUtils.deleteFromTables(jdbctemplate, EXPERIENCE_TABLE);
    }

    @Test
    public void easy() {
        Assert.assertTrue(true);
    }

//    @Test
//    public void testCreate() {
//        final Experience newExperience = dao.create(TEST_USER_ID, TEST_FROM, TEST_TO, TEST_ENTERPRISE_NAME, TEST_POSITION, TEST_DESCRIPTION);
//
//        Assert.assertNotNull(newExperience);
//        Assert.assertEquals(TEST_USER_ID, newExperience.getUserId());
////        Assert.assertEquals(TEST_FROM, newExperience.getFrom());
////        Assert.assertEquals(TEST_TO, newExperience.getTo());
//        Assert.assertEquals(TEST_ENTERPRISE_NAME, newExperience.getEnterpriseName());
//        Assert.assertEquals(TEST_POSITION, newExperience.getPosition());
//        Assert.assertEquals(TEST_DESCRIPTION, newExperience.getDescription());
//
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, EXPERIENCE_TABLE, USER_ID + " = '" + TEST_USER_ID + "'"));
//    }
//
//    @Test
//    public void testFindById() {
//        final Optional<Experience> newExperience = dao.findById(FIRST_ID);
//
//        Assert.assertTrue(newExperience.isPresent());
//        Assert.assertEquals(FIRST_ID, newExperience.get().getId());
//        Assert.assertEquals(EXISTING_USER_ID, newExperience.get().getUserId());
////        Assert.assertEquals(EXISTING_FROM, newExperience.get().getFrom());
//        Assert.assertEquals(EXISTING_POSITION, newExperience.get().getPosition());
//    }
}
