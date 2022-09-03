package ar.itba.edu.paw.services;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.services.SkillServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class SkillServiceImplTest {
    private static final String TEST_SKILL = "testSkill";
    private static final long TEST_ID = 1;

    @InjectMocks
    private SkillServiceImpl skillService;

    @Mock
    private SkillDao skillDao;

    @Test
    public void testCreate() {
        Mockito.when(skillDao.create(eq(TEST_SKILL)))
                .thenReturn(new Skill(TEST_ID, TEST_SKILL));

        final Skill newSkill = skillService.create(TEST_SKILL);

        Assert.assertNotNull(newSkill);
        Assert.assertEquals(TEST_SKILL, newSkill.getDescription());
        Assert.assertEquals(TEST_ID, newSkill.getId());
    }

    @Test
    public void testFindByDescription() {
        Mockito.when(skillDao.findByDescription(eq(TEST_SKILL)))
                .thenReturn(Optional.of(new Skill(TEST_ID, TEST_SKILL)));

        final Optional<Skill> optSkill = skillService.findByDescription(TEST_SKILL);

        Assert.assertTrue(optSkill.isPresent());
        Assert.assertEquals(TEST_SKILL, optSkill.get().getDescription());
        Assert.assertEquals(TEST_ID, optSkill.get().getId());

    }

    @Test
    public void testFindById() {
        Mockito.when(skillDao.findById(eq(TEST_ID)))
                .thenReturn(Optional.of(new Skill(TEST_ID, TEST_SKILL)));

        final Optional<Skill> optSkill = skillService.findById(TEST_ID);

        Assert.assertTrue(optSkill.isPresent());
        Assert.assertEquals(TEST_ID, optSkill.get().getId());
        Assert.assertEquals(TEST_SKILL, optSkill.get().getDescription());
    }
}
