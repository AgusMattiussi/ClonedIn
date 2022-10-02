package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
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
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class EnterpriseJdbcDaoTest {
    private static final String ENTERPRISE_TABLE = "empresa";
    private static final String ID = "id";
    private static final String NAME = "nombre";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "contrasenia";
    private static final String LOCATION = "ubicacion";
    private static final String CATEGORY_ID_FK = "idRubro";
    private static final String DESCRIPTION = "descripcion";

    private static final String TEST_NAME = "Empresa1";
    private static final String TEST_EMAIL = "empresa1@gmail.com";
    private static final String TEST_PASSWORD = "pass123";
    private static final String TEST_LOCATION = "Calle Falsa para Empresas 123";
    private static final String TEST_CATEGORY_NAME = "testCategory";

    private static final String TEST_DESCRIPTION = "La mejor empresa del mundo";
    private static final long FIRST_ID = 1;
    private static final String EXISTING_NAME = "Empresaurio";
    private static final String EXISTING_EMAIL = "empresaurio@gmail.com";
    private static final String EXISTING_PASSWORD = "12345678";

    public static final String UPDATED_STRING = "updatedstring";

    @Autowired
    private EnterpriseJdbcDao dao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private DataSource ds;
    private JdbcTemplate jdbctemplate;
    private Category testCategory;




    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        testCategory = categoryDao.findByName(TEST_CATEGORY_NAME).get();
        //JdbcTestUtils.deleteFromTables(jdbctemplate, ENTERPRISE_TABLE);
    }

    @Test
    public void testCreate() {
        final Enterprise newEnterprise = dao.create(TEST_EMAIL, TEST_NAME, TEST_PASSWORD, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_DESCRIPTION);

        Assert.assertNotNull(newEnterprise);
        Assert.assertEquals(TEST_EMAIL, newEnterprise.getEmail());
        //Assert.assertEquals(TEST_PASSWORD, newEnterprise.getPassword());
        Assert.assertEquals(TEST_NAME, newEnterprise.getName());
        Assert.assertEquals(TEST_LOCATION, newEnterprise.getLocation());
        Assert.assertEquals(testCategory, newEnterprise.getCategory());
        Assert.assertEquals(TEST_DESCRIPTION, newEnterprise.getDescription());

        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, ENTERPRISE_TABLE, EMAIL + " = '" + TEST_EMAIL + "'"));
    }



    @Test
    public void testFindById() {
        final Optional<Enterprise> newEnterprise = dao.findById(FIRST_ID);

        Assert.assertTrue(newEnterprise.isPresent());
        Assert.assertEquals(FIRST_ID, newEnterprise.get().getId());
        Assert.assertEquals(EXISTING_EMAIL, newEnterprise.get().getEmail());
        Assert.assertEquals(EXISTING_NAME, newEnterprise.get().getName());
    }

    @Test
    public void testFindByEmail() {
        final Optional<Enterprise> newEnterprise = dao.findByEmail(EXISTING_EMAIL);

        Assert.assertTrue(newEnterprise.isPresent());
        Assert.assertEquals(FIRST_ID, newEnterprise.get().getId());
        Assert.assertEquals(EXISTING_EMAIL, newEnterprise.get().getEmail());
        Assert.assertEquals(EXISTING_NAME, newEnterprise.get().getName());
    }

    @Test
    public void testUpdateName(){
        Enterprise newEnterprise = dao.create(TEST_EMAIL, TEST_NAME, TEST_PASSWORD, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_DESCRIPTION);
        dao.updateName(newEnterprise.getId(), UPDATED_STRING);
        newEnterprise = dao.findById(newEnterprise.getId()).get();

        Assert.assertEquals(UPDATED_STRING, newEnterprise.getName());
    }

    @Test
    public void testUpdateDescription(){
        Enterprise newEnterprise = dao.create(TEST_EMAIL, TEST_NAME, TEST_PASSWORD, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_DESCRIPTION);
        dao.updateDescription(newEnterprise.getId(), UPDATED_STRING);
        newEnterprise = dao.findById(newEnterprise.getId()).get();

        Assert.assertEquals(UPDATED_STRING, newEnterprise.getDescription());
    }

    @Test
    public void testUpdateLocation(){
        Enterprise newEnterprise = dao.create(TEST_EMAIL, TEST_NAME, TEST_PASSWORD, TEST_LOCATION, TEST_CATEGORY_NAME, TEST_DESCRIPTION);
        dao.updateLocation(newEnterprise.getId(), UPDATED_STRING);
        newEnterprise = dao.findById(newEnterprise.getId()).get();

        Assert.assertEquals(UPDATED_STRING, newEnterprise.getLocation());
    }


}
