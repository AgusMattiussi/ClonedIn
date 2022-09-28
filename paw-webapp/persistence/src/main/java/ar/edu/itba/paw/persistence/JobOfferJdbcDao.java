package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.persistence.exceptions.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.*;

@Repository
public class JobOfferJdbcDao implements JobOfferDao {

    private static final String JOB_OFFER_TABLE = "ofertaLaboral";
    private static final String ID = "id";
    private static final String ENTERPRISE_ID = "idEmpresa";
    private static final String CATEGORY_ID = "idRubro";
    private static final String POSITION = "posicion";
    private static final String DESCRIPTION = "descripcion";
    private static final String SALARY = "salario";
    private static final String MODALITY = "modalidad";

    public static final String[] MODALITIES = new String[] {"Remoto", "Presencial", "Mixto"};
    public static final Set<String> modalitiesSet = new HashSet<>(Arrays.asList(MODALITIES));

    private final JdbcTemplate template;

    private final SimpleJdbcInsert insert;

    private CategoryDao categoryDao;

    private final RowMapper<JobOffer> JOB_OFFER_MAPPER = ((resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(CATEGORY_ID);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new JobOffer(resultSet.getLong(ID),
                resultSet.getLong(ENTERPRISE_ID),
                category,
                resultSet.getString(POSITION),
                resultSet.getString(DESCRIPTION),
                resultSet.getBigDecimal(SALARY),
                resultSet.getString(MODALITY));
    });

    @Autowired
    public JobOfferJdbcDao(final DataSource ds, CategoryDao categoryDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(JOB_OFFER_TABLE)
                .usingGeneratedKeyColumns(ID);
        this.categoryDao = categoryDao;
    }


    @Override
    public JobOffer create(long enterpriseID, long categoryID, String position, String description, BigDecimal salary, String modality) {
        if(salary != null) {
            if (salary.compareTo(BigDecimal.valueOf(0)) <= 0)
                throw new InvalidParameterException("El salario no puede <= 0");
        }

        Category category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        if(!modalitiesSet.contains(modality))
            modality = "No especificado";

        final Map<String, Object> values = new HashMap<>();
        values.put(ENTERPRISE_ID, enterpriseID);
        values.put(CATEGORY_ID, categoryID);
        values.put(POSITION, position);
        values.put(DESCRIPTION, description);
        values.put(SALARY, salary);
        values.put(MODALITY, modality);

        Number jobOfferID = insert.executeAndReturnKey(values);

        return new JobOffer(jobOfferID.longValue(), enterpriseID, category, position, description, salary, modality);
    }

    @Override
    public Optional<JobOffer> findById(long id) {
        return template.query("SELECT * FROM " +  JOB_OFFER_TABLE + " WHERE " + ID + " = ?",
                new Object[]{ id }, JOB_OFFER_MAPPER).stream().findFirst();
    }

    @Override
    public List<JobOffer> findByEnterpriseId(long enterpriseID) {
        return template.query("SELECT * FROM " +  JOB_OFFER_TABLE + " WHERE " + ENTERPRISE_ID + " = ?",
                new Object[]{ enterpriseID }, JOB_OFFER_MAPPER);
    }
}
