package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.persistence.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;

@Repository
public class ContactJdbcDao implements ContactDao {

    private static final String CONTACT_TABLE = "contactado";
    private static final String ENTERPRISE_ID = "idEmpresa";
    private static final String USER_ID = "idUsuario";
    private static final String JOB_OFFER_ID = "idOferta";
    private static final String STATUS = "estado";
    public static final String JOB_OFFER_TABLE = "ofertaLaboral";
    public static final String JOB_OFFER_TABLE_ID = "id";
    private static final String CATEGORY_ID = "idRubro";
    private static final String POSITION = "posicion";
    private static final String DESCRIPTION = "descripcion";
    private static final String SALARY = "salario";
    private static final String MODALITY = "modalidad";
    public static final String USER_TABLE = "usuario";
    public static final String USER_TABLE_ID = "id";
    public static final String USER_TABLE_NAME = "nombre";
    private static final String ENTERPRISE_TABLE = "empresa";
    private static final String ENTERPRISE_TABLE_NAME = "nombre";
    private static final String ENTERPRISE_TABLE_ID = "id";
    private static final String STATUS_PENDING = "pendiente";
    private static final String STATUS_ACCEPTED = "aceptada";
    private static final String STATUS_REJECTED = "rechazada";

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;
    private UserDao userDao;
    private final EnterpriseDao enterpriseDao;
    private final JobOfferDao jobOfferDao;
    private CategoryDao categoryDao;

    private final RowMapper<JobOfferWithStatus> JOB_OFFER_WITH_STATUS_MAPPER = ((resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(CATEGORY_ID);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new JobOfferWithStatus(resultSet.getLong(JOB_OFFER_TABLE_ID),
                resultSet.getLong(ENTERPRISE_ID),
                category,
                resultSet.getString(POSITION),
                resultSet.getString(DESCRIPTION),
                resultSet.getBigDecimal(SALARY),
                resultSet.getString(MODALITY),
                resultSet.getString(STATUS)
                );
    });

    private final RowMapper<JobOfferStatusUserData> JOB_OFFER_WITH_STATUS_USER_DATA_MAPPER = ((resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(CATEGORY_ID);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new JobOfferStatusUserData(resultSet.getLong(JOB_OFFER_TABLE_ID),
                resultSet.getLong(ENTERPRISE_ID),
                category,
                resultSet.getString(POSITION),
                resultSet.getString(DESCRIPTION),
                resultSet.getBigDecimal(SALARY),
                resultSet.getString(MODALITY),
                resultSet.getString(STATUS),
                resultSet.getString(USER_TABLE_NAME)
                );
    });

    private final RowMapper<JobOfferStatusEnterpriseData> JOB_OFFER_WITH_STATUS_ENTERPRISE_DATA_MAPPER = ((resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(CATEGORY_ID);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new JobOfferStatusEnterpriseData(resultSet.getLong(JOB_OFFER_TABLE_ID),
                resultSet.getLong(ENTERPRISE_ID),
                category,
                resultSet.getString(POSITION),
                resultSet.getString(DESCRIPTION),
                resultSet.getBigDecimal(SALARY),
                resultSet.getString(MODALITY),
                resultSet.getString(STATUS),
                resultSet.getString(ENTERPRISE_TABLE_NAME)
                );
    });

    @Autowired
    public ContactJdbcDao(final DataSource ds, UserDao userDao, EnterpriseDao enterpriseDao, JobOfferDao jobOfferDao, CategoryDao categoryDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(CONTACT_TABLE);
        this.userDao = userDao;
        this.enterpriseDao = enterpriseDao;
        this.jobOfferDao = jobOfferDao;
        this.categoryDao = categoryDao;
    }

    private List<Long> getEnterpriseIDsForUser(long userID){
        return template.query("SELECT " + ENTERPRISE_ID + " FROM " + CONTACT_TABLE + " WHERE " + USER_ID + " = ?",
                new Object[]{ userID }, (resultSet, rowNum) ->
            resultSet.getLong(ENTERPRISE_ID));
    }

    @Override
    public List<Enterprise> getEnterprisesForUser(long userID) {
        List<Long> enterpriseIDs = getEnterpriseIDsForUser(userID);
        List<Enterprise> enterpriseList = new ArrayList<>();

        for (Long id : enterpriseIDs) {
            Optional<Enterprise> currentEnterprise = enterpriseDao.findById(id);
            currentEnterprise.ifPresent(enterpriseList::add);
        }

        return enterpriseList;
    }

    private List<Long> getUserIDsForEnterprise(long enterpriseID){
        return template.query("SELECT " + USER_ID + " FROM " + CONTACT_TABLE + " WHERE " + ENTERPRISE_ID + " = ?",
                new Object[]{ enterpriseID }, (resultSet, rowNum) ->
            resultSet.getLong(USER_ID));
    }

    @Override
    public List<User> getUsersForEnterprise(long enterpriseID) {
        List<Long> userIDs = getUserIDsForEnterprise(enterpriseID);
        List<User> userList = new ArrayList<>();

        for (Long id : userIDs) {
            Optional<User> currentUser = userDao.findById(id);
            currentUser.ifPresent(userList::add);
        }

        return userList;
    }

    private List<Long> getJobOfferIDsForUser(long userId){
        return template.query("SELECT " + JOB_OFFER_ID + " FROM " + CONTACT_TABLE + " WHERE " + USER_ID + " = ?",
                new Object[]{ userId }, (resultSet, rowNum) ->
            resultSet.getLong(JOB_OFFER_ID));
    }

    @Override
    public List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId) {
        return template.query("SELECT ol." + JOB_OFFER_TABLE_ID + ", ol." + ENTERPRISE_ID + ", ol." + POSITION + ", ol." +
                DESCRIPTION + ", ol." + SALARY + ", ol." + CATEGORY_ID + ", ol." + MODALITY + ", c." + STATUS +
                " FROM " + JOB_OFFER_TABLE + " ol JOIN "+ CONTACT_TABLE +  " c ON ol."+JOB_OFFER_TABLE_ID + " = c." + JOB_OFFER_ID +
                " WHERE c." + USER_ID + " = ?", new Object[]{ userId }, JOB_OFFER_WITH_STATUS_MAPPER);
    }


    @Override
    public List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID) {
        return template.query("SELECT ol." + JOB_OFFER_TABLE_ID + ", ol." + ENTERPRISE_ID + ", ol." + POSITION + ", ol." +
                DESCRIPTION + ", ol." + SALARY + ", ol." + CATEGORY_ID + ", ol." + MODALITY + ", c." + STATUS + ", u." + USER_TABLE_NAME +
                " FROM " + JOB_OFFER_TABLE + " ol JOIN "+ CONTACT_TABLE +  " c ON ol."+JOB_OFFER_TABLE_ID + " = c." + JOB_OFFER_ID +
                " JOIN " + USER_TABLE + " u ON u." + USER_TABLE_ID + " = c." + USER_ID +
                " WHERE c." + ENTERPRISE_ID + " = ?", new Object[]{ enterpriseID }, JOB_OFFER_WITH_STATUS_USER_DATA_MAPPER);
    }

    @Override
    public List<JobOfferStatusEnterpriseData> getJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize) {
        return template.query("SELECT ol." + JOB_OFFER_TABLE_ID + ", ol." + ENTERPRISE_ID + ", ol." + POSITION + ", ol." +
                DESCRIPTION + ", ol." + SALARY + ", ol." + CATEGORY_ID + ", ol." + MODALITY + ", c." + STATUS + ", e." + ENTERPRISE_TABLE_NAME +
                " FROM " + JOB_OFFER_TABLE + " ol JOIN "+ CONTACT_TABLE +  " c ON ol."+JOB_OFFER_TABLE_ID + " = c." + JOB_OFFER_ID +
                " JOIN " + ENTERPRISE_TABLE + " e ON e." + ENTERPRISE_TABLE_ID + " = c." + ENTERPRISE_ID +
                " WHERE c." + USER_ID + " = ?" + " OFFSET ? LIMIT ? ", new Object[]{ userID, page, pageSize }, JOB_OFFER_WITH_STATUS_ENTERPRISE_DATA_MAPPER);
    }


    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        return template.queryForObject("SELECT COUNT(*) FROM " + CONTACT_TABLE + " WHERE " +
                USER_ID + " = ?" + " AND " + JOB_OFFER_ID + " = ?", new Object[]{ userID, jobOfferID},
                Integer.class) > 0;
    }

    @Override
    public String getStatus(long userID, long jobOfferID) {
        return template.queryForObject("SELECT " + STATUS + " FROM " + CONTACT_TABLE + " WHERE " +
                USER_ID + " = ?" + " AND " + JOB_OFFER_ID + " = ?", new Object[]{ userID, jobOfferID},
                String.class);
    }

    private void updateStatus(long userID, long jobOfferID, String newStatus){
        template.update("UPDATE " + CONTACT_TABLE + " SET " + STATUS + " = ?" +
                " WHERE " + USER_ID + " = ?" + " AND " + JOB_OFFER_ID + " = ?",
                new Object[]{newStatus, userID, jobOfferID});
    }

    @Override
    public void acceptJobOffer(long userID, long jobOfferID) {
        updateStatus(userID, jobOfferID, STATUS_ACCEPTED);
    }

    @Override
    public void rejectJobOffer(long userID, long jobOfferID) {
        updateStatus(userID, jobOfferID, STATUS_REJECTED);
    }


    //TODO: Manejar el caso en que ya exista el par
    @Override
    public void addContact(long enterpriseID, long userID, long jobOfferID) {
        final Map<String, Object> values = new HashMap<>();
        values.put(ENTERPRISE_ID, enterpriseID);
        values.put(USER_ID, userID);
        values.put(JOB_OFFER_ID, jobOfferID);
        values.put(STATUS, STATUS_PENDING);

        insert.execute(values);
    }




    /*@Override
    public Category create(String name) {
        Optional<Category> existing = findByName(name);
        if(existing.isPresent())
            return existing.get();

        final Map<String, Object> values = new HashMap<>();
        values.put(NAME, name);

        Number categoryId = insert.executeAndReturnKey(values);

        return new Category(categoryId.longValue(), name);
    }*/
}
