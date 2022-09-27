package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
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
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class JobOfferJdbcDaoTest {

    private static final String JOB_OFFER_TABLE = "ofertaLaboral";
    private static final String POSITION = "posicion";
    private static final String TEST_ENTERPRISE = "empresaurio@gmail.com";
    private static final String TEST_CATEGORY = "testCategory";
    private static final String NEW_POSITION = "CEO de PAW";
    private static final String NEW_DESCRIPTION = "Venite a PAW que vas a ser feliz pibe";
    private static final BigDecimal NEW_SALARY = BigDecimal.valueOf(9999999.99);
    private static final String TEST_POSITION = "testPosition";
    private static final String TEST_DESCRIPTION = "testdescription";
    private static final String TEST_MODALITY = "remoto";
    private static final BigDecimal TEST_SALARY = BigDecimal.valueOf(1000.99);

    @Autowired
    private JobOfferJdbcDao jobOfferJdbcDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    private Enterprise enterprise;
    private Category category;

    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        enterprise = enterpriseDao.findByEmail(TEST_ENTERPRISE).get();
        category = categoryDao.findByName(TEST_CATEGORY).get();
    }

    @Test
    public void testCreate() {
        final JobOffer newJobOffer = jobOfferJdbcDao.create(enterprise.getId(), category.getId(), NEW_POSITION, NEW_DESCRIPTION, NEW_SALARY, TEST_MODALITY);

        Assert.assertNotNull(newJobOffer);
        Assert.assertEquals(enterprise.getId(), newJobOffer.getEnterpriseID());
        Assert.assertEquals(category.getId(), newJobOffer.getCategoryID());
        Assert.assertEquals(NEW_POSITION, newJobOffer.getPosition());
        Assert.assertEquals(NEW_DESCRIPTION, newJobOffer.getDescription());
        Assert.assertEquals(NEW_SALARY, newJobOffer.getSalary());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, JOB_OFFER_TABLE,  POSITION + " = '" + NEW_POSITION + "'"));
        Assert.assertEquals(TEST_MODALITY, newJobOffer.getModality());
    }

    @Test(expected = InvalidParameterException.class)
    public void testInvalidCreate() {
        BigDecimal invalidSalary = BigDecimal.valueOf(-1000);
        jobOfferJdbcDao.create(enterprise.getId(), category.getId(), NEW_POSITION, NEW_DESCRIPTION, invalidSalary, TEST_MODALITY);
    }

    @Test
    public void testFindByID(){
        final JobOffer existingJobOffer = jobOfferJdbcDao.findById(1).orElse(null);

        Assert.assertNotNull(existingJobOffer);
        Assert.assertEquals(1, existingJobOffer.getId());
        Assert.assertEquals(enterprise.getId(), existingJobOffer.getEnterpriseID());
        Assert.assertEquals(category.getId(), existingJobOffer.getCategoryID());
        Assert.assertEquals(TEST_POSITION, existingJobOffer.getPosition());
        Assert.assertEquals(TEST_DESCRIPTION, existingJobOffer.getDescription());
        Assert.assertEquals(TEST_SALARY, existingJobOffer.getSalary());
        Assert.assertEquals(TEST_MODALITY, existingJobOffer.getModality());
    }

    @Test
    public void testFindByEnterpriseID(){
        final List<JobOffer> jobOfferList = jobOfferJdbcDao.findByEnterpriseId(enterprise.getId());

        Assert.assertNotNull(jobOfferList);
        Assert.assertFalse(jobOfferList.isEmpty());
        Assert.assertEquals(1, jobOfferList.size());
        Assert.assertEquals(1, jobOfferList.get(0).getId());
        Assert.assertEquals(enterprise.getId(), jobOfferList.get(0).getEnterpriseID());
        Assert.assertEquals(category.getId(), jobOfferList.get(0).getCategoryID());
        Assert.assertEquals(TEST_POSITION, jobOfferList.get(0).getPosition());
        Assert.assertEquals(TEST_DESCRIPTION, jobOfferList.get(0).getDescription());
        Assert.assertEquals(TEST_SALARY, jobOfferList.get(0).getSalary());
        Assert.assertEquals(TEST_MODALITY, jobOfferList.get(0).getModality());
    }

}
