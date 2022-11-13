package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.*;
import ar.edu.itba.paw.models.ids.ContactId;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ContactHibernateDaoTest {

    private static final String ENTERPRISE_ID = "idEmpresa";
    private static final String USER_ID = "idUsuario";
    private static final String TEST_USER_EMAIL = "johnlennon@gmail.com";
    private static final String TEST_ENTERPRISE_EMAIL = "empresaurio@gmail.com";
    private static final String NEW_ENTERPRISE_NAME = "Empresa1";
    private static final String NEW_ENTERPRISE_EMAIL = "empresa1@gmail.com";
    private static final String NEW_ENTERPRISE_PASSWORD = "pass123";
    private static final String NEW_ENTERPRISE_LOCATION = "Calle Falsa para Empresas 123";
    private static final Integer NEW_ENTERPRISE_YEAR = 2008;
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
    public static final String TEST_CATEGORY_DESC = "unaCategoria";
    public static final String EMPTY_FIELD = "-";
    public static final String NEW_JOB_OFFER_POSITION = "position";
    public static final BigDecimal NEW_JOB_OFFER_SALARY = BigDecimal.valueOf(1000);
    public static final String NEW_JOB_OFFER_MODALITY = JobOfferModalities.NOT_SPECIFIED.getModality();
    public static final String NEW_OFFER_AVAILABILITY = JobOfferAvailability.ACTIVE.getStatus();
    public static final String NEW_OFFER_STATUS = JobOfferStatus.PENDING.getStatus();
    public static final FilledBy NEW_JOB_OFFER_FILLED_BY = FilledBy.ENTERPRISE;


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ContactDao contactDao;

    private Category testCategory;
    private User testUser;
    private User newUser;
    private Enterprise testEnterprise;
    private Enterprise newEnterprise;
    private JobOffer testJobOffer;
    private JobOffer testJobOffer2;
    private Contact testContact;
    private Contact newContact = null;

    @Before
    public void setUp() {
        testCategory = new Category(TEST_CATEGORY_DESC);
        em.persist(testCategory);

        testUser = new User(TEST_USER_EMAIL, EMPTY_FIELD, EMPTY_FIELD, EMPTY_FIELD, testCategory, EMPTY_FIELD, EMPTY_FIELD,
                EMPTY_FIELD, Visibility.VISIBLE.getValue(), null);
        em.persist(testUser);

        testEnterprise = new Enterprise(NEW_ENTERPRISE_NAME, NEW_ENTERPRISE_EMAIL, NEW_ENTERPRISE_PASSWORD, EMPTY_FIELD,
                testCategory, EMPTY_FIELD, NEW_ENTERPRISE_YEAR, EMPTY_FIELD, EMPTY_FIELD, null);
        em.persist(testEnterprise);

        testJobOffer = new JobOffer(testEnterprise, testCategory, NEW_JOB_OFFER_POSITION, EMPTY_FIELD, NEW_JOB_OFFER_SALARY,
                NEW_JOB_OFFER_MODALITY, NEW_OFFER_AVAILABILITY);
        em.persist(testJobOffer);

        testJobOffer2 = new JobOffer(testEnterprise, testCategory, NEW_JOB_OFFER_POSITION, EMPTY_FIELD, NEW_JOB_OFFER_SALARY,
                NEW_JOB_OFFER_MODALITY, NEW_OFFER_AVAILABILITY);
        em.persist(testJobOffer2);

        testContact = new Contact(testUser, testEnterprise, testJobOffer, FilledBy.ENTERPRISE, Date.from(Instant.now()));
        em.persist(testContact);
    }

    @After
    public void restore(){
        em.remove(testContact);
        em.remove(testCategory);
        em.remove(testUser);
        em.remove(testEnterprise);
        em.remove(testJobOffer);
        em.remove(testJobOffer2);

        if(newContact != null)
            em.remove(newContact);
    }

    @Test
    public void testAddContact(){
        newContact = contactDao.addContact(testEnterprise,  testUser, testJobOffer2, FilledBy.ENTERPRISE);

        Contact foundContact = em.find(Contact.class, new ContactId(testUser.getId(), testJobOffer2.getId()));

        assertNotNull(foundContact);
        assertEquals(foundContact, newContact);
    }

    @Test
    public void testGetEnterprisesForUser(){
        List<Enterprise> enterpriseList = contactDao.getEnterprisesForUser(testUser, NEW_JOB_OFFER_FILLED_BY);

        assertFalse(enterpriseList.isEmpty());
        assertEquals(1, enterpriseList.size());
        assertEquals(testEnterprise, enterpriseList.get(0));
    }

    @Test
    public void testGetUsersForEnterprise(){
        List<User> userList = contactDao.getUsersForEnterprise(testEnterprise, NEW_JOB_OFFER_FILLED_BY);

        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        assertEquals(testUser, userList.get(0));
    }

    @Test
    public void testGetContactsForUser(){
        List<Contact> contactList = contactDao.getContactsForUser(testUser, NEW_JOB_OFFER_FILLED_BY);

        assertFalse(contactList.isEmpty());
        assertEquals(1 , contactList.size());
        assertEquals(testContact, contactList.get(0));
    }

    @Test
    public void testGetContactsForEnterprise(){
        List<Contact> contactList = contactDao.getContactsForEnterprise(testEnterprise, NEW_JOB_OFFER_FILLED_BY);

        assertFalse(contactList.isEmpty());
        assertEquals(1 , contactList.size());
        assertEquals(testContact, contactList.get(0));
    }

    @Test
    public void testGetContactsForJobOffer(){
        List<Contact> contactList = contactDao.getContactsForJobOffer(testJobOffer, NEW_JOB_OFFER_FILLED_BY);

        assertFalse(contactList.isEmpty());
        assertEquals(1 , contactList.size());
        assertEquals(testContact, contactList.get(0));
    }

    @Test
    public void testGetContactsForEnterpriseAndUser(){
        List<Contact> contactList = contactDao.getContactsForEnterpriseAndUser(testEnterprise, testUser, NEW_JOB_OFFER_FILLED_BY);

        assertFalse(contactList.isEmpty());
        assertEquals(1 , contactList.size());
        assertEquals(testContact, contactList.get(0));
    }

    @Test
    public void testAlreadyContactedTrue(){
        boolean contacted = contactDao.alreadyContacted(testUser.getId(), testJobOffer.getId());

        assertTrue(contacted);
    }

    @Test
    public void testAlreadyContactedFalse(){
        boolean contacted = contactDao.alreadyContacted(testUser.getId(), testJobOffer2.getId());

        assertFalse(contacted);
    }

    @Test
    public void testAlreadyContactedByEnterprise(){
        boolean contacted = contactDao.alreadyContactedByEnterprise(testUser.getId(), testEnterprise.getId());

        assertTrue(contacted);
    }

    @Test
    public void testGetStatus(){
        Optional<String> status = contactDao.getStatus(testUser, testJobOffer);

        assertTrue(status.isPresent());
        assertEquals(NEW_OFFER_STATUS, status.get());
    }

    @Test
    public void testAcceptJobOffer(){
        boolean accepted = contactDao.acceptJobOffer(testUser, testJobOffer);

        Contact found = em.find(Contact.class, new ContactId(testUser.getId(), testJobOffer.getId()));
        em.refresh(found);

        assertTrue(accepted);
        assertNotNull(found);
        assertEquals(JobOfferStatus.ACCEPTED.getStatus(), found.getStatus());
    }

    @Test
    public void testRejectJobOffer(){
        boolean rejected = contactDao.rejectJobOffer(testUser, testJobOffer);

        Contact found = em.find(Contact.class, new ContactId(testUser.getId(), testJobOffer.getId()));
        em.refresh(found);

        assertTrue(rejected);
        assertNotNull(found);
        assertEquals(JobOfferStatus.DECLINED.getStatus(), found.getStatus());
    }

    @Test
    public void testCancelJobOffer(){
        boolean cancelled = contactDao.cancelJobOffer(testUser, testJobOffer);

        Contact found = em.find(Contact.class, new ContactId(testUser.getId(), testJobOffer.getId()));
        em.refresh(found);

        assertTrue(cancelled);
        assertNotNull(found);
        assertEquals(JobOfferStatus.CANCELLED.getStatus(), found.getStatus());
    }

    @Test
    public void testCancelJobOfferForEveryone(){
        boolean cancelled = contactDao.cancelJobOfferForEveryone(testJobOffer);

        Contact found = em.find(Contact.class, new ContactId(testUser.getId(), testJobOffer.getId()));
        em.refresh(found);

        assertTrue(cancelled);
        assertNotNull(found);
        assertEquals(JobOfferStatus.CANCELLED.getStatus(), found.getStatus());
    }

    @Test
    public void testCloseJobOffer(){
        boolean closed = contactDao.closeJobOffer(testUser, testJobOffer);

        Contact found = em.find(Contact.class, new ContactId(testUser.getId(), testJobOffer.getId()));
        em.refresh(found);

        assertTrue(closed);
        assertNotNull(found);
        assertEquals(JobOfferStatus.CLOSED.getStatus(), found.getStatus());
    }

    @Test
    public void testCloseJobOfferForEveryone(){
        boolean closed = contactDao.closeJobOfferForEveryone(testJobOffer);

        Contact found = em.find(Contact.class, new ContactId(testUser.getId(), testJobOffer.getId()));
        em.refresh(found);

        assertTrue(closed);
        assertNotNull(found);
        assertEquals(JobOfferStatus.CLOSED.getStatus(), found.getStatus());
    }

    @Test
    public void testGetContactsCountForEnterprise(){
        long count = contactDao.getContactsCountForEnterprise(testEnterprise);

        assertEquals(1, count);
    }

    @Test
    public void testGetContactsCountForUser(){
        long count = contactDao.getContactsCountForUser(testUser);

        assertEquals(1, count);
    }
}
