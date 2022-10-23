package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.enums.JobOfferAvailability;
import ar.edu.itba.paw.models.enums.JobOfferModalities;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.*;

public class JobOfferJdbcDao implements JobOfferDao {

    private static final String JOB_OFFER_TABLE = "ofertaLaboral";
    private static final String ID = "id";
    private static final String ENTERPRISE_ID = "idEmpresa";
    private static final String CATEGORY_ID = "idRubro";
    private static final String POSITION = "posicion";
    private static final String DESCRIPTION = "descripcion";
    private static final String SALARY = "salario";
    private static final String MODALITY = "modalidad";
    private static final String AVAILABLE = "disponible";
    public static final String[] MODALITIES = new String[] {JobOfferModalities.IN_PERSON.getModality(), JobOfferModalities.REMOTE.getModality(),
                                            JobOfferModalities.REMOTE.getModality(), JobOfferModalities.MIXED.getModality()};
    public static final Set<String> modalitiesSet = new HashSet<>(Arrays.asList(MODALITIES));

    private final JdbcTemplate template;

    private final SimpleJdbcInsert insert;

    private CategoryDao categoryDao;
    private ContactDao contactDao;
    private EnterpriseDao enterpriseDao;

    private final RowMapper<JobOffer> JOB_OFFER_MAPPER = ((resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(CATEGORY_ID);
        Category category = null;
        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        Enterprise enterprise = enterpriseDao.findById(resultSet.getLong(ENTERPRISE_ID)).orElseThrow(UserNotFoundException::new);

        return new JobOffer(resultSet.getLong(ID),
                enterprise,
                category,
                resultSet.getString(POSITION),
                resultSet.getString(DESCRIPTION),
                resultSet.getBigDecimal(SALARY),
                resultSet.getString(MODALITY),
                resultSet.getString(AVAILABLE));
    });

    private static final RowMapper<Integer> COUNT_ROW_MAPPER = (rs, rowNum) -> rs.getInt("count");


    @Autowired
    public JobOfferJdbcDao(final DataSource ds, CategoryDao categoryDao, ContactDao contactDao, EnterpriseDao enterpriseDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(JOB_OFFER_TABLE)
                .usingGeneratedKeyColumns(ID);
        this.categoryDao = categoryDao;
        this.contactDao = contactDao;
        this.enterpriseDao = enterpriseDao;
    }


    @Override
    public JobOffer create(Enterprise enterprise, Category category, String position, String description, BigDecimal salary, String modality) {
        if(salary != null) {
            if (salary.compareTo(BigDecimal.valueOf(0)) <= 0)
                throw new InvalidParameterException("El salario no puede <= 0");
        }

        if(!modalitiesSet.contains(modality))
            modality = JobOfferModalities.NOT_SPECIFIED.getModality();

        final Map<String, Object> values = new HashMap<>();
        values.put(ENTERPRISE_ID, enterprise.getId());
        values.put(CATEGORY_ID, category.getId());
        values.put(POSITION, position);
        values.put(DESCRIPTION, description);
        values.put(SALARY, salary);
        values.put(MODALITY, modality);
        values.put(AVAILABLE, JobOfferAvailability.ACTIVE.getStatus());

        Number jobOfferID = insert.executeAndReturnKey(values);

        return new JobOffer(jobOfferID.longValue(), enterprise, category, position, description, salary, modality, JobOfferAvailability.ACTIVE.getStatus());
    }

    @Override
    public Optional<JobOffer> findById(long id) {
        return template.query("SELECT * FROM ofertaLaboral WHERE id = ?",
                new Object[]{ id }, JOB_OFFER_MAPPER).stream().findFirst();
    }

    @Override
    public List<JobOffer> findByEnterpriseId(long enterpriseID) {
        return template.query("SELECT * FROM ofertaLaboral WHERE idEmpresa = ? ORDER BY id DESC",
                new Object[]{ enterpriseID }, JOB_OFFER_MAPPER);
    }

    @Override
    public List<JobOffer> findByEnterpriseId(long enterpriseID, int page, int pageSize) {
        return template.query("SELECT * FROM ofertaLaboral WHERE idEmpresa = ? ORDER BY id DESC OFFSET ? LIMIT ?",
                new Object[]{ enterpriseID, pageSize * page, pageSize }, JOB_OFFER_MAPPER);
    }

    @Override
    public List<JobOffer> findActiveByEnterpriseId(long enterpriseID) {
        return template.query("SELECT * FROM ofertaLaboral WHERE idEmpresa = ? AND disponible = ? ORDER BY id DESC",
                new Object[]{ enterpriseID, JobOfferAvailability.ACTIVE.getStatus() }, JOB_OFFER_MAPPER);
    }

    @Override
    public List<JobOffer> getAllJobOffers() {
        return null;
    }

    @Override
    public Integer getJobOffersCount() {
        return null;
    }

    @Override
    public List<JobOffer> getJobOffersList(int page, int pageSize) {
        return null;
    }

    @Override
    public List<JobOffer> findActiveByEnterpriseId(long enterpriseID, int page, int pageSize) {
        return template.query("SELECT * FROM ofertaLaboral WHERE idEmpresa = ? AND disponible = ? ORDER BY id DESC OFFSET ? LIMIT ?",
                new Object[]{ enterpriseID, JobOfferAvailability.ACTIVE.getStatus(), pageSize * page, pageSize }, JOB_OFFER_MAPPER);
    }

    @Override
    public Integer getJobOffersCountForEnterprise(long enterpriseID) {
        return template.queryForObject("SELECT COUNT(*) FROM ofertaLaboral WHERE idEmpresa = ?",
                new Object[]{ enterpriseID}, Integer.class);
    }

    @Override
    public void closeJobOffer(long jobOfferID) {
        template.update("UPDATE ofertaLaboral SET disponible = ? WHERE id = ?", JobOfferAvailability.CLOSED.getStatus(), jobOfferID);
        contactDao.closeJobOfferForEveryone(jobOfferID);
    }

    @Override
    public void cancelJobOffer(long jobOfferID) {
        template.update("UPDATE ofertaLaboral SET disponible = ? WHERE id = ?", JobOfferAvailability.CANCELLED.getStatus(), jobOfferID);
        contactDao.cancelJobOfferForEveryone(jobOfferID);
    }
}
