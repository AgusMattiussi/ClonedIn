package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Category;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class CategoryJdbcDaoTest {

    private static final String CATEGORY_TABLE = "rubro";
    private static final String NAME = "nombre";

    private static final String TEST_CATEGORY = "testCategory";
    private static final String NEW_CATEGORY = "newCategory";
    private static final String NON_EXISTING_CATEGORY = "NonExistingCategory";
    private static final long FIRST_ID = 1;

    @Autowired
    private CategoryJdbcDao dao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;

    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        //JdbcTestUtils.deleteFromTables(jdbctemplate, CATEGORY_TABLE);
    }

    @Test
    public void testCreate() {
        final Category newCategory = dao.create(NEW_CATEGORY) ;

        Assert.assertNotNull(newCategory);
        Assert.assertEquals(NEW_CATEGORY, newCategory.getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, CATEGORY_TABLE, NAME + " = '" + NEW_CATEGORY + "'"));
        
    }

    @Test
    public void testFindByName() {
        final Optional<Category> category = dao.findByName(TEST_CATEGORY);

        Assert.assertTrue(category.isPresent());
        Assert.assertEquals(TEST_CATEGORY, category.get().getName());
        Assert.assertEquals(FIRST_ID, category.get().getId());
    }

    /*@Test
    public void testFindByNameOrCreate(){
        final Optional<Category> notFoundCategory = dao.findByName(NON_EXISTING_CATEGORY);
        final Category category = dao.findByNameOrCreate(NON_EXISTING_CATEGORY);

        Assert.assertFalse(notFoundCategory.isPresent());
        Assert.assertNotNull(category);
        Assert.assertEquals(NON_EXISTING_CATEGORY, category.getName());
    }*/

    @Test
    public void testFindById() {
        final Optional<Category> category = dao.findById(1);

        Assert.assertTrue(category.isPresent());
        Assert.assertEquals(FIRST_ID, category.get().getId());
        Assert.assertEquals(TEST_CATEGORY, category.get().getName());
    }

    @Test
    public void testGetAllCategories(){
        final Category cat1 = dao.create("Cat1");
        final Category cat2 = dao.create("Cat2");
        final Category cat3 = dao.create("Cat3");

        final List<Category> allCategories = dao.getAllCategories();

        Assert.assertEquals(3 + 1, allCategories.size());
        Assert.assertTrue(allCategories.contains(cat1));
        Assert.assertTrue(allCategories.contains(cat2));
        Assert.assertTrue(allCategories.contains(cat3));
    }
}
