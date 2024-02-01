package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.enums.EducationLevel;
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
public class UserHibernateDaoTest {

    public static final String UNEXISTING_EMAIL = "falseemail@gmail.com";
    private static final String TEST_USER_NAME = "John Doe";
    private static final String TEST_USER_EMAIL = "johndoe@gmail.com";
    private static final String TEST_USER_PASSWORD = "pass123";
    private static final String TEST_USER_LOCATION = "Calle Falsa 123";
    public static final String EMPTY_FIELD = "-";

    private static final String NEW_USER_NAME = "newuser";
    private static final String NEW_USER_EMAIL = "newuser@gmail.com";
    private static final String NEW_USER_PASSWORD = "admin";

    public static final String TEST_CATEGORY_DESCRIPTION = "unaCategory";
    public static final String TEST_CATEGORY2_DESCRIPTION = "unaCategory2";

    public static final String CHANGED_PASSWORD = "supersecret";
    public static final String CHANGED_EMAIL = "newemail@gmail.com";
    public static final String CHANGED_NAME = "newName";
    public static final String CHANGED_DESCRIPTION = "newDescription";
    public static final String CHANGED_LOCATION = "newLocation";
    public static final String CHANGED_CURRENT_POSITION = "newPosition";
    public static final EducationLevel CHANGED_EDUCATION_LEVEL = EducationLevel.SECONDARY;
    public static final Visibility CHANGED_VISIBILITY = Visibility.INVISIBLE;


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao userDao;
    private Category testCategory;
    private Category testCategory2;
    private User testUser;
    private User createdUser = null;

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    @Before
    public void setUp() {
        testCategory = new Category(TEST_CATEGORY_DESCRIPTION);
        em.persist(testCategory);

        testCategory2 = new Category(TEST_CATEGORY2_DESCRIPTION);
        em.persist(testCategory2);

        testUser = new User(TEST_USER_EMAIL, TEST_USER_PASSWORD, TEST_USER_NAME, TEST_USER_LOCATION, testCategory, EMPTY_FIELD, EMPTY_FIELD,
                EMPTY_FIELD, Visibility.VISIBLE.getValue(), null);
        em.persist(testUser);
    }

    @After
    public void restore(){
        em.remove(testUser);
        em.remove(testCategory);
        em.remove(testCategory2);

        if(createdUser != null)
            em.remove(createdUser);
    }

    @Test
    public void testCreate(){
        User created = userDao.create(NEW_USER_EMAIL, NEW_USER_PASSWORD, NEW_USER_NAME, EMPTY_FIELD, testCategory, EMPTY_FIELD,
                EMPTY_FIELD, EducationLevel.NOT_SPECIFIED);

        User found = em.find(User.class, created.getId());

        assertNotNull(found);
        assertEquals(found, created);
    }

    @Test
    public void testFindByEmail(){
        Optional<User> found = userDao.findByEmail(testUser.getEmail());

        assertTrue(found.isPresent());
        assertEquals(testUser, found.get());
    }

    @Test
    public void testFindById(){
        Optional<User> found = userDao.findById(testUser.getId());

        assertTrue(found.isPresent());
        assertEquals(testUser, found.get());
    }

    @Test
    public void testGetIdForEmail(){
        Optional<Long> found = userDao.getIdForEmail(testUser.getEmail());

        assertTrue(found.isPresent());
        assertEquals(testUser.getId(), found.get());
    }

    @Test
    public void testUserExistsTrue(){
        boolean exists = userDao.userExists(testUser.getEmail());

        assertTrue(exists);
    }

    @Test
    public void testUserExistsFalse(){
        boolean exists = userDao.userExists(UNEXISTING_EMAIL);

        assertFalse(exists);
    }

    @Test
    public void testGetAllUsers(){
        List<User> userList = userDao.getAllUsers();

        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        assertEquals(testUser, userList.get(0));
    }

    @Test
    public void testChangePassword(){
        userDao.changePassword(testUser.getEmail(), CHANGED_PASSWORD);

        User found = em.find(User.class, testUser.getId());
        em.refresh(found);

        assertNotNull(found);
        assertEquals(CHANGED_PASSWORD, found.getPassword());
    }

    @Test
    public void testGetUsersCount(){
        long count = userDao.getUsersCount();

        assertEquals(1, count);
    }

    @Test
    public void testGetUsersCountByFilters(){
        long count = userDao.getUsersCountByFilters(testCategory, TEST_USER_LOCATION, null, "");

        assertEquals(1, count);
    }

    @Test
    public void testGetVisibleUsers(){
        List<User> userList = userDao.getVisibleUsers(0, 10);

        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        assertEquals(testUser, userList.get(0));
    }

    @Test
    public void testGetVisibleUsersByCategory(){
        List<User> userList = userDao.getVisibleUsersByCategory(testCategory,0, 10);

        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        assertEquals(testUser, userList.get(0));
    }

    @Test
    public void testGetVisibleUsersByNameLike(){
        final String partialUserName = TEST_USER_NAME.substring(2, 6);
        List<User> userList = userDao.getVisibleUsersByNameLike(partialUserName,0, 10);

        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        assertEquals(testUser, userList.get(0));
    }

    @Test
    public void testGetVisibleUsersByLocationLike(){
        List<User> userList = userDao.getVisibleUsersByLocationLike(TEST_USER_LOCATION,0, 10);

        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        assertEquals(testUser, userList.get(0));
    }

    @Test
    public void testGetUsersListByFilters(){
        List<User> userList = userDao.getUsersListByFilters(testCategory, TEST_USER_LOCATION, null, "", 0, 10);

        assertFalse(userList.isEmpty());
        assertEquals(1, userList.size());
        assertEquals(testUser, userList.get(0));
    }

    @Test
    public void testUpdateName(){
        userDao.updateName(testUser.getId(), CHANGED_NAME);

        User found = em.find(User.class, testUser.getId());
        em.refresh(found);

        assertNotNull(found);
        assertEquals(CHANGED_NAME, found.getName());
    }


    @Test
    public void testUpdateDescription(){
        userDao.updateDescription(testUser.getId(), CHANGED_DESCRIPTION);

        User found = em.find(User.class, testUser.getId());
        em.refresh(found);

        assertNotNull(found);
        assertEquals(CHANGED_DESCRIPTION, found.getDescription());
    }

    @Test
    public void testUpdateLocation(){
        userDao.updateLocation(testUser.getId(), CHANGED_LOCATION);

        User found = em.find(User.class, testUser.getId());
        em.refresh(found);

        assertNotNull(found);
        assertEquals(CHANGED_LOCATION, found.getLocation());
    }

    @Test
    public void testUpdateCurrentPosition(){
        userDao.updateCurrentPosition(testUser.getId(), CHANGED_CURRENT_POSITION);

        User found = em.find(User.class, testUser.getId());
        em.refresh(found);

        assertNotNull(found);
        assertEquals(CHANGED_CURRENT_POSITION, found.getCurrentPosition());
    }

    @Test
    public void testUpdateCategory(){
        userDao.updateCategory(testUser.getId(), testCategory2);

        User found = em.find(User.class, testUser.getId());
        em.refresh(found);

        assertNotNull(found);
        assertEquals(testCategory2, found.getCategory());
    }

    @Test
    public void testUpdateEducationLevel(){
        userDao.updateEducationLevel(testUser.getId(), CHANGED_EDUCATION_LEVEL);

        User found = em.find(User.class, testUser.getId());
        em.refresh(found);

        assertNotNull(found);
        assertEquals(CHANGED_EDUCATION_LEVEL.getStringValue(), found.getEducation());
    }

    @Test
    public void testUpdateVisibility(){
        userDao.updateVisibility(testUser.getId(), CHANGED_VISIBILITY);

        User found = em.find(User.class, testUser.getId());
        em.refresh(found);

        assertNotNull(found);
        assertEquals(CHANGED_VISIBILITY.getValue(), found.getVisibility());
    }

}
