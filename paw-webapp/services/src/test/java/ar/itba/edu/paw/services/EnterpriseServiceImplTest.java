package ar.itba.edu.paw.services;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.services.EnterpriseServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class EnterpriseServiceImplTest {
    private static final long TEST_ID = 1;
    private static final String TEST_NAME = "Empresa1";
    private static final String TEST_EMAIL = "empresa1@gmail.com";
    private static final String TEST_PASSWORD = "pass123";
    private static final String TEST_LOCATION = "Calle Falsa para Empresas 123";
    private static final long TEST_CATEGORY_ID_FK = 1;
    private static final String TEST_CATEGORY_NAME = "AlgunaCategoria";

    private static final String TEST_DESCRIPTION = "La mejor empresa del mundo";
    private static final Category TEST_CATEGORY = new Category(1, TEST_CATEGORY_NAME);

    @InjectMocks
    private EnterpriseServiceImpl enterpriseService;

    @Mock
    private EnterpriseDao enterpriseDao;

    @Before
    public void setUp(){
    }

    @Test
    public void testCreate() {
        Mockito.when(enterpriseDao.create(eq(TEST_EMAIL), eq(TEST_NAME), eq(TEST_PASSWORD), eq(TEST_LOCATION), eq(TEST_CATEGORY_NAME), eq(TEST_DESCRIPTION)))
                .thenReturn(new Enterprise(TEST_ID, TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_LOCATION, TEST_CATEGORY, TEST_DESCRIPTION, 1));

        final Enterprise newEnterprise = enterpriseService.create(TEST_EMAIL, TEST_NAME, TEST_PASSWORD, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_DESCRIPTION);

        Assert.assertNotNull(newEnterprise);
        Assert.assertEquals(TEST_EMAIL, newEnterprise.getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newEnterprise.getPassword());
        Assert.assertEquals(TEST_NAME, newEnterprise.getName());
        Assert.assertEquals(TEST_LOCATION, newEnterprise.getLocation());
        Assert.assertEquals(TEST_CATEGORY, newEnterprise.getCategory());
        Assert.assertEquals(TEST_DESCRIPTION, newEnterprise.getDescription());
    }

    @Test
    public void testFindByEmail() {
        Mockito.when(enterpriseDao.findByEmail(eq(TEST_EMAIL)))
                .thenReturn(Optional.of(new Enterprise(TEST_ID, TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_LOCATION, TEST_CATEGORY, TEST_DESCRIPTION, 1)));

        final Optional<Enterprise> optEnterprise = enterpriseService.findByEmail(TEST_EMAIL);

        Assert.assertTrue(optEnterprise.isPresent());
        Assert.assertEquals(TEST_EMAIL, optEnterprise.get().getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newEnterprise.getPassword());
        Assert.assertEquals(TEST_NAME, optEnterprise.get().getName());
        Assert.assertEquals(TEST_LOCATION, optEnterprise.get().getLocation());
        Assert.assertEquals(TEST_CATEGORY, optEnterprise.get().getCategory());
        Assert.assertEquals(TEST_DESCRIPTION, optEnterprise.get().getDescription());

    }

    @Test
    public void testFindById() {
        Mockito.when(enterpriseDao.findById(eq(TEST_ID)))
                .thenReturn(Optional.of(new Enterprise(TEST_ID, TEST_NAME, TEST_EMAIL, TEST_PASSWORD, TEST_LOCATION, TEST_CATEGORY, TEST_DESCRIPTION, 1)));


        final Optional<Enterprise> optEnterprise = enterpriseService.findById(TEST_ID);

        Assert.assertTrue(optEnterprise.isPresent());
        Assert.assertEquals(TEST_EMAIL, optEnterprise.get().getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newEnterprise.getPassword());
        Assert.assertEquals(TEST_NAME, optEnterprise.get().getName());
        Assert.assertEquals(TEST_LOCATION, optEnterprise.get().getLocation());
        Assert.assertEquals(TEST_CATEGORY, optEnterprise.get().getCategory());
        Assert.assertEquals(TEST_DESCRIPTION, optEnterprise.get().getDescription());
    }
}
