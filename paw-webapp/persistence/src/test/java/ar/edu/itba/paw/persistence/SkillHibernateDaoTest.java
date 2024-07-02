package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.persistence.config.TestConfig;
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
public class SkillHibernateDaoTest {
    private static final String TEST_SKILL_1 = "testSkill1";
    private static final String TEST_SKILL_2 = "testSkill2";
    private static final String TEST_SKILL_3 = "testSkill3";
    private static final String NEW_SKILL = "newSkill";
    private static final String NON_EXISTING_SKILL = "nonExistingSkill";
    private static final long SKILLS_COUNT = 3;

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private SkillDao dao;
    private Skill testSkill;

    @Before
    public void setUp() {
        testSkill = new Skill(TEST_SKILL_1);
        Skill testSkill2 = new Skill(TEST_SKILL_2);
        Skill testSkill3 = new Skill(TEST_SKILL_3);
        em.persist(testSkill);
        em.persist(testSkill2);
        em.persist(testSkill3);
    }

    @Test
    public void testCreate() {
        final Skill newSkill = dao.create(NEW_SKILL);
        assertNotNull(newSkill);
        assertEquals(NEW_SKILL, newSkill.getDescription());
        assertEquals(newSkill, em.find(Skill.class, newSkill.getId()));
    }

    @Test
    public void testFindById() {
        final Optional<Skill> skill = dao.findById(testSkill.getId());
        assertTrue(skill.isPresent());
        assertEquals(TEST_SKILL_1, skill.get().getDescription());
    }

    @Test
    public void testFindByDescription() {
        final Optional<Skill> skill = dao.findByDescription(TEST_SKILL_1);
        assertTrue(skill.isPresent());
        assertEquals(TEST_SKILL_1, skill.get().getDescription());
    }

    @Test
    public void testFindByDescriptionOrCreate(){
        final Skill skill = dao.findByDescriptionOrCreate(NON_EXISTING_SKILL);
        assertNotNull(skill);
        assertEquals(NON_EXISTING_SKILL, skill.getDescription());
    }

    @Test
    public void testGetAllSkills(){
        final List<Skill> allSkills = dao.getAllSkills(null, 0, 3);
        assertEquals(SKILLS_COUNT, allSkills.size());
    }

}
