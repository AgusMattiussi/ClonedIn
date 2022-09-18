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
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class EducationJdbcDaoTest {

    private static final String EDUCATION_TABLE = "educacion";
    private static final Date NEW_DATE_FROM = Date.valueOf("2000-11-11");
    private static final Date NEW_DATE_TO = Date.valueOf("2005-12-12");
    private static final String NEW_TITLE = "Bachiller especializado en PAW";
    private static final String NEW_INSTITUTION = "Colegio Nuestra Seniora de PAW";
    private static final String NEW_DESCRIPTION = "Siempre me gusto mucho este colegio";
    private static final String TEST_USER_EMAIL = "johnlennon@gmail.com";
    private static final Date TEST_DATE_FROM = Date.valueOf("2011-11-11");
    private static final String TEST_TITLE = "Licenciado en Paw";
    private static final String TEST_INSTITUTION = "PAW University";
    private static final String TEST_DESCRIPTION = "Una linda facultad";
    
    @Autowired
    private EducationJdbcDao educationDao;
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
    public void testAdd() {
        final Education newEducation = educationDao.add(testUser.getId(), NEW_DATE_FROM, NEW_DATE_TO, NEW_TITLE, NEW_INSTITUTION, NEW_DESCRIPTION) ;

        Assert.assertNotNull(newEducation);
        Assert.assertEquals(testUser.getId(), newEducation.getUserId());
        Assert.assertEquals(NEW_DATE_FROM, newEducation.getDateFrom());
        Assert.assertEquals(NEW_DATE_TO, newEducation.getDateTo());
        Assert.assertEquals(NEW_TITLE, newEducation.getTitle());
        Assert.assertEquals(NEW_INSTITUTION, newEducation.getInstitutionName());
        Assert.assertEquals(NEW_DESCRIPTION, newEducation.getDescription());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, EDUCATION_TABLE,  "institucion = '" + NEW_INSTITUTION + "'"));
    }



    @Test
    public void testFindById() {
        final Education newEducation = educationDao.add(testUser.getId(), NEW_DATE_FROM, NEW_DATE_TO, NEW_TITLE, NEW_INSTITUTION, NEW_DESCRIPTION) ;
        final Optional<Education> foundEducation = educationDao.findById(newEducation.getId());

        Assert.assertTrue(foundEducation.isPresent());
        Assert.assertEquals(testUser.getId(), foundEducation.get().getUserId());
        Assert.assertEquals(NEW_DATE_FROM, foundEducation.get().getDateFrom());
        Assert.assertEquals(NEW_DATE_TO, foundEducation.get().getDateTo());
        Assert.assertEquals(NEW_TITLE, foundEducation.get().getTitle());
        Assert.assertEquals(NEW_INSTITUTION, foundEducation.get().getInstitutionName());
        Assert.assertEquals(NEW_DESCRIPTION, foundEducation.get().getDescription());
    }

    @Test
    public void testFindByUserId(){
        final List<Education> educationList = educationDao.findByUserId(testUser.getId());

        Assert.assertNotNull(educationList);
        Assert.assertFalse(educationList.isEmpty());
        Assert.assertEquals(1, educationList.size());
        Assert.assertEquals(testUser.getId(), educationList.get(0).getUserId());
        Assert.assertEquals(TEST_DATE_FROM, educationList.get(0).getDateFrom());
        Assert.assertNull(educationList.get(0).getDateTo());
        Assert.assertEquals(TEST_TITLE, educationList.get(0).getTitle());
        Assert.assertEquals(TEST_INSTITUTION, educationList.get(0).getInstitutionName());
        Assert.assertEquals(TEST_DESCRIPTION, educationList.get(0).getDescription());
    }


}
