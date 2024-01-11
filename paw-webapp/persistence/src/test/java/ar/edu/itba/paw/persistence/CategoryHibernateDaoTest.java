package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class CategoryHibernateDaoTest {
    private static final String TEST_CATEGORY_1 = "testCategory1";
    private static final String TEST_CATEGORY_2 = "testCategory2";
    private static final String TEST_CATEGORY_3 = "testCategory3";
    private static final String NEW_CATEGORY = "newCategory";
    private static final long CATEGORIES_COUNT = 2;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CategoryDao dao;
    private Category testCategory;

    @Before
    public void setUp() {
        testCategory = new Category(TEST_CATEGORY_1);
        Category testCategory2 = new Category(TEST_CATEGORY_2);
        Category testCategory3 = new Category(TEST_CATEGORY_3);
        em.persist(testCategory);
        em.persist(testCategory2);
        em.persist(testCategory3);
    }

    @Test
    public void testCreate() {
        final Category newCategory = dao.create(NEW_CATEGORY) ;
        assertNotNull(newCategory);
        assertEquals(NEW_CATEGORY, newCategory.getName());
        assertEquals(newCategory, em.find(Category.class, newCategory.getId()));
    }

    @Test
    public void testFindByName() {
        final Optional<Category> category = dao.findByName(TEST_CATEGORY_1);
        assertTrue(category.isPresent());
        assertEquals(TEST_CATEGORY_1, category.get().getName());
    }

    @Test
    public void testFindById() {
        final Optional<Category> category = dao.findById(testCategory.getId());
        assertTrue(category.isPresent());
        assertEquals(TEST_CATEGORY_1, category.get().getName());
    }

    @Test
    public void testGetAllCategories(){
        final List<Category> allCategories = dao.getAllCategories(0, 3);
        assertEquals(CATEGORIES_COUNT, allCategories.size());
    }
}
