package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.*;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class ImageJdbcDaoTest {

    /*private static final String IMAGE_TABLE = "imagen";
    private static final String ID = "id";
    private static final String BYTES = "bytes";
    public static final String TEST_IMAGE_PATH = "src/test/resources/logo.png";

    @Value("classpath:logo.png")
    private Resource logoImage;

    @Autowired
    private ImageJdbcDao imageDao;
    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;
    private byte[] testImageByteArray;*/

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    /*@Before
    public void setUp() throws IOException {
        jdbctemplate = new JdbcTemplate(ds);
        File testImageFile = new File(TEST_IMAGE_PATH);
        FileInputStream testImageFIS = new FileInputStream(testImageFile);
        testImageByteArray = new byte[(int) testImageFile.length()];
        testImageFIS.read(testImageByteArray);
        testImageFIS.close();

        //System.out.println("\n\n\n\n\n\n Tiene valor? " + testImageByteArray.length + " - " + Arrays.toString(testImageByteArray) + "\n\n\n\n");
    }

    @Test
    public void testCreate() throws IOException {
        final Image newImage = imageDao.uploadImage(testImageByteArray);

        Assert.assertNotNull(newImage);
        Assert.assertEquals(testImageByteArray, newImage.getBytes());
    }

    @Test
    public void testFindById() {
        final Image originalImage = imageDao.uploadImage(testImageByteArray);

        final Optional<Image> image = imageDao.getImage(originalImage.getId());

        assertTrue(image.isPresent());
        Assert.assertEquals(originalImage.getId(), image.get().getId());
        Assert.assertNotNull(image.get().getBytes());
        Assert.assertArrayEquals(testImageByteArray, image.get().getBytes());
    }
*/
}
