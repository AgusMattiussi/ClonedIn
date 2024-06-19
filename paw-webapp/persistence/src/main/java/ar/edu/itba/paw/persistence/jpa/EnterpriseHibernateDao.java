package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.enums.EmployeeRanges;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
//@CacheConfig(cacheNames = "enterprises-cache")
public class EnterpriseHibernateDao implements EnterpriseDao {

    public static final Image DEFAULT_IMAGE = null;

    @PersistenceContext
    private EntityManager em;

    @Override
//    @CachePut(key = "#result.id")
    public Enterprise create(String email, String name, String password, String location, Category category, EmployeeRanges workers,
                             Integer year, String link, String description) {
        final Enterprise enterprise = new Enterprise(name, email, password, location, category, workers.getStringValue(),
                year, link, description, DEFAULT_IMAGE);
        em.persist(enterprise);
        return enterprise;
    }

    @Override
    public Optional<Enterprise> findByEmail(String email) {
        final TypedQuery<Enterprise> query = em.createQuery("SELECT e from Enterprise as e where e.email = :email", Enterprise.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
//    @Cacheable(key = "#enterpriseId", unless = "#result == null")
    public Optional<Enterprise> findById(long enterpriseId) {
        return Optional.ofNullable(em.find(Enterprise.class, enterpriseId));
    }

    @Override
    public Optional<Long> getIdForEmail(String email) {
        Query query = em.createQuery("SELECT e.id FROM Enterprise e WHERE e.email = :email");
        query.setParameter("email", email);
        Long id = (Long) query.getSingleResult();
        return  Optional.ofNullable(id);
    }

    @Override
    public void changePassword(String email, String password) {
        Query query = em.createQuery("UPDATE Enterprise SET password = :password WHERE email = :email");
        query.setParameter("password", password);
        query.setParameter("email", email);
        query.executeUpdate();
    }

    @Override
    public boolean enterpriseExists(String email) {
        Query query = em.createQuery("SELECT COUNT(e) FROM Enterprise e WHERE e.email = :email");
        query.setParameter("email", email);
        return (Long) query.getSingleResult() > 0;
    }

    @Override
//    @CacheEvict(key = "#enterpriseID")
    public void updateName(long enterpriseID, String newName) {
        Query query = em.createQuery("UPDATE Enterprise SET name = :newName WHERE id = :enterpriseID");
        query.setParameter("newName", newName);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
//    @CacheEvict(key = "#enterpriseID")
    public void updateWorkers(long enterpriseID, EmployeeRanges newWorkers) {
        Query query = em.createQuery("UPDATE Enterprise SET workers = :newWorkers WHERE id = :enterpriseID");
        query.setParameter("newWorkers", newWorkers.getStringValue());
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
//    @CacheEvict(key = "#enterpriseID")
    public void updateYear(long enterpriseID, Integer newYear) {
        Query query = em.createQuery("UPDATE Enterprise SET year = :newYear WHERE id = :enterpriseID");
        query.setParameter("newYear", newYear);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
//    @CacheEvict(key = "#enterpriseID")
    public void updateLink(long enterpriseID, String newLink) {
        Query query = em.createQuery("UPDATE Enterprise SET link = :newLink WHERE id = :enterpriseID");
        query.setParameter("newLink", newLink);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
//    @CacheEvict(key = "#enterpriseID")
    public void updateDescription(long enterpriseID, String newDescription) {
        Query query = em.createQuery("UPDATE Enterprise SET description = :newDescription WHERE id = :enterpriseID");
        query.setParameter("newDescription", newDescription);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
//    @CacheEvict(key = "#enterpriseID")
    public void updateLocation(long enterpriseID, String newLocation) {
        Query query = em.createQuery("UPDATE Enterprise SET location = :newLocation WHERE id = :enterpriseID");
        query.setParameter("newLocation", newLocation);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
//    @CacheEvict(key = "#enterpriseID")
    public void updateCategory(long enterpriseID, Category newCategory) {
        Query query = em.createQuery("UPDATE Enterprise SET category = :newCategory WHERE id = :enterpriseID");
        query.setParameter("newCategory", newCategory);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
/*    @Caching(evict = {
            @CacheEvict(value ="enterprises-cache", key = "#enterprise.id"),
            @CacheEvict(value = "images-cache", key = "#result", condition = "#result > 0")
    })*/
    public long updateEnterpriseProfileImage(Enterprise enterprise, Image image) {
        Image oldImage = enterprise.getImage();

        enterprise.setImage(image);
        em.persist(enterprise);

        if(oldImage != null){
            long oldId = oldImage.getId();
            oldImage = em.merge(oldImage);
            em.remove(oldImage);
            return oldId;
        }

        return 0;
    }

    @Override
    public List<Enterprise> getEnterpriseListByFilters(Category category, String location, EmployeeRanges workers,
                                                       String enterpriseName, String term, int page, int pageSize) {
        if(term != null && !term.isEmpty()){
            term = term.replace("_", "\\_");
            term = term.replace("%", "\\%");
        }

        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT e FROM Enterprise e");

        filterQueryAppendConditions(queryStringBuilder, category, location, workers, enterpriseName, term);

        TypedQuery<Enterprise> query = em.createQuery(queryStringBuilder.toString(), Enterprise.class);
        filterQuerySetParameters(query, category, location, workers, enterpriseName, term);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getEnterpriseCountByFilters(Category category, String location, EmployeeRanges workers, String enterpriseName, String term) {
        if(term != null && !term.isEmpty()){
            term = term.replace("_", "\\_");
            term = term.replace("%", "\\%");
        }

        StringBuilder queryStringBuilder = new StringBuilder().append("SELECT COUNT(DISTINCT e) FROM Enterprise e");

        filterQueryAppendConditions(queryStringBuilder, category, location, workers, enterpriseName, term);

        Query query = em.createQuery(queryStringBuilder.toString());
        filterQuerySetParameters(query, category, location, workers, enterpriseName, term);

        return (Long) query.getSingleResult();
    }

    private void filterQueryAppendConditions(StringBuilder queryStringBuilder, Category category, String location,
                                             EmployeeRanges workers, String enterpriseName, String term) {

        // We use 1=1 to avoid having to check if we need to add a WHERE for each condition
        if(category != null || workers != null || location != null || enterpriseName != null || term != null)
            queryStringBuilder.append(" WHERE 1=1");

        if(category != null)
            queryStringBuilder.append(" AND e.category = :category");
        if(workers != null)
            queryStringBuilder.append(" AND e.workers = :workers");
        if(location != null && !location.isEmpty())
            queryStringBuilder.append(" AND LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%'))");
        if(enterpriseName != null && !enterpriseName.isEmpty())
            queryStringBuilder.append(" AND LOWER(e.name) LIKE LOWER(CONCAT('%', :enterpriseName, '%'))");
        if(term != null && !term.isEmpty()) {
            queryStringBuilder.append(" AND (EXISTS")
                    .append(" LOWER(e.location) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'")
                    .append(" OR LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'")
                    .append(" OR LOWER(e.description) LIKE LOWER(CONCAT('%', :term, '%')) ESCAPE '\\'")
                    .append(")");
        }
    }

    private void filterQuerySetParameters(Query query, Category category, String location,
                                          EmployeeRanges workers, String enterpriseName, String term) {
        if(category != null)
            query.setParameter("category", category);
        if(location != null && !location.isEmpty())
            query.setParameter("location", location);
        if(workers != null)
            query.setParameter("workers", workers.getStringValue());
        if(enterpriseName != null && !enterpriseName.isEmpty())
            query.setParameter("enterpriseName", enterpriseName);
        if(term != null && !term.isEmpty())
            query.setParameter("skillDescription", term);
    }
}
