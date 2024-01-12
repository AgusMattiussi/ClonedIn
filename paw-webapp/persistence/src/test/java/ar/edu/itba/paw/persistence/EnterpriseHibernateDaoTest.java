package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.enums.EmployeeRanges;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class EnterpriseHibernateDaoTest {
    private static final String NEW_NAME = "newEnterpriseName";
    private static final String NEW_EMAIL = "newEnterprise@gmail.com";
    private static final String EMPTY_FIELD = "-";
    private static final String TEST_NAME = "testEnterpriseName";
    private static final String TEST_EMAIL = "enterprise@gmail.com";
    private static final String TEST_CATEGORY_DESC = "testCategory";
    private static final String UPDATED_STRING = "updatedString";

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private EnterpriseDao dao;

    private Category testCategory;
    private Enterprise testEnterprise;

    @Before
    public void setUp() {
        testCategory = new Category(TEST_CATEGORY_DESC);
        em.persist(testCategory);
        testEnterprise = new Enterprise(TEST_NAME, TEST_EMAIL, EMPTY_FIELD, EMPTY_FIELD, testCategory, EMPTY_FIELD,
                null, EMPTY_FIELD, EMPTY_FIELD, null);
        em.persist(testEnterprise);
    }

    @Test
    public void testCreate() {
        final Enterprise newEnterprise = dao.create(NEW_EMAIL, NEW_NAME, EMPTY_FIELD, EMPTY_FIELD, testCategory, EmployeeRanges.FROM_51_TO_100,
                null, EMPTY_FIELD, EMPTY_FIELD);

        assertNotNull(newEnterprise);
        assertEquals(NEW_EMAIL, newEnterprise.getEmail());
        assertEquals(NEW_NAME, newEnterprise.getName());
        assertEquals(EMPTY_FIELD, newEnterprise.getLocation());
        assertEquals(testCategory, newEnterprise.getCategory());
        assertEquals(EMPTY_FIELD, newEnterprise.getDescription());
    }

    @Test
    public void testFindById() {
        final Optional<Enterprise> newEnterprise = dao.findById(testEnterprise.getId());

        assertTrue(newEnterprise.isPresent());
        assertEquals(testEnterprise, newEnterprise.get());
        assertEquals(TEST_EMAIL, newEnterprise.get().getEmail());
        assertEquals(TEST_NAME, newEnterprise.get().getName());
    }

    @Test
    public void testFindByEmail() {
        final Optional<Enterprise> newEnterprise = dao.findByEmail(TEST_EMAIL);

        assertTrue(newEnterprise.isPresent());
        assertEquals(testEnterprise, newEnterprise.get());
        assertEquals(TEST_EMAIL, newEnterprise.get().getEmail());
        assertEquals(TEST_NAME, newEnterprise.get().getName());
    }

    @Test
    public void testEnterpriseExists(){
        assertTrue(dao.enterpriseExists(TEST_EMAIL));
    }

    @Test
    public void testUpdateName(){
        dao.updateName(testEnterprise.getId(), UPDATED_STRING);
        em.refresh(testEnterprise);
        assertEquals(UPDATED_STRING, testEnterprise.getName());
    }

    @Test
    public void testUpdateDescription(){
        dao.updateDescription(testEnterprise.getId(), UPDATED_STRING);
        em.refresh(testEnterprise);
        assertEquals(UPDATED_STRING, testEnterprise.getDescription());
    }

    @Test
    public void testUpdateLocation(){
        dao.updateLocation(testEnterprise.getId(), UPDATED_STRING);
        em.refresh(testEnterprise);
        assertEquals(UPDATED_STRING, testEnterprise.getLocation());
    }
    @Test
    public void testUpdateWorkers(){
        dao.updateWorkers(testEnterprise.getId(), EmployeeRanges.FROM_1_TO_10);
        em.refresh(testEnterprise);
        assertEquals(EmployeeRanges.FROM_1_TO_10.getStringValue(), testEnterprise.getWorkers());
    }

    @Test
    public void testUpdateYear(){
        dao.updateYear(testEnterprise.getId(), 0);
        em.refresh(testEnterprise);
        assertEquals(0, (long) testEnterprise.getYear());
    }
    @Test
    public void testUpdateLink(){
        dao.updateLink(testEnterprise.getId(), UPDATED_STRING);
        em.refresh(testEnterprise);
        assertEquals(UPDATED_STRING, testEnterprise.getLink());
    }
}
