package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class JobOfferSkillJdbcDao implements JobOfferSkillDao {

    private static final String JOB_OFFER_SKILL_TABLE = "aptitudOfertaLaboral";
    private static final String JOB_OFFER_ID = "idOferta";
    private static final String SKILL_ID = "idAptitud";
    private final SkillDao skillDao;
    private final JobOfferDao jobOfferDao;
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    @Autowired
    public JobOfferSkillJdbcDao(final DataSource ds, final SkillDao skillDao, final JobOfferDao jobOfferDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(JOB_OFFER_SKILL_TABLE);
        this.skillDao = skillDao;
        this.jobOfferDao = jobOfferDao;
    }

    @Override
    public boolean addSkillToJobOffer(long skillID, long jobOfferID) {
        final Map<String, Object> values = new HashMap<>();
        values.put(JOB_OFFER_ID, jobOfferID);
        values.put(SKILL_ID, skillID);

        return insert.execute(values) > 0;
    }

    @Override
    public boolean addSkillToJobOffer(String skillDescription, long jobOfferID) {
        Optional<Skill> skill = skillDao.findByDescription(skillDescription);
        //Skill skill = skillDao.findByDescriptionOrCreate(skillDescription);
        if(skill.isPresent())
            return addSkillToJobOffer(skill.get().getId(), jobOfferID);
        return false;
    }

    private List<Long> getJobOfferIDsWithSkill(long skillID){
        return template.query("SELECT " + JOB_OFFER_ID + " FROM " + JOB_OFFER_SKILL_TABLE + " WHERE " + SKILL_ID + " = ?",
                new Object[]{ skillID }, (resultSet, rowNum) ->
            resultSet.getLong(JOB_OFFER_ID));
    }

    @Override
    public List<JobOffer> getJobOffersWithSkill(long skillID) {
        List<Long> jobOfferIDs = getJobOfferIDsWithSkill(skillID);
        List<JobOffer> jobOfferList = new ArrayList<>();

        for (Long id : jobOfferIDs) {
            Optional<JobOffer> currentJobOffer = jobOfferDao.findById(id);
            currentJobOffer.ifPresent(jobOfferList::add);
        }

        return jobOfferList;
    }

    @Override
    public List<JobOffer> getJobOffersWithSkill(String skillDescription) {
        Optional<Skill> skill = skillDao.findByDescription(skillDescription);
        return skill.isPresent() ? getJobOffersWithSkill(skill.get().getId()) : new ArrayList<>();
    }

    private List<Long> getSkillIDsForJobOffer(long jobOfferID){
        return template.query("SELECT " + SKILL_ID + " FROM " + JOB_OFFER_SKILL_TABLE + " WHERE " + JOB_OFFER_ID + " = ?",
                new Object[]{ jobOfferID }, (resultSet, rowNum) ->
            resultSet.getLong(SKILL_ID));
    }

    @Override
    public List<Skill> getSkillsForJobOffer(long jobOfferID) {
        List<Long> skillIDs = getSkillIDsForJobOffer(jobOfferID);
        List<Skill> skillList = new ArrayList<>();

        for (Long id : skillIDs) {
            Optional<Skill> currentSkill = skillDao.findById(id);
            currentSkill.ifPresent(skillList::add);
        }

        return skillList;
    }
}
