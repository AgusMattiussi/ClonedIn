package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Transactional
@Rollback
public class CategoryHibernateDaoTest {
    private static final String TEST_CATEGORY_1 = "testCategory1";
    private static final String TEST_CATEGORY_2 = "testCategory2";
    private static final String TEST_CATEGORY_3 = "testCategory3";
    private static final String NEW_CATEGORY = "newCategory";
    private static final long ID = 7;
    private static final long CATEGORIES_COUNT = 3;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CategoryDao dao;

    @Before
    public void setUp() {
        Category testCategory1 = new Category(TEST_CATEGORY_1);
        Category testCategory2 = new Category(TEST_CATEGORY_2);
        Category testCategory3 = new Category(TEST_CATEGORY_3);
        em.persist(testCategory1);
        em.persist(testCategory2);
        em.persist(testCategory3);
    }

    @Test
    public void testCreate() {
        final Category newCategory = dao.create(NEW_CATEGORY) ;
        Assert.assertNotNull(newCategory);
        Assert.assertEquals(NEW_CATEGORY, newCategory.getName());
        Assert.assertEquals(newCategory, em.find(Category.class, newCategory.getId()));
    }

    @Test
    public void testFindByName() {
        final Optional<Category> category = dao.findByName(TEST_CATEGORY_1);
        assertTrue(category.isPresent());
        Assert.assertEquals(TEST_CATEGORY_1, category.get().getName());
    }

    @Test
    public void testFindById() {
        final Optional<Category> category = dao.findById(ID);
        assertTrue(category.isPresent());
        Assert.assertEquals(TEST_CATEGORY_1, category.get().getName());
    }

    @Test
    public void testGetAllCategories(){
        final List<Category> allCategories = dao.getAllCategories();
        Assert.assertEquals(CATEGORIES_COUNT, allCategories.size());
    }
}
