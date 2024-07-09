package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
//@CacheConfig(cacheNames = "categories-cache")
public class CategoryHibernateDao implements CategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    //@CacheEvict(allEntries = true)
    public Category create(String name) {
        final Category category = new Category(name);
        em.persist(category);
        return category;
    }

    @Override
    //@Cacheable(key = "#name", unless = "#result == null")
    public Optional<Category> findByName(String name) {
        final TypedQuery<Category> query = em.createQuery("SELECT c FROM Category AS c WHERE LOWER(c.name) = LOWER(:name)", Category.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }

    @Override
    //@Cacheable(key = "#id", unless = "#result == null")
    public Optional<Category> findById(long id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }


    @Override
    //@Cacheable(key = "#page + '-' + #pageSize")
    public List<Category> getAllCategories(String nameLike, int page, int pageSize) {
        StringBuilder queryBuilder = new StringBuilder("SELECT c FROM Category c WHERE c.id <> 1");

        if(nameLike != null && !nameLike.isEmpty()) {
            queryBuilder.append(" AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\'");
        }

        TypedQuery<Category> query = em.createQuery(queryBuilder.toString(), Category.class);

        if(nameLike != null && !nameLike.isEmpty()) {
            query.setParameter("name", nameLike);
        }

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getCategoryCount() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Category c WHERE c.id <> 1");

        return (Long) query.getSingleResult();
    }
}
