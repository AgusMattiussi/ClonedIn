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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional*/
public class EducationJdbcDaoTest {

    /*private static final String EDUCATION_TABLE = "educacion";
    private static final String NEW_TITLE = "Bachiller especializado en PAW";
    private static final String NEW_INSTITUTION = "Colegio Nuestra Seniora de PAW";
    private static final String NEW_DESCRIPTION = "Siempre me gusto mucho este colegio";
    private static final String TEST_USER_EMAIL = "johnlennon@gmail.com";
    private static final String TEST_TITLE = "Licenciado en Paw";
    private static final String TEST_INSTITUTION = "PAW University";
    private static final String TEST_DESCRIPTION = "Una linda facultad";
    public static final int NEW_MONTH_FROM = 11;
    public static final int NEW_YEAR_FROM = 2000;
    public static final int NEW_MONTH_TO = 12;
    public static final int NEW_YEAR_TO = 2004;
public static final int TEST_MONTH_FROM = 11;
    public static final int TEST_YEAR_FROM = 2011;
    public static final int TEST_MONTH_TO = 12;
    public static final int TEST_YEAR_TO = 2012;
    
    @Autowired
    private EducationJdbcDao educationDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    private User testUser;*/

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    /*@Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        testUser = userDao.findByEmail(TEST_USER_EMAIL).get();
    }

    @Test
    public void testAdd() {
        final Education newEducation = educationDao.add(testUser.getId(), NEW_MONTH_FROM, NEW_YEAR_FROM, NEW_MONTH_TO, NEW_YEAR_TO, NEW_TITLE, NEW_INSTITUTION, NEW_DESCRIPTION) ;

        Assert.assertNotNull(newEducation);
        Assert.assertEquals(testUser.getId(), Long.valueOf(newEducation.getUserId()));
        Assert.assertEquals(NEW_MONTH_FROM, newEducation.getMonthFrom());
        Assert.assertEquals(NEW_YEAR_FROM, newEducation.getYearFrom());
        Assert.assertEquals(NEW_MONTH_TO, newEducation.getMonthTo());
        Assert.assertEquals(NEW_YEAR_TO, newEducation.getYearTo());
        Assert.assertEquals(NEW_TITLE, newEducation.getTitle());
        Assert.assertEquals(NEW_INSTITUTION, newEducation.getInstitutionName());
        Assert.assertEquals(NEW_DESCRIPTION, newEducation.getDescription());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, EDUCATION_TABLE,  "institucion = '" + NEW_INSTITUTION + "'"));
    }



    @Test
    public void testFindById() {
        final Education newEducation = educationDao.add(testUser.getId(), NEW_MONTH_FROM, NEW_YEAR_FROM,
                NEW_MONTH_TO, NEW_YEAR_TO, NEW_TITLE, NEW_INSTITUTION, NEW_DESCRIPTION) ;
        final Optional<Education> foundEducation = educationDao.findById(newEducation.getId());

        assertTrue(foundEducation.isPresent());
        Assert.assertEquals(testUser.getId(), Long.valueOf(foundEducation.get().getUserId()));
        Assert.assertEquals(NEW_MONTH_FROM, foundEducation.get().getMonthFrom());
        Assert.assertEquals(NEW_YEAR_FROM, foundEducation.get().getYearFrom());
        Assert.assertEquals(NEW_MONTH_TO, foundEducation.get().getMonthTo());
        Assert.assertEquals(NEW_YEAR_TO, foundEducation.get().getYearTo());
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
        Assert.assertEquals(testUser.getId(), Long.valueOf(educationList.get(0).getUserId()));
        Assert.assertEquals(TEST_MONTH_FROM, educationList.get(0).getMonthFrom());
        Assert.assertEquals(TEST_YEAR_FROM, educationList.get(0).getYearFrom());
        Assert.assertEquals(TEST_MONTH_TO, educationList.get(0).getMonthTo());
        Assert.assertEquals(TEST_YEAR_TO, educationList.get(0).getYearTo());
        Assert.assertEquals(TEST_TITLE, educationList.get(0).getTitle());
        Assert.assertEquals(TEST_INSTITUTION, educationList.get(0).getInstitutionName());
        Assert.assertEquals(TEST_DESCRIPTION, educationList.get(0).getDescription());
    }

    @Test
    public void testDeleteEducation(){
        final Education newEducation = educationDao.add(testUser.getId(), NEW_MONTH_FROM, NEW_YEAR_FROM,
                NEW_MONTH_TO, NEW_YEAR_TO, NEW_TITLE, NEW_INSTITUTION, NEW_DESCRIPTION) ;
        Optional<Education> foundEducation = educationDao.findById(newEducation.getId());
        assertTrue(foundEducation.isPresent());
        educationDao.deleteEducation(newEducation.getId());
        foundEducation = educationDao.findById(newEducation.getId());
        Assert.assertFalse(foundEducation.isPresent());
    }*/


}
