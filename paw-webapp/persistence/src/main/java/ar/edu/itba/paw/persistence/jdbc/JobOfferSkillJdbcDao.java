package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferSkillDao;
import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

import static ar.edu.itba.paw.persistence.jdbc.SkillJdbcDao.SKILL_MAPPER;

@Repository
public class JobOfferSkillJdbcDao implements JobOfferSkillDao {

    private static final String JOB_OFFER_SKILL_TABLE = "aptitudOfertaLaboral";
    private static final String JOB_OFFER_ID = "idOferta";
    private static final String SKILL_ID = "idAptitud";
    private static final String JOB_OFFER_TABLE = "ofertaLaboral";
    private static final String JOB_OFFER_TABLE_ID = "id";
    private static final String JOB_OFFER_TABLE_ENTERPRISE_ID = "idEmpresa";
    private static final String JOB_OFFER_TABLE_CATEGORY_ID = "idRubro";
    private static final String JOB_OFFER_TABLE_POSITION = "posicion";
    private static final String JOB_OFFER_TABLE_DESCRIPTION = "descripcion";
    private static final String JOB_OFFER_TABLE_SALARY = "salario";
    private static final String JOB_OFFER_TABLE_MODALITY = "modalidad";
    private static final String JOB_OFFER_TABLE_AVAILABLE = "disponible";

    private final SkillDao skillDao;
    private final JobOfferDao jobOfferDao;
    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;
    private CategoryDao categoryDao;

    private final RowMapper<JobOffer> JOB_OFFER_MAPPER = ((resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(JOB_OFFER_TABLE_CATEGORY_ID);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new JobOffer(resultSet.getLong(JOB_OFFER_TABLE_ID),
                resultSet.getLong(JOB_OFFER_TABLE_ENTERPRISE_ID),
                category,
                resultSet.getString(JOB_OFFER_TABLE_POSITION),
                resultSet.getString(JOB_OFFER_TABLE_DESCRIPTION),
                resultSet.getBigDecimal(JOB_OFFER_TABLE_SALARY),
                resultSet.getString(JOB_OFFER_TABLE_MODALITY),
                resultSet.getString(JOB_OFFER_TABLE_AVAILABLE));
    });

    @Autowired
    public JobOfferSkillJdbcDao(final DataSource ds, final SkillDao skillDao, final JobOfferDao jobOfferDao, CategoryDao categoryDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(JOB_OFFER_SKILL_TABLE);
        this.skillDao = skillDao;
        this.jobOfferDao = jobOfferDao;
        this.categoryDao = categoryDao;
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
        Skill skill = skillDao.findByDescriptionOrCreate(skillDescription);
        return addSkillToJobOffer(skill.getId(), jobOfferID);
    }

    private List<Long> getJobOfferIDsWithSkill(long skillID){
        return template.query("SELECT idOferta FROM aptitudOfertaLaboral WHERE idAptitud = ?",
                new Object[]{ skillID }, (resultSet, rowNum) ->
            resultSet.getLong(JOB_OFFER_ID));
    }

    @Override
    public List<JobOffer> getJobOffersWithSkill(long skillID) {
        return template.query("SELECT * FROM aptitudOfertaLaboral aol JOIN ofertaLaboral ol ON aol.idOferta = ol.id WHERE aol.idAptitud = ?",
                new Object[]{ skillID }, JOB_OFFER_MAPPER);
    }

    @Override
    public List<JobOffer> getJobOffersWithSkill(String skillDescription) {
        Optional<Skill> skill = skillDao.findByDescription(skillDescription);
        return skill.isPresent() ? getJobOffersWithSkill(skill.get().getId()) : new ArrayList<>();
    }

    private List<Long> getSkillIDsForJobOffer(long jobOfferID){
        return template.query("SELECT idAptitud FROM aptitudOfertaLaboral WHERE idOferta = ?",
                new Object[]{ jobOfferID }, (resultSet, rowNum) ->
            resultSet.getLong(SKILL_ID));
    }

    @Override
    public List<Skill> getSkillsForJobOffer(long jobOfferID) {
        return template.query("SELECT a.id, a.descripcion FROM aptitudOfertaLaboral aol JOIN " +
                "aptitud a ON aol.idAptitud = a.id WHERE aol.idOferta = ?",
                new Object[]{jobOfferID}, SKILL_MAPPER);
    }
}
