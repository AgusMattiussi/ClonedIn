package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
        final TypedQuery<Category> query = em.createQuery("SELECT c FROM Category AS c WHERE c.name = :name", Category.class);
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
    public List<Category> getAllCategories(int page, int pageSize) {
        TypedQuery<Category> query = em.createQuery("SELECT c FROM Category c WHERE c.id <> 1", Category.class);

        query.setFirstResult(page * pageSize).setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long getCategoryCount() {
        Query query = em.createQuery("SELECT COUNT(c) FROM Category c WHERE c.id <> 1");

        return (Long) query.getSingleResult();
    }
}
