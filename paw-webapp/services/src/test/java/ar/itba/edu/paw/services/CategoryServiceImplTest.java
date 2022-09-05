package ar.itba.edu.paw.services;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.services.CategoryServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceImplTest {

    private static final String TEST_CATEGORY = "testCategory";
    private static final long TEST_ID = 1;
    private long currentId = 1;

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryDao categoryDao;

    @Before
    public void setUp(){
        Mockito.when(categoryDao.create(eq("Cat1")))
            .thenReturn(new Category(1, "Cat1"));
        Mockito.when(categoryDao.create(eq("Cat2")))
            .thenReturn(new Category(2, "Cat2"));
        Mockito.when(categoryDao.create(eq("Cat3")))
            .thenReturn(new Category(3, "Cat3"));
    }

    @Test
    public void testCreate() {
        Mockito.when(categoryDao.create(eq(TEST_CATEGORY)))
            .thenReturn(new Category(TEST_ID, TEST_CATEGORY));

        final Category newCategory = categoryService.create(TEST_CATEGORY);

        Assert.assertNotNull(newCategory);
        Assert.assertEquals(TEST_CATEGORY, newCategory.getName());
        Assert.assertEquals(TEST_ID, newCategory.getId());
    }

    @Test
    public void testFindByName() {
        Mockito.when(categoryDao.findByName(eq(TEST_CATEGORY)))
                .thenReturn(Optional.of(new Category(TEST_ID, TEST_CATEGORY)));

        final Optional<Category> optCategory = categoryService.findByName(TEST_CATEGORY);

        Assert.assertTrue(optCategory.isPresent());
        Assert.assertEquals(TEST_CATEGORY, optCategory.get().getName());
        Assert.assertEquals(TEST_ID, optCategory.get().getId());

    }

    @Test
    public void testFindById() {
        Mockito.when(categoryDao.findById(eq(TEST_ID)))
                .thenReturn(Optional.of(new Category(TEST_ID, TEST_CATEGORY)));

        final Optional<Category> optCategory = categoryService.findById(TEST_ID);

        Assert.assertTrue(optCategory.isPresent());
        Assert.assertEquals(TEST_ID, optCategory.get().getId());
        Assert.assertEquals(TEST_CATEGORY, optCategory.get().getName());
    }

    @Test
    public void testGetAllCategories(){
        final Category cat1 = categoryDao.create("Cat1");
        final Category cat2 = categoryDao.create("Cat2");
        final Category cat3 = categoryDao.create("Cat3");

        final List<Category> correctList = new ArrayList<>();
        correctList.add(cat1);
        correctList.add(cat2);
        correctList.add(cat3);
        Mockito.when(categoryDao.getAllCategories()).thenReturn(correctList);

        final List<Category> allCategories = categoryDao.getAllCategories();

        Assert.assertEquals(3, allCategories.size());
        Assert.assertTrue(allCategories.contains(cat1));
        Assert.assertTrue(allCategories.contains(cat2));
        Assert.assertTrue(allCategories.contains(cat3));
    }
}
