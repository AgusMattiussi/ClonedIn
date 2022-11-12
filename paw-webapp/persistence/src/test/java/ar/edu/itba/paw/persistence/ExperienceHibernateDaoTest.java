package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class ExperienceHibernateDaoTest {
    private static final String TEST_USER_EMAIL = "user@email.com";
    private static final String TEST_CATEGORY_DESC = "testCategoryDescription";
    private static final String EMPTY_FIELD = "";
    public static final int NEW_MONTH_FROM = 11;
    public static final int NEW_YEAR_FROM = 2000;
    public static final Integer NEW_MONTH_TO = 12;
    public static final Integer NEW_YEAR_TO = 2004;
    private static final String NEW_ENTERPRISE_NAME = "newEnterprise";
    private static final String NEW_POSITION = "newPosition";
    private static final String NEW_DESCRIPTION = "newDescription";
    private static final long ID = 1;
    private static final long EXISTING_USER_ID = 1;
    private static final String EXISTING_ENTERPRISE_NAME = "testEnterpriseName";
    public static final int EXISTING_MONTH_FROM = 11;
    public static final int EXISTING_YEAR_FROM = 2011;
    public static final Integer EXISTING_MONTH_TO = 12;
    public static final Integer EXISTING_YEAR_TO = 2012;
    private static final String EXISTING_POSITION = "testPosition";
    private static final String EXISTING_DESCRIPTION = "testDescription";
    public static final long USER_EXPERIENCE_LIST_SIZE = 1;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ExperienceDao dao;

    private User testUser;
    private Experience testExperience;

    @Before
    public void setUp() {
        Category testCategory = new Category(TEST_CATEGORY_DESC);
        em.persist(testCategory);

        testUser = new User(TEST_USER_EMAIL, EMPTY_FIELD, EMPTY_FIELD, EMPTY_FIELD, testCategory, EMPTY_FIELD,
                EMPTY_FIELD, EMPTY_FIELD, Visibility.VISIBLE.getValue(), null);
        em.persist(testUser);

        testExperience = new Experience(testUser, EXISTING_MONTH_FROM, EXISTING_YEAR_FROM, EXISTING_MONTH_TO, EXISTING_YEAR_TO, EXISTING_ENTERPRISE_NAME, EXISTING_POSITION, EXISTING_DESCRIPTION);
        em.persist(testExperience);
    }

    @Test
    public void testCreate() {
        final Experience newExperience = dao.create(testUser, NEW_MONTH_FROM, NEW_YEAR_FROM, NEW_MONTH_TO, NEW_YEAR_TO,
                NEW_ENTERPRISE_NAME, NEW_POSITION, NEW_DESCRIPTION);

        Assert.assertNotNull(newExperience);
        Assert.assertEquals(NEW_MONTH_FROM, newExperience.getMonthFrom());
        Assert.assertEquals(NEW_YEAR_FROM, newExperience.getYearFrom());
        Assert.assertEquals(NEW_MONTH_TO, newExperience.getMonthTo());
        Assert.assertEquals(NEW_YEAR_TO, newExperience.getYearTo());
        Assert.assertEquals(NEW_ENTERPRISE_NAME, newExperience.getEnterpriseName());
        Assert.assertEquals(NEW_POSITION, newExperience.getPosition());
        Assert.assertEquals(NEW_DESCRIPTION, newExperience.getDescription());
    }

    @Test
    public void testFindById() {
        final Optional<Experience> newExperience = dao.findById(testExperience.getId());

        assertTrue(newExperience.isPresent());
        Assert.assertEquals(testExperience, newExperience.get());
        Assert.assertEquals(testExperience.getUser(), newExperience.get().getUser());
        Assert.assertEquals(EXISTING_MONTH_FROM, newExperience.get().getMonthFrom());
        Assert.assertEquals(EXISTING_YEAR_FROM, newExperience.get().getYearFrom());
        Assert.assertEquals(EXISTING_MONTH_TO, newExperience.get().getMonthTo());
        Assert.assertEquals(EXISTING_YEAR_TO, newExperience.get().getYearTo());
        Assert.assertEquals(EXISTING_ENTERPRISE_NAME, newExperience.get().getEnterpriseName());
        Assert.assertEquals(EXISTING_POSITION, newExperience.get().getPosition());
        Assert.assertEquals(EXISTING_DESCRIPTION, newExperience.get().getDescription());
    }

    @Test
    public void testFindByUser() {
        final List<Experience> experienceList = dao.findByUser(testUser);

        Assert.assertNotNull(experienceList);
        Assert.assertEquals(USER_EXPERIENCE_LIST_SIZE, experienceList.size());
        Assert.assertEquals(testUser, experienceList.get(0).getUser());
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
        Experience testExperience2 = new Experience(testUser, EXISTING_MONTH_FROM, EXISTING_YEAR_FROM, EXISTING_MONTH_TO, EXISTING_YEAR_TO, EXISTING_ENTERPRISE_NAME, EXISTING_ENTERPRISE_NAME, EXISTING_DESCRIPTION);
        em.persist(testExperience2);
        dao.deleteExperience(testExperience2.getId());
        //TODO: fix assert
        //Assert.assertNull(em.find(Experience.class, testExperience2.getId()));
    }
}
