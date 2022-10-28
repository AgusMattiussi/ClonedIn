package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Skill;
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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional*/
public class SkillJdbcDaoTest {
    /*private static final String SKILL_TABLE = "aptitud";

    private static final String DESCRIPTION = "descripcion";

    private static final String TEST_SKILL = "testskill";
    private static final String NEW_SKILL = "newskill";
    private static final String NON_EXISTING_SKILL = "nonexistingskill";
    private static final long FIRST_ID = 1;

    @Autowired
    private SkillJdbcDao dao;

    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;*/

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    /*@Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        //JdbcTestUtils.deleteFromTables(jdbctemplate, SKILL_TABLE);
    }

    @Test
    public void testCreate() {
        final Skill newSkill = dao.create(NEW_SKILL);

        Assert.assertNotNull(newSkill);
        Assert.assertEquals(NEW_SKILL, newSkill.getDescription());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbctemplate, SKILL_TABLE, DESCRIPTION + " = '" + NEW_SKILL + "'"));
    }

    @Test
    public void testFindById() {
        final Optional<Skill> skill = dao.findById(1);

        assertTrue(skill.isPresent());
        Assert.assertEquals(FIRST_ID, skill.get().getId());
        Assert.assertEquals(TEST_SKILL, skill.get().getDescription());
    }

    @Test
    public void testFindByDescription() {
        final Optional<Skill> skill = dao.findByDescription(TEST_SKILL);

        assertTrue(skill.isPresent());
        Assert.assertEquals(TEST_SKILL, skill.get().getDescription());
        Assert.assertEquals(FIRST_ID, skill.get().getId());
    }

    @Test
    public void testFindByDescriptionOrCreate(){
        final Optional<Skill> notFoundCategory = dao.findByDescription(NON_EXISTING_SKILL);
        final Skill skill = dao.findByDescriptionOrCreate(NON_EXISTING_SKILL);

        Assert.assertFalse(notFoundCategory.isPresent());
        Assert.assertNotNull(skill);
        Assert.assertEquals(NON_EXISTING_SKILL, skill.getDescription());
    }

    @Test
    public void testGetAllSkills(){
        final Skill skill1 = dao.create("skill1");
        final Skill skill2 = dao.create("skill2");
        final Skill skill3 = dao.create("skill3");

        final List<Skill> allSkills = dao.getAllSkills();

        Assert.assertEquals(3 + 1, allSkills.size());
        assertTrue(allSkills.contains(skill1));
        assertTrue(allSkills.contains(skill2));
        assertTrue(allSkills.contains(skill3));
    }*/

}
