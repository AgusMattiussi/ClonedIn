package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.persistence.config.TestConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class ImageJdbcDaoTest {

    private static final long ID = 1;
    private final byte[] NEW_IMAGE_BYTE_ARRAY = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private final byte[] TEST_IMAGE_BYTE_ARRAY = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private ImageDao dao;

    @Before
    public void setUp() {
        Image newImage = new Image(TEST_IMAGE_BYTE_ARRAY);
        em.persist(newImage);
    }

    @Test
    public void testUploadImage() {
        final Image newImage = dao.uploadImage(NEW_IMAGE_BYTE_ARRAY);
        Assert.assertNotNull(newImage);
        Assert.assertEquals(NEW_IMAGE_BYTE_ARRAY, newImage.getBytes());
    }

    @Test
    public void testGetImage() {
        final Optional<Image> image = dao.getImage(ID);

        assertTrue(image.isPresent());
        Assert.assertNotNull(image.get().getBytes());
        Assert.assertArrayEquals(TEST_IMAGE_BYTE_ARRAY, image.get().getBytes());
    }

}
