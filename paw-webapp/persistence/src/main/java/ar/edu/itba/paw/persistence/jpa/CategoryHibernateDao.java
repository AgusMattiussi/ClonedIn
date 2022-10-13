package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
@Primary
@Repository
public class CategoryHibernateDao implements CategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Category create(String name) {
        final Category category = new Category(name);
        em.persist(category);
        return category;
    }

    @Override
    public Optional<Category> findByName(String name) {
        final TypedQuery<Category> query = em.createQuery("from Category as c where c.name = :name", Category.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Category> findById(long id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    @Override
    public List<Category> getAllCategories() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }
}
