package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Rollback
@Transactional
public class JobOfferSkillJdbcDaoTest {

    private static final String USER_SKILL_TABLE = "aptitudUsuario";
    private static final String SKILL_ID = "idAptitud";
    private static final String USER_ID = "idUsuario";
    private static final String NEW_SKILL = "newskill";
    private static final String NEW_POSITION = "newPosition";
    private static final String NEW_DESCRIPTION = "newDescription";
    private static final String TEST_ENTERPRISE_EMAIL = "empresaurio@gmail.com";
    private static final String TEST_SKILL = "testskill";


    @Autowired
    private JobOfferSkillJdbcDao jobOfferSkillDao;

    @Autowired
    private SkillDao skillDao;
    @Autowired
    private JobOfferDao jobOfferDao;
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private DataSource ds;

    private JdbcTemplate jdbctemplate;
    private Enterprise testEnterprise;
    private JobOffer testJobOffer;

    @Before
    public void setUp() {
        jdbctemplate = new JdbcTemplate(ds);
        testEnterprise = enterpriseDao.findByEmail(TEST_ENTERPRISE_EMAIL).get();
        testJobOffer = jobOfferDao.findById(1).get();
    }

    @Test
    public void testGetSkillsForJobOffer() {

        final List<Skill> skillList = jobOfferSkillDao.getSkillsForJobOffer(1);

        Assert.assertEquals(1, skillList.size());
        Assert.assertEquals(1, skillList.get(0).getId());
    }

    @Test
    public void testGetJobOffersWithSkillUsingID() {
        final List<JobOffer> jobOfferList = jobOfferSkillDao.getJobOffersWithSkill(1);

        Assert.assertEquals(1, jobOfferList.size());
        Assert.assertEquals(1, jobOfferList.get(0).getId());
    }

    @Test
    public void testGetJobOffersWithSkillUsingDescription() {
        final List<JobOffer> jobOfferList = jobOfferSkillDao.getJobOffersWithSkill(TEST_SKILL);

        Assert.assertEquals(1, jobOfferList.size());
        Assert.assertEquals(1, jobOfferList.get(0).getId());
    }

    @Test
    public void addSkillToJobOfferUsingIDTest() {
        final Skill skill = skillDao.create(NEW_SKILL);

        final boolean added = jobOfferSkillDao.addSkillToJobOffer(skill.getId(), testJobOffer.getId());
        final List<Skill> skillList = jobOfferSkillDao.getSkillsForJobOffer(testJobOffer.getId());
        final List<JobOffer> jobOfferList = jobOfferSkillDao.getJobOffersWithSkill(skill.getId());

        Assert.assertTrue(added);
        Assert.assertEquals(2, skillList.size());
        Assert.assertTrue(skillList.contains(skill));
        Assert.assertEquals(1, jobOfferList.size());
        Assert.assertTrue(jobOfferList.contains(testJobOffer));
    }

    @Test
    public void addSkillToJobOfferUsingDescriptionTest() {
        final Skill skill = skillDao.create(NEW_SKILL + "2");

        final boolean added = jobOfferSkillDao.addSkillToJobOffer(skill.getDescription(), testJobOffer.getId());
        final List<Skill> skillList = jobOfferSkillDao.getSkillsForJobOffer(testJobOffer.getId());
        final List<JobOffer> jobOfferList = jobOfferSkillDao.getJobOffersWithSkill(skill.getDescription());

        Assert.assertTrue(added);
        Assert.assertEquals(2, skillList.size());
        Assert.assertTrue(skillList.contains(skill));
        Assert.assertEquals(1, jobOfferList.size());
        Assert.assertTrue(jobOfferList.contains(testJobOffer));
    }

}
