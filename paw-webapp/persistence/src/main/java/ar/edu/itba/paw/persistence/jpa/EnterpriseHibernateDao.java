package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.exceptions.CategoryNotFoundException;
import ar.edu.itba.paw.models.exceptions.ImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Optional;

@Primary
@Repository
@Transactional
public class EnterpriseHibernateDao implements EnterpriseDao {

    public static final Image DEFAULT_IMAGE = null;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Enterprise create(String email, String name, String password, String location, Category category, String workers, Integer year, String link, String description) {
        final Enterprise enterprise = new Enterprise(name, email, password, location, category, workers, year, link, description, DEFAULT_IMAGE);
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
    public Optional<Enterprise> findById(long enterpriseId) {
        return Optional.ofNullable(em.find(Enterprise.class, enterpriseId));
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
    public void updateName(long enterpriseID, String newName) {
        Query query = em.createQuery("UPDATE Enterprise SET name = :newName WHERE id = :enterpriseID");
        query.setParameter("newName", newName);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
    public void updateWorkers(long enterpriseID, String newWorkers) {
        Query query = em.createQuery("UPDATE Enterprise SET workers = :newWorkers WHERE id = :enterpriseID");
        query.setParameter("newWorkers", newWorkers);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
    public void updateYear(long enterpriseID, Integer newYear) {
        Query query = em.createQuery("UPDATE Enterprise SET year = :newYear WHERE id = :enterpriseID");
        query.setParameter("newYear", newYear);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
    public void updateLink(long enterpriseID, String newLink) {
        Query query = em.createQuery("UPDATE Enterprise SET link = :newLink WHERE id = :enterpriseID");
        query.setParameter("newLink", newLink);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
    public void updateDescription(long enterpriseID, String newDescription) {
        Query query = em.createQuery("UPDATE Enterprise SET description = :newDescription WHERE id = :enterpriseID");
        query.setParameter("newDescription", newDescription);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
    public void updateLocation(long enterpriseID, String newLocation) {
        Query query = em.createQuery("UPDATE Enterprise SET location = :newLocation WHERE id = :enterpriseID");
        query.setParameter("newLocation", newLocation);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
    public void updateCategory(long enterpriseID, Category newCategory) {
        Query query = em.createQuery("UPDATE Enterprise SET category = :newCategory WHERE id = :enterpriseID");
        query.setParameter("newCategory", newCategory);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    @Override
    public void updateEnterpriseProfileImage(long enterpriseID, Image image) {
        Query query = em.createQuery("UPDATE Enterprise SET image = :image WHERE id = :enterpriseID");
        query.setParameter("image", image);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }
}
