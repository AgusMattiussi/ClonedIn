package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.*;
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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class ContactJdbcDaoTest {

    private static final String CONTACT_TABLE = "contactado";
    private static final String ENTERPRISE_ID = "idEmpresa";
    private static final String USER_ID = "idUsuario";
    private static final String TEST_USER_EMAIL = "johnlennon@gmail.com";
    private static final String TEST_ENTERPRISE_EMAIL = "empresaurio@gmail.com";
    private static final String NEW_ENTERPRISE_NAME = "Empresa1";
    private static final String NEW_ENTERPRISE_EMAIL = "empresa1@gmail.com";
    private static final String NEW_ENTERPRISE_PASSWORD = "pass123";
    private static final String NEW_ENTERPRISE_LOCATION = "Calle Falsa para Empresas 123";
    private static final long NEW_ENTERPRISE_CATEGORY_ID_FK = 1;
    private static final String NEW_ENTERPRISE_DESCRIPTION = "La mejor empresa del mundo";
    private static final String NEW_USER_NAME = "John Doe";
    private static final String NEW_USER_EMAIL = "johndoe@gmail.com";
    private static final String NEW_USER_PASSWORD = "pass123";
    private static final String NEW_USER_LOCATION = "Calle Falsa 123";
    private static final String NEW_USER_CATEGORY_NAME = "testCategory";
    private static final String NEW_USER_CURRENT_POSITION = "CEO de PAW";
    private static final String NEW_USER_DESCRIPTION = "Un tipo muy laburante";
    private static final String NEW_USER_EDUCATION = "Licenciado en la Universidad de la Calle";
    private static final String TEST_SKILL = "unaskill";
    private static final long NON_EXISTING_JOB_OFFER_ID = 777;
    public static final String STATUS_PENDING = "pendiente";
    public static final String STATUS_ACCEPTED = "aceptada";
    public static final String STATUS_REJECTED = "rechazada";

    @Autowired
    private ContactJdbcDao contactJdbcDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private JobOfferDao jobOfferDao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    private User testUser;
    private User newUser;
    private Enterprise testEnterprise;
    private Enterprise newEnterprise;
    private JobOffer testJobOffer;
    private JobOfferWithStatus testJobOfferWithStatus;
    private JobOfferStatusUserData testJobOfferWithStatusUserData;
    private JobOfferStatusEnterpriseData testJobOfferWithStatusEnterpriseData;

    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        testUser = userDao.findByEmail(TEST_USER_EMAIL).get();
        testEnterprise = enterpriseDao.findByEmail(TEST_ENTERPRISE_EMAIL).get();
        testJobOffer = jobOfferDao.findById(1).get();
        testJobOfferWithStatus = new JobOfferWithStatus(testJobOffer.getId(), testJobOffer.getEnterpriseID(), testJobOffer.getCategory(), testJobOffer.getPosition(),
                testJobOffer.getDescription(), testJobOffer.getSalary(), testJobOffer.getModality(), STATUS_PENDING);
        testJobOfferWithStatusUserData = new JobOfferStatusUserData(testJobOffer.getId(), testJobOffer.getEnterpriseID(), testJobOffer.getCategory(), testJobOffer.getPosition(),
                testJobOffer.getDescription(), testJobOffer.getSalary(), testJobOffer.getModality(), STATUS_PENDING, testUser.getName(), 1);
        testJobOfferWithStatusEnterpriseData = new JobOfferStatusEnterpriseData(testJobOffer.getId(), testJobOffer.getEnterpriseID(), testJobOffer.getCategory(), testJobOffer.getPosition(),
                testJobOffer.getDescription(), testJobOffer.getSalary(), testJobOffer.getModality(), STATUS_PENDING, testEnterprise.getName());
        newEnterprise = enterpriseDao.create(NEW_ENTERPRISE_EMAIL, NEW_ENTERPRISE_PASSWORD, NEW_ENTERPRISE_NAME, NEW_ENTERPRISE_LOCATION, NEW_USER_CATEGORY_NAME, NEW_ENTERPRISE_DESCRIPTION);
        newUser = userDao.create(NEW_USER_EMAIL, NEW_USER_PASSWORD, NEW_USER_NAME, NEW_USER_LOCATION, NEW_USER_CATEGORY_NAME, NEW_USER_CURRENT_POSITION, NEW_USER_DESCRIPTION, NEW_USER_EDUCATION) ;
    }

    @Test
    public void testGetEnterprisesForUser() {
        final List<Enterprise> enterpriseList = contactJdbcDao.getEnterprisesForUser(testUser.getId());

        Assert.assertEquals(1, enterpriseList.size());
        Assert.assertEquals(testEnterprise.getId(), enterpriseList.get(0).getId());
    }

    @Test
    public void testGetUsersForEnterprise() {
        final List<User> userList = contactJdbcDao.getUsersForEnterprise(testEnterprise.getId());

        Assert.assertEquals(1, userList.size());
        Assert.assertEquals(testUser.getId(), userList.get(0).getId());
    }

//    @Test
//    public void addContactTest() {
//        contactJdbcDao.addContact(newEnterprise.getId(), newUser.getId(), testEnterprise.getId());
//
//        final List<Enterprise> enterpriseList = contactJdbcDao.getEnterprisesForUser(newUser.getId());
//        final List<User> userList = contactJdbcDao.getUsersForEnterprise(newEnterprise.getId());
//
//        Assert.assertEquals(1, enterpriseList.size());
//        Assert.assertTrue(enterpriseList.contains(newEnterprise));
//        Assert.assertEquals(1, userList.size());
//        Assert.assertTrue(userList.contains(newUser));
//    }

    @Test
    public void testGetJobOffersWithStatusForUser() {
        final List<JobOfferWithStatus> jobOfferList = contactJdbcDao.getJobOffersWithStatusForUser(testUser.getId());

        Assert.assertNotNull(jobOfferList);
        Assert.assertFalse(jobOfferList.isEmpty());
        Assert.assertEquals(1, jobOfferList.size());
        Assert.assertTrue(jobOfferList.contains(testJobOfferWithStatus));
    }


//    @Test
//    public void testGetJobOffersWithStatusUserData() {
//        final List<JobOfferStatusUserData> jobOfferList = contactJdbcDao.getJobOffersWithStatusUserData(testEnterprise.getId(), 0, 8,"");
//
//        Assert.assertNotNull(jobOfferList);
//        Assert.assertFalse(jobOfferList.isEmpty());
//        Assert.assertEquals(1, jobOfferList.size());
//        Assert.assertTrue(jobOfferList.contains(testJobOfferWithStatusUserData));
//    }

//    @Test
//    public void testGetJobOffersWithStatusEnterpriseData() {
//        final List<JobOfferStatusEnterpriseData> jobOfferList = contactJdbcDao.getJobOffersWithStatusEnterpriseData(testUser.getId(), 0, 8, "");
//
//        Assert.assertNotNull(jobOfferList);
//        Assert.assertFalse(jobOfferList.isEmpty());
//        Assert.assertEquals(1, jobOfferList.size());
//        Assert.assertTrue(jobOfferList.contains(testJobOfferWithStatusEnterpriseData));
//    }


    @Test
    public void testGetStatus(){
        final String status = contactJdbcDao.getStatus(testUser.getId(), testJobOffer.getId());
        Assert.assertNotNull(status);
        Assert.assertFalse(status.isEmpty());
        Assert.assertEquals(STATUS_PENDING, status);
    }

    @Test
    public void testAlreadyContactedTrue(){
        final boolean contacted = contactJdbcDao.alreadyContacted(testUser.getId(), testJobOffer.getId());
        Assert.assertTrue(contacted);
    }

    @Test
    public void testAlreadyContactedFalse(){
        final boolean contacted = contactJdbcDao.alreadyContacted(testUser.getId(), NON_EXISTING_JOB_OFFER_ID);
        Assert.assertFalse(contacted);
    }

    @Test
    public void testAcceptJobOffer(){
        contactJdbcDao.acceptJobOffer(testUser.getId(), testJobOffer.getId());
        final String status = contactJdbcDao.getStatus(testUser.getId(), testJobOffer.getId());

        Assert.assertNotNull(status);
        Assert.assertFalse(status.isEmpty());
        Assert.assertEquals(STATUS_ACCEPTED, status);
    }

    @Test
    public void testRejectJobOffer(){
        contactJdbcDao.rejectJobOffer(testUser.getId(), testJobOffer.getId());
        final String status = contactJdbcDao.getStatus(testUser.getId(), testJobOffer.getId());

        Assert.assertNotNull(status);
        Assert.assertFalse(status.isEmpty());
        Assert.assertEquals(STATUS_REJECTED, status);
    }
}
