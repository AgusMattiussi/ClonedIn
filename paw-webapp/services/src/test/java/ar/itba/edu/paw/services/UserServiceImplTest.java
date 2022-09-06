package ar.itba.edu.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final long TEST_ID = 1;
    private static final String TEST_NAME = "John Doe";
    private static final String TEST_EMAIL = "johndoe@gmail.com";
    private static final String TEST_PASSWORD = "pass123";
    private static final String TEST_LOCATION = "Calle Falsa 123";
    //private static final long TEST_CATEGORY_ID_FK = 1;
    private static final String TEST_CATEGORY_NAME = "AlgunaCategoria";
    private static final String TEST_CURRENT_POSITION = "CEO de PAW";
    private static final String TEST_DESCRIPTION = "Un tipo muy laburante";
    private static final String TEST_EDUCATION = "Licenciado en la Universidad de la Calle";


    private static final String PASSWORD = "passwordpassword";
    private static final String EMAIL = "foo@bar.com";
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserDao userDao;

    @Before
    public void setUp(){
        Mockito.when(userDao.create(eq("naruto@gmail.com"), eq(TEST_PASSWORD), eq("Naruto"), eq(TEST_LOCATION), eq(TEST_CATEGORY_NAME), eq(TEST_CURRENT_POSITION), eq(TEST_DESCRIPTION), eq(TEST_EDUCATION)))
            .thenReturn(new User(2, "naruto@gmail.com", TEST_PASSWORD, "Naruto", TEST_LOCATION, 0, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION));
        Mockito.when(userDao.create(eq("sasuke@gmail.com"), eq(TEST_PASSWORD), eq("Sasuke"), eq(TEST_LOCATION), eq(TEST_CATEGORY_NAME), eq(TEST_CURRENT_POSITION), eq(TEST_DESCRIPTION), eq(TEST_EDUCATION)))
            .thenReturn(new User(3, "sasuke@gmail.com", TEST_PASSWORD, "Sasuke", TEST_LOCATION, 0, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION));
        Mockito.when(userDao.create(eq("sakura@gmail.com"), eq(TEST_PASSWORD), eq("Sakura"), eq(TEST_LOCATION), eq(TEST_CATEGORY_NAME), eq(TEST_CURRENT_POSITION), eq(TEST_DESCRIPTION), eq(TEST_EDUCATION)))
            .thenReturn(new User(4, "sakura@gmail.com", TEST_PASSWORD, "Sakura", TEST_LOCATION, 0, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION));
    }

    @Test
    public void testCreate() {
        Mockito.when(userDao.create(eq(TEST_EMAIL), eq(TEST_PASSWORD), eq(TEST_NAME), eq(TEST_LOCATION), eq(TEST_CATEGORY_NAME), eq(TEST_CURRENT_POSITION), eq(TEST_DESCRIPTION), eq(TEST_EDUCATION)))
            .thenReturn(new User(TEST_ID, TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, 0, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION));

        final User newUser = userService.register(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);

        Assert.assertNotNull(newUser);
        Assert.assertEquals(TEST_EMAIL, newUser.getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newUser.getPassword());
        Assert.assertEquals(TEST_NAME, newUser.getName());
        Assert.assertEquals(TEST_LOCATION, newUser.getLocation());
        Assert.assertEquals(0, newUser.getCategoryId_fk());
        Assert.assertEquals(TEST_CURRENT_POSITION, newUser.getCurrentPosition());
        Assert.assertEquals(TEST_DESCRIPTION, newUser.getDescription());
        Assert.assertEquals(TEST_EDUCATION, newUser.getEducation());
    }

    @Test
    public void testFindByEmail() {
        Mockito.when(userDao.findByEmail(eq(TEST_EMAIL)))
                .thenReturn(Optional.of(new User(TEST_ID, TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, 0, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION)));

        final Optional<User> optUser = userService.findByEmail(TEST_EMAIL);

        Assert.assertTrue(optUser.isPresent());
        Assert.assertEquals(TEST_EMAIL, optUser.get().getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newUser.getPassword());
        Assert.assertEquals(TEST_NAME, optUser.get().getName());
        Assert.assertEquals(TEST_LOCATION, optUser.get().getLocation());
        Assert.assertEquals(0, optUser.get().getCategoryId_fk());
        Assert.assertEquals(TEST_CURRENT_POSITION, optUser.get().getCurrentPosition());
        Assert.assertEquals(TEST_DESCRIPTION, optUser.get().getDescription());
        Assert.assertEquals(TEST_EDUCATION, optUser.get().getEducation());

    }

    @Test
    public void testFindById() {
        Mockito.when(userDao.findById(eq(TEST_ID)))
                .thenReturn(Optional.of(new User(TEST_ID, TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, 0, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION)));


        final Optional<User> optUser = userService.findById(TEST_ID);

        Assert.assertTrue(optUser.isPresent());
        Assert.assertEquals(TEST_EMAIL, optUser.get().getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newUser.getPassword());
        Assert.assertEquals(TEST_NAME, optUser.get().getName());
        Assert.assertEquals(TEST_LOCATION, optUser.get().getLocation());
        Assert.assertEquals(0, optUser.get().getCategoryId_fk());
        Assert.assertEquals(TEST_CURRENT_POSITION, optUser.get().getCurrentPosition());
        Assert.assertEquals(TEST_DESCRIPTION, optUser.get().getDescription());
        Assert.assertEquals(TEST_EDUCATION, optUser.get().getEducation());
    }

    @Test
    public void testGetAllUsers() {
        final User u1 = userDao.create("naruto@gmail.com", TEST_PASSWORD, "Naruto", TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        final User u2 = userDao.create("sasuke@gmail.com", TEST_PASSWORD, "Sasuke", TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);
        final User u3 = userDao.create("sakura@gmail.com", TEST_PASSWORD, "Sakura", TEST_LOCATION, TEST_CATEGORY_NAME, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);

        List<User> mockAllUsers = new ArrayList<>();
        mockAllUsers.add(u1);
        mockAllUsers.add(u2);
        mockAllUsers.add(u3);
        Mockito.when(userDao.getAllUsers())
            .thenReturn(mockAllUsers);

        final List<User> allUsers = userDao.getAllUsers();

        //Tenemos en cuenta el insert inicial
        Assert.assertEquals(3, allUsers.size());
        Assert.assertTrue(allUsers.contains(u1));
        Assert.assertTrue(allUsers.contains(u2));
        Assert.assertTrue(allUsers.contains(u3));
    }

}
