package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.JobOfferSkill;
import ar.edu.itba.paw.models.Skill;
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
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class JobOfferSkillHibernateDaoTest {

    private static final String EMPTY_FIELD = "";
    private static final String NEW_SKILL = "newSkill";
    private static final String TEST_SKILL = "testSkill";
    private static final String EXISTING_ENTERPRISE_NAME = "testEnterpriseName";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JobOfferSkillDao jobOfferSkillDao;

    private JobOffer testJobOffer;
    private Enterprise testEnterprise;
    private Skill testSkill;
    private JobOfferSkill testJobOfferSkill;

    @Before
    public void setUp() {
        testEnterprise = new Enterprise(EXISTING_ENTERPRISE_NAME, EMPTY_FIELD, EMPTY_FIELD, EMPTY_FIELD, null,
                EMPTY_FIELD, null, EMPTY_FIELD, EMPTY_FIELD,null);
        em.persist(testEnterprise);
        testJobOffer = new JobOffer(testEnterprise, null, EMPTY_FIELD, EMPTY_FIELD, null, EMPTY_FIELD, EMPTY_FIELD);
        em.persist(testJobOffer);
        testSkill = new Skill(TEST_SKILL);
        em.persist(testSkill);
        testJobOfferSkill = new JobOfferSkill(testJobOffer, testSkill);
        em.persist(testJobOfferSkill);
    }

    @Test
    public void testAddSkillToJobOffer() {
        final Skill newSkill = new Skill(NEW_SKILL);
        jobOfferSkillDao.addSkillToJobOffer(newSkill, testJobOffer);

        TypedQuery<Skill> query = em.createQuery("SELECT jos.skill FROM JobOfferSkill AS jos WHERE jos.jobOffer = :jobOffer", Skill.class);
        query.setParameter("jobOffer", testJobOffer);
        final List<Skill> skillList = query.getResultList();

        Assert.assertEquals(2, skillList.size());
        Assert.assertEquals(newSkill, skillList.get(1));
    }

    @Test
    public void testGetJobOffersWithSkill() {
        final List<JobOffer> jobOfferList = jobOfferSkillDao.getJobOffersWithSkill(testSkill);
        Assert.assertEquals(1, jobOfferList.size());
        Assert.assertEquals(testJobOffer, jobOfferList.get(0));
    }

    @Test
    public void testGetSkillsForJobOffer() {
        final List<Skill> skillList = jobOfferSkillDao.getSkillsForJobOffer(testJobOffer);
        Assert.assertEquals(1, skillList.size());
        Assert.assertEquals(testSkill, skillList.get(0));
    }

}
