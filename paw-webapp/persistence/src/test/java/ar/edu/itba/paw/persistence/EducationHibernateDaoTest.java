package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.EducationDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Education;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.Visibility;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class EducationHibernateDaoTest {

    private static final String NEW_TITLE = "newTitle";
    private static final String NEW_INSTITUTION = "newInstitution";
    private static final String NEW_DESCRIPTION = "newDescription";
    private static final String TEST_USER_EMAIL = "user@gmail.com";
    private static final String EMPTY_FIELD = "-";
    public static final String TEST_CATEGORY_DESC = "category";
    private static final String TEST_TITLE = "testTitle";
    private static final String TEST_INSTITUTION = "testInstitution";
    private static final String TEST_DESCRIPTION = "testDescription";
    public static final int NEW_MONTH_FROM = 11;
    public static final int NEW_YEAR_FROM = 2000;
    public static final int NEW_MONTH_TO = 12;
    public static final int NEW_YEAR_TO = 2004;
    public static final int TEST_MONTH_FROM = 11;
    public static final int TEST_YEAR_FROM = 2011;
    public static final int TEST_MONTH_TO = 12;
    public static final int TEST_YEAR_TO = 2012;
    
    @PersistenceContext
    private EntityManager em;
    
    @Autowired
    private EducationDao educationDao;
    
    private User testUser;
    private Category testCategory;
    private Education testEducation;
    private Education createdEducation;
    
    @Before
    public void setUp() {
        testCategory = new Category(TEST_CATEGORY_DESC);
        em.persist(testCategory);
        
        testUser = new User(TEST_USER_EMAIL, EMPTY_FIELD, EMPTY_FIELD, EMPTY_FIELD, testCategory, EMPTY_FIELD,
                EMPTY_FIELD, EMPTY_FIELD, Visibility.VISIBLE.getValue(), null);
        em.persist(testUser);

        testEducation = new Education(testUser, TEST_MONTH_FROM, TEST_YEAR_FROM, TEST_MONTH_TO, TEST_YEAR_TO, TEST_TITLE,
                TEST_INSTITUTION, TEST_DESCRIPTION);
        em.persist(testEducation);
    }

    @After
    public void restore(){
        em.remove(testCategory);
        em.remove(testUser);
        em.remove(testEducation);

        if(createdEducation != null) {
            em.remove(createdEducation);
            createdEducation = null;
        }
    }
    
    @Test
    public void testAdd() {
        createdEducation = educationDao.add(testUser, NEW_MONTH_FROM, NEW_YEAR_FROM, NEW_MONTH_TO, NEW_YEAR_TO, NEW_TITLE,
                NEW_INSTITUTION, NEW_DESCRIPTION);

        assertNotNull(createdEducation);
        assertEquals(testUser, createdEducation.getUser());
        assertEquals(NEW_MONTH_FROM, createdEducation.getMonthFrom());
        assertEquals(NEW_YEAR_FROM, createdEducation.getYearFrom());
        assertEquals(NEW_MONTH_TO, createdEducation.getMonthTo());
        assertEquals(NEW_YEAR_TO, createdEducation.getYearTo());
        assertEquals(NEW_TITLE, createdEducation.getTitle());
        assertEquals(NEW_INSTITUTION, createdEducation.getInstitutionName());
        assertEquals(NEW_DESCRIPTION, createdEducation.getDescription());
    }



    @Test
    public void testFindById() {
        final Optional<Education> foundEducation = educationDao.findById(testEducation.getId());

        assertTrue(foundEducation.isPresent());
        assertEquals(testUser, foundEducation.get().getUser());
        assertEquals(TEST_MONTH_FROM, foundEducation.get().getMonthFrom());
        assertEquals(TEST_YEAR_FROM, foundEducation.get().getYearFrom());
        assertEquals(TEST_MONTH_TO, foundEducation.get().getMonthTo());
        assertEquals(TEST_YEAR_TO, foundEducation.get().getYearTo());
        assertEquals(TEST_TITLE, foundEducation.get().getTitle());
        assertEquals(TEST_INSTITUTION, foundEducation.get().getInstitutionName());
        assertEquals(TEST_DESCRIPTION, foundEducation.get().getDescription());
    }

    @Test
    public void testFindByUser(){
        final List<Education> educationList = educationDao.findByUser(testUser);

        assertNotNull(educationList);
        assertFalse(educationList.isEmpty());
        assertEquals(1, educationList.size());
        assertEquals(testUser, educationList.get(0).getUser());
        assertEquals(TEST_MONTH_FROM, educationList.get(0).getMonthFrom());
        assertEquals(TEST_YEAR_FROM, educationList.get(0).getYearFrom());
        assertEquals(TEST_MONTH_TO, educationList.get(0).getMonthTo());
        assertEquals(TEST_YEAR_TO, educationList.get(0).getYearTo());
        assertEquals(TEST_TITLE, educationList.get(0).getTitle());
        assertEquals(TEST_INSTITUTION, educationList.get(0).getInstitutionName());
        assertEquals(TEST_DESCRIPTION, educationList.get(0).getDescription());
    }

    @Test
    public void testDeleteEducation(){
        educationDao.deleteEducation(testEducation.getId());

        Education deletedEducation = em.find(Education.class, testEducation.getId());
        assertNull(deletedEducation);
    }
}
