package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.enums.JobOfferStatuses;
import ar.edu.itba.paw.persistence.exceptions.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
    public static final String AVAILABLE = "disponible";
    public static final String USER_TABLE = "usuario";
    private static final String USER_TABLE_ID = "id";
    private static final String USER_TABLE_NAME = "nombre";
    private static final String USER_TABLE_EMAIL = "email";
    private static final String USER_TABLE_PASSWORD = "contrasenia";
    private static final String USER_TABLE_LOCATION = "ubicacion";
    private static final String USER_TABLE_CATEGORY_ID_FK = "idRubro";
    private static final String USER_TABLE_CURRENT_POSITION = "posicionActual";
    private static final String USER_TABLE_DESCRIPTION = "descripcion";
    private static final String USER_TABLE_EDUCATION = "educacion";
    private static final String USER_TABLE_VISIBILITY = "visibilidad";
    private static final String IMAGE_ID = "idImagen";
    private static final String ENTERPRISE_TABLE = "empresa";
    private static final String ENTERPRISE_TABLE_ID = "id";
    private static final String ENTERPRISE_TABLE_NAME = "nombre";
    private static final String ENTERPRISE_TABLE_EMAIL = "email";
    private static final String ENTERPRISE_TABLE_PASSWORD = "contrasenia";
    private static final String ENTERPRISE_TABLE_LOCATION = "ubicacion";
    private static final String ENTERPRISE_TABLE_CATEGORY_ID_FK = "idRubro";
    private static final String ENTERPRISE_TABLE_DESCRIPTION = "descripcion";


    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;
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
                resultSet.getString(AVAILABLE),
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
                resultSet.getString(AVAILABLE),
                resultSet.getString(STATUS),
                resultSet.getString(USER_TABLE_NAME),
                resultSet.getLong("idUsuario")
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
                resultSet.getString(AVAILABLE),
                resultSet.getString(STATUS),
                resultSet.getString(ENTERPRISE_TABLE_NAME)
                );
    });

    protected final RowMapper<Enterprise> ENTERPRISE_MAPPER = (resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(ENTERPRISE_TABLE_CATEGORY_ID_FK);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new Enterprise(resultSet.getLong(ENTERPRISE_TABLE_ID),
                resultSet.getString(ENTERPRISE_TABLE_NAME),
                resultSet.getString(ENTERPRISE_TABLE_EMAIL),
                resultSet.getString(ENTERPRISE_TABLE_PASSWORD),
                resultSet.getString(ENTERPRISE_TABLE_LOCATION),
                category,
                resultSet.getString(ENTERPRISE_TABLE_DESCRIPTION),
                resultSet.getLong(IMAGE_ID));
    };

    private final RowMapper<User> USER_MAPPER = (resultSet, rowNum) -> {
        long categoryID = resultSet.getLong(USER_TABLE_CATEGORY_ID_FK);
        Category category = null;

        if(categoryID != 0)
            category = categoryDao.findById(categoryID).orElseThrow(CategoryNotFoundException::new);

        return new User(resultSet.getLong(USER_TABLE_ID),
                resultSet.getString(USER_TABLE_EMAIL),
                resultSet.getString(USER_TABLE_PASSWORD),
                resultSet.getString(USER_TABLE_NAME),
                resultSet.getString(USER_TABLE_LOCATION),
                category,
                resultSet.getString(USER_TABLE_CURRENT_POSITION),
                resultSet.getString(USER_TABLE_DESCRIPTION),
                resultSet.getString(USER_TABLE_EDUCATION),
                resultSet.getInt(USER_TABLE_VISIBILITY),
                resultSet.getLong(IMAGE_ID));
    };

    @Autowired
    public ContactJdbcDao(final DataSource ds, CategoryDao categoryDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(CONTACT_TABLE);
        this.categoryDao = categoryDao;
    }

    private List<Long> getEnterpriseIDsForUser(long userID){
        return template.query("SELECT idEmpresa FROM contactado WHERE idUsuario = ?",
                new Object[]{ userID }, (resultSet, rowNum) ->
            resultSet.getLong(ENTERPRISE_ID));
    }

    @Override
    public List<Enterprise> getEnterprisesForUser(long userID) {
        return template.query("SELECT e.id, e.nombre, e.email, e.contrasenia, e.ubicacion, e.idRubro, e.descripcion, e.idImagen " +
                        "FROM contactado c JOIN empresa e ON c.idEmpresa = e.id JOIN usuario u ON c.idUsuario = u.id WHERE c.idUsuario = ?",
                new Object[]{ userID }, ENTERPRISE_MAPPER);
    }

    private List<Long> getUserIDsForEnterprise(long enterpriseID){
        return template.query("SELECT idUsuario FROM contactado WHERE idEmpresa = ?",
                new Object[]{ enterpriseID }, (resultSet, rowNum) ->
            resultSet.getLong(USER_ID));
    }

    @Override
    public List<User> getUsersForEnterprise(long enterpriseID) {
        return template.query("SELECT u.id, u.nombre, u.email, u.contrasenia, u.ubicacion, u.idRubro, u.posicionActual, u.descripcion, u.educacion, u.visibilidad, " +
                        "u.idImagen FROM contactado c JOIN empresa e ON c.idEmpresa = e.id JOIN usuario u ON c.idUsuario = u.id WHERE c.idEmpresa = ?",
                new Object[]{ enterpriseID }, USER_MAPPER);
    }

    private List<Long> getJobOfferIDsForUser(long userId){
        return template.query("SELECT idOferta FROM contactado WHERE idUsuario = ?",
                new Object[]{ userId }, (resultSet, rowNum) ->
            resultSet.getLong(JOB_OFFER_ID));
    }

    @Override
    public List<JobOfferWithStatus> getJobOffersWithStatusForUser(long userId) {
        return template.query("SELECT ol.id, ol.idEmpresa, ol.posicion, ol.descripcion, ol.salario, ol.idRubro, ol.modalidad, ol.disponible, c.estado " +
                "FROM ofertaLaboral ol JOIN contactado c ON ol.id = c.idOferta WHERE c.idUsuario = ?",
                new Object[]{ userId }, JOB_OFFER_WITH_STATUS_MAPPER);
    }

    @Override
    public List<JobOfferStatusUserData> getJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize, String status) {
        return template.query("SELECT ol.id, ol.idEmpresa, ol.posicion, ol.descripcion, ol.salario, ol.idRubro, ol.modalidad, ol.disponible, c.estado, u.nombre, u.id as idUsuario" +
                " FROM ofertaLaboral ol JOIN contactado c ON ol.id = c.idOferta JOIN usuario u ON u.id = c.idUsuario" +
                " WHERE c.idEmpresa = ? AND c.estado = ? OFFSET ? LIMIT ? ",
                new Object[]{ enterpriseID, status, pageSize * page, pageSize }, JOB_OFFER_WITH_STATUS_USER_DATA_MAPPER);
    }

    @Override
    public List<JobOfferStatusUserData> getAllJobOffersWithStatusUserData(long enterpriseID, int page, int pageSize) {
        return template.query("SELECT ol.id, ol.idEmpresa, ol.posicion, ol.descripcion, ol.salario, ol.idRubro, ol.modalidad, ol.disponible, c.estado, u.nombre, u.id as idUsuario" +
                        " FROM ofertaLaboral ol JOIN contactado c ON ol.id = c.idOferta JOIN usuario u ON u.id = c.idUsuario" +
                        " WHERE c.idEmpresa = ? OFFSET ? LIMIT ? ",
                new Object[]{ enterpriseID, pageSize * page, pageSize }, JOB_OFFER_WITH_STATUS_USER_DATA_MAPPER);
    }

    @Override
    public List<JobOfferStatusEnterpriseData> getJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize, String status) {
        return template.query("SELECT ol.id, ol.idEmpresa, ol.posicion, ol.descripcion, ol.salario, ol.idRubro, ol.modalidad, ol.disponible, c.estado, e.nombre" +
                " FROM ofertaLaboral ol JOIN contactado c ON ol.id = c.idOferta JOIN empresa e ON e.id = c.idEmpresa" +
                " WHERE c.idUsuario = ? AND c.estado = ? OFFSET ? LIMIT ? ",
                new Object[]{ userID, status, pageSize * page, pageSize }, JOB_OFFER_WITH_STATUS_ENTERPRISE_DATA_MAPPER);
    }

    @Override
    public List<JobOfferStatusEnterpriseData> getAllJobOffersWithStatusEnterpriseData(long userID, int page, int pageSize) {
        return template.query("SELECT ol.id, ol.idEmpresa, ol.posicion, ol.descripcion, ol.salario, ol.idRubro, ol.modalidad, ol.disponible, c.estado, e.nombre" +
                        " FROM ofertaLaboral ol JOIN contactado c ON ol.id = c.idOferta JOIN empresa e ON e.id = c.idEmpresa" +
                        " WHERE c.idUsuario = ? OFFSET ? LIMIT ? ",
                new Object[]{ userID, pageSize * page, pageSize }, JOB_OFFER_WITH_STATUS_ENTERPRISE_DATA_MAPPER);
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        return template.queryForObject("SELECT COUNT(*) FROM contactado WHERE idUsuario = ? AND idOferta = ?",
                new Object[]{ userID, jobOfferID}, Integer.class) > 0;
    }

    @Override
    public String getStatus(long userID, long jobOfferID) {
        return template.queryForObject("SELECT estado FROM contactado WHERE idUsuario = ? AND idOferta = ?",
                new Object[]{ userID, jobOfferID}, String.class);
    }

    private void updateStatus(long userID, long jobOfferID, String newStatus){
        template.update("UPDATE contactado SET estado = ? WHERE idUsuario = ? AND idOferta = ?",
                new Object[]{newStatus, userID, jobOfferID});
    }

    private void updateStatusForEveryone(long jobOfferID, String newStatus){
        template.update("UPDATE contactado SET estado = ? WHERE idOferta = ?",
                new Object[]{newStatus, jobOfferID});
    }

    @Override
    public void acceptJobOffer(long userID, long jobOfferID) {
        updateStatus(userID, jobOfferID, JobOfferStatuses.ACCEPTED.getStatus());
    }

    @Override
    public void rejectJobOffer(long userID, long jobOfferID) {
        updateStatus(userID, jobOfferID, JobOfferStatuses.DECLINED.getStatus());
    }

    @Override
    public void cancelJobOffer(long userID, long jobOfferID) {
        updateStatus(userID, jobOfferID, JobOfferStatuses.CANCELLED.getStatus());
    }

    @Override
    public void cancelJobOfferForEveryone(long jobOfferID) {
        updateStatusForEveryone(jobOfferID, JobOfferStatuses.CANCELLED.getStatus());
    }

    @Override
    public void closeJobOffer(long userID, long jobOfferID) {
        updateStatus(userID, jobOfferID, JobOfferStatuses.CLOSED.getStatus());
    }

    @Override
    public void closeJobOfferForEveryone(long jobOfferID) {
        updateStatusForEveryone(jobOfferID, JobOfferStatuses.CLOSED.getStatus());
    }

    @Override
    public void addContact(long enterpriseID, long userID, long jobOfferID) {
        final Map<String, Object> values = new HashMap<>();
        values.put(ENTERPRISE_ID, enterpriseID);
        values.put(USER_ID, userID);
        values.put(JOB_OFFER_ID, jobOfferID);
        values.put(STATUS, JobOfferStatuses.PENDING.getStatus());

        insert.execute(values);
    }

    @Override
    public long getContactsCountForEnterprise(long enterpriseID) {
        return template.queryForObject("SELECT COUNT(*) FROM contactado WHERE idEmpresa = ?",
                new Object[]{ enterpriseID }, Long.class);
    }

    @Override
    public long getContactsCountForUser(long userID) {
        return template.queryForObject("SELECT COUNT(*) FROM contactado WHERE idUsuario = ?",
                new Object[]{ userID }, Long.class);
    }
}
