package ar.itba.edu.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.services.UserServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final long TEST_ID = 1;
    private static final String TEST_NAME = "John Doe";
    private static final String TEST_EMAIL = "johndoe@gmail.com";
    private static final String TEST_PASSWORD = "pass123";
    private static final String TEST_LOCATION = "Calle Falsa 123";
    private static final long TEST_CATEGORY_ID_FK = 1;
    private static final String TEST_CURRENT_POSITION = "CEO de PAW";
    private static final String TEST_DESCRIPTION = "Un tipo muy laburante";
    private static final String TEST_EDUCATION = "Licenciado en la Universidad de la Calle";


    private static final String PASSWORD = "passwordpassword";
    private static final String EMAIL = "foo@bar.com";
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserDao userDao;

    @Test
    public void easy() {
        Assert.assertTrue(true);
    }

    @Test
    public void testCreate() {
        Mockito.when(userDao.create(eq(TEST_EMAIL), eq(TEST_PASSWORD), eq(TEST_NAME), eq(TEST_LOCATION), eq(TEST_CATEGORY_ID_FK), eq(TEST_CURRENT_POSITION), eq(TEST_DESCRIPTION), eq(TEST_EDUCATION)))
            .thenReturn(new User(TEST_ID, TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_ID_FK, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION));

        final User newUser = userService.register(TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_ID_FK, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION);

        Assert.assertNotNull(newUser);
        Assert.assertEquals(TEST_EMAIL, newUser.getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newUser.getPassword());
        Assert.assertEquals(TEST_NAME, newUser.getName());
        Assert.assertEquals(TEST_LOCATION, newUser.getLocation());
        Assert.assertEquals(TEST_CATEGORY_ID_FK, newUser.getCategoryId_fk());
        Assert.assertEquals(TEST_CURRENT_POSITION, newUser.getCurrentPosition());
        Assert.assertEquals(TEST_DESCRIPTION, newUser.getDescription());
        Assert.assertEquals(TEST_EDUCATION, newUser.getEducation());
    }

    @Test
    public void testFindByEmail() {
        Mockito.when(userDao.findByEmail(eq(TEST_EMAIL)))
                .thenReturn(Optional.of(new User(TEST_ID, TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_ID_FK, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION)));

        final Optional<User> optUser = userService.findByEmail(TEST_EMAIL);

        Assert.assertTrue(optUser.isPresent());
        Assert.assertEquals(TEST_EMAIL, optUser.get().getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newUser.getPassword());
        Assert.assertEquals(TEST_NAME, optUser.get().getName());
        Assert.assertEquals(TEST_LOCATION, optUser.get().getLocation());
        Assert.assertEquals(TEST_CATEGORY_ID_FK, optUser.get().getCategoryId_fk());
        Assert.assertEquals(TEST_CURRENT_POSITION, optUser.get().getCurrentPosition());
        Assert.assertEquals(TEST_DESCRIPTION, optUser.get().getDescription());
        Assert.assertEquals(TEST_EDUCATION, optUser.get().getEducation());

    }

    @Test
    public void testFindById() {
        Mockito.when(userDao.findById(eq(TEST_ID)))
                .thenReturn(Optional.of(new User(TEST_ID, TEST_EMAIL, TEST_PASSWORD, TEST_NAME, TEST_LOCATION, TEST_CATEGORY_ID_FK, TEST_CURRENT_POSITION, TEST_DESCRIPTION, TEST_EDUCATION)));


        final Optional<User> optUser = userService.findById(TEST_ID);

        Assert.assertTrue(optUser.isPresent());
        Assert.assertEquals(TEST_EMAIL, optUser.get().getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newUser.getPassword());
        Assert.assertEquals(TEST_NAME, optUser.get().getName());
        Assert.assertEquals(TEST_LOCATION, optUser.get().getLocation());
        Assert.assertEquals(TEST_CATEGORY_ID_FK, optUser.get().getCategoryId_fk());
        Assert.assertEquals(TEST_CURRENT_POSITION, optUser.get().getCurrentPosition());
        Assert.assertEquals(TEST_DESCRIPTION, optUser.get().getDescription());
        Assert.assertEquals(TEST_EDUCATION, optUser.get().getEducation());
    }

}
