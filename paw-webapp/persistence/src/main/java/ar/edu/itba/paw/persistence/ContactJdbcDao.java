package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ContactDao;
import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.persistence.JobOfferDao;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.JobOffer;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private static final String PENDING_STATUS = "pendiente";

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;
    private final UserDao userDao;
    private final EnterpriseDao enterpriseDao;
    private final JobOfferDao jobOfferDao;

    @Autowired
    public ContactJdbcDao(final DataSource ds, UserDao userDao, EnterpriseDao enterpriseDao, JobOfferDao jobOfferDao){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(CONTACT_TABLE);
        this.userDao = userDao;
        this.enterpriseDao = enterpriseDao;
        this.jobOfferDao = jobOfferDao;
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
    public List<JobOffer> getJobOffersForUser(long userId) {
        List<Long> jobOfferIDs = getJobOfferIDsForUser(userId);
        List<JobOffer> jobOfferList = new ArrayList<>();

        for (long id : jobOfferIDs) {
            Optional<JobOffer> optJobOffer = jobOfferDao.findById(id);
            optJobOffer.ifPresent(jobOfferList::add);
        }

        return jobOfferList;
    }

    @Override
    public boolean alreadyContacted(long userID, long jobOfferID) {
        return template.queryForObject("SELECT COUNT(*) FROM " + CONTACT_TABLE + " WHERE " +
                USER_ID + " = ?" + " AND " + JOB_OFFER_ID + " = ?", new Object[]{ userID, jobOfferID},
                Integer.class) > 0;
    }

    /*@Override
    public void acceptJobOffer(long userID, long jobOfferID) {

    }

    @Override
    public void rejectJobOffer(long userID, long jobOfferID) {

    }*/

    //TODO: Manejar el caso en que ya exista el par
    @Override
    public void addContact(long enterpriseID, long userID, long jobOfferID) {
        final Map<String, Object> values = new HashMap<>();
        values.put(ENTERPRISE_ID, enterpriseID);
        values.put(USER_ID, userID);
        values.put(JOB_OFFER_ID, jobOfferID);
        values.put(STATUS, PENDING_STATUS);

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
