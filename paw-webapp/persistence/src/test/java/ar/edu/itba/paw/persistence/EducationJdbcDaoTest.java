package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Education;
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
public class EducationJdbcDaoTest {

    private static final String EDUCATION_TABLE = "educacion";
    private static final String NEW_DATE_FROM = "2000-11-11";
    private static final String NEW_DATE_TO = "2005-12-12";
    private static final String NEW_TITLE = "Bachiller especializado en PAW";
    private static final String NEW_INSTITUTION = "Colegio Nuestra Seniora de PAW";
    private static final String NEW_DESCRIPTION = "Siempre me gusto mucho este colegio";
    private static final String TEST_USER_EMAIL = "johnlennon@gmail.com";
    
    @Autowired
    private EducationJdbcDao educationDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testAdd() {
        final User user = userDao.findByEmail(TEST_USER_EMAIL).get();
        final Education newEducation = educationDao.add(user.getId(), Date.valueOf(NEW_DATE_FROM), Date.valueOf(NEW_DATE_TO), NEW_TITLE, NEW_INSTITUTION, NEW_DESCRIPTION) ;

        Assert.assertNotNull(newEducation);
        Assert.assertEquals(user.getId(), newEducation.getUserId());
        Assert.assertEquals(NEW_DATE_FROM, newEducation.getDateFrom().toString());
        Assert.assertEquals(NEW_DATE_TO, newEducation.getDateTo().toString());
        Assert.assertEquals(NEW_TITLE, newEducation.getTitle());
        Assert.assertEquals(NEW_INSTITUTION, newEducation.getInstitutionName());
        Assert.assertEquals(NEW_DESCRIPTION, newEducation.getDescription());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, EDUCATION_TABLE,  "institucion = '" + NEW_INSTITUTION + "'"));
    }



    @Test
    public void testFindById() {
        final User user = userDao.findByEmail(TEST_USER_EMAIL).get();
        final Education newEducation = educationDao.add(user.getId(), Date.valueOf(NEW_DATE_FROM), Date.valueOf(NEW_DATE_TO), NEW_TITLE, NEW_INSTITUTION, NEW_DESCRIPTION) ;
        final Optional<Education> foundEducation = educationDao.findById(newEducation.getId());

        Assert.assertTrue(foundEducation.isPresent());
        Assert.assertEquals(user.getId(), foundEducation.get().getUserId());
        Assert.assertEquals(NEW_DATE_FROM, foundEducation.get().getDateFrom().toString());
        Assert.assertEquals(NEW_DATE_TO, foundEducation.get().getDateTo().toString());
        Assert.assertEquals(NEW_TITLE, foundEducation.get().getTitle());
        Assert.assertEquals(NEW_INSTITUTION, foundEducation.get().getInstitutionName());
        Assert.assertEquals(NEW_DESCRIPTION, foundEducation.get().getDescription());
    }


}
