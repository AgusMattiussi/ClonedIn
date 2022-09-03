package ar.itba.edu.paw.services;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
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

    /*@Test
    public void testCreate() {
        // 1. Setup!
        Mockito.when(userDao.create(eq(EMAIL), eq(PASSWORD)))
            .thenReturn(new User(1, EMAIL, PASSWORD));

        // 2. "ejercito" la class under test
        final User newUser = userService.register(EMAIL, PASSWORD);

        // 3. Asserts!
        Assert.assertNotNull(newUser);
        Assert.assertEquals(EMAIL, newUser.getEmail());
        //Assert.assertEquals(PASSWORD, newUser.getPassword());
    }*/

    /*@Test(expected = DuplicateKeyException.class)
    public void testCreateEmailAlreadyExists() {
        // 1. Setup!
        Mockito.when(userDao.create(eq(EMAIL), eq(PASSWORD)))
            .thenThrow(DuplicateKeyException.class);
    }*/

    /*@Test
    public void testFindByEmail() {
        Mockito.when(userDao.findByEmail(eq(EMAIL)))
                .thenReturn(Optional.of(new User(1, EMAIL, PASSWORD)));

        final Optional<User> maybeUser = userService.findByEmail(EMAIL);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(EMAIL, maybeUser.get().getEmail());
    }*/

}
