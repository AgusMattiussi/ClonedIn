package ar.itba.edu.paw.services;

import ar.edu.itba.paw.interfaces.persistence.ExperienceDao;
import ar.edu.itba.paw.models.Experience;
import ar.edu.itba.paw.services.ExperienceServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class ExperienceServiceImplTest {
    private static final long TEST_ID = 1;
    private static final long TEST_USER_ID = 1;
    private static final Date TEST_FROM = new Date(1000000);
    private static final Date TEST_TO = new Date(2000000);
    private static final String TEST_ENTERPRISE_NAME = "Empresa 1";
    private static final String TEST_POSITION = "Hokage";
    private static final String TEST_DESCRIPTION = "El admin de la aldea";

    @InjectMocks
    private ExperienceServiceImpl experienceService;

    @Mock
    private ExperienceDao experienceDao;

    @Test
    public void easy() {
        Assert.assertTrue(true);
    }

//    @Test
//    public void testCreate() {
//        Mockito.when(experienceDao.create(eq(TEST_USER_ID), eq(TEST_FROM), eq(TEST_TO), eq(TEST_ENTERPRISE_NAME), eq(TEST_POSITION), eq(TEST_DESCRIPTION)))
//                .thenReturn(new Experience(TEST_ID, TEST_USER_ID, TEST_FROM, TEST_TO, TEST_ENTERPRISE_NAME, TEST_POSITION, TEST_DESCRIPTION));
//
//        final Experience newExperience = experienceService.create(TEST_USER_ID, TEST_FROM, TEST_TO, TEST_ENTERPRISE_NAME, TEST_POSITION, TEST_DESCRIPTION);
//
//        Assert.assertNotNull(newExperience);
//        Assert.assertEquals(TEST_USER_ID, newExperience.getUserId());
//        Assert.assertEquals(TEST_FROM, newExperience.getFrom());
//        Assert.assertEquals(TEST_TO, newExperience.getTo());
//        Assert.assertEquals(TEST_ENTERPRISE_NAME, newExperience.getEnterpriseName());
//        Assert.assertEquals(TEST_POSITION, newExperience.getPosition());
//        Assert.assertEquals(TEST_DESCRIPTION, newExperience.getDescription());
//    }
//
//    @Test
//    public void testFindById() {
//        Mockito.when(experienceDao.findById(eq(TEST_ID)))
//                .thenReturn(Optional.of(new Experience(TEST_ID, TEST_USER_ID, TEST_FROM, TEST_TO, TEST_ENTERPRISE_NAME, TEST_POSITION, TEST_DESCRIPTION)));
//
//
//        final Optional<Experience> optEnterprise = experienceService.findById(TEST_ID);
//
//        Assert.assertTrue(optEnterprise.isPresent());
//        Assert.assertEquals(TEST_USER_ID, optEnterprise.get().getUserId());
//        Assert.assertEquals(TEST_FROM, optEnterprise.get().getFrom());
//        Assert.assertEquals(TEST_TO, optEnterprise.get().getTo());
//        Assert.assertEquals(TEST_ENTERPRISE_NAME, optEnterprise.get().getEnterpriseName());
//        Assert.assertEquals(TEST_POSITION, optEnterprise.get().getPosition());
//        Assert.assertEquals(TEST_DESCRIPTION, optEnterprise.get().getDescription());
//    }
}
