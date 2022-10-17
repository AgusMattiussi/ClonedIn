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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Primary
@Repository
public class EnterpriseHibernateDao implements EnterpriseDao {

    public static final Image DEFAULT_IMAGE = null;

    private CategoryDao categoryDao;
    private final ImageDao imageDao;
    @PersistenceContext
    private EntityManager em;

    @Autowired
    public EnterpriseHibernateDao(CategoryDao categoryDao, ImageDao imageDao) {
        this.categoryDao = categoryDao;
        this.imageDao = imageDao;
    }

    @Override
    public Enterprise create(String email, String name, String password, String location, String categoryName, String description) {
        Category category = categoryDao.findByName(categoryName).orElseThrow(CategoryNotFoundException::new);

        final Enterprise enterprise = new Enterprise(email, name, password, location, category, description, DEFAULT_IMAGE);
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
//        Query query = em.createNativeQuery("SELECT COUNT(*) FROM empresa WHERE email = :email", Boolean.class);
//        query.setParameter("email", email);
//        return (boolean) query.getSingleResult();
        return findByEmail(email).isPresent();
    }

    @Override
    public void updateName(long enterpriseID, String newName) {
        Query query = em.createQuery("UPDATE Enterprise SET name = :newName WHERE id = :enterpriseID");
        query.setParameter("newName", newName);
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

    //FIXME: Cambiar a Category newCategory?
    @Override
    public void updateCategory(long enterpriseID, String newCategoryName) {
        Category category = categoryDao.findByName(newCategoryName).orElseThrow(CategoryNotFoundException::new);

        Query query = em.createQuery("UPDATE Enterprise SET category = :newCategory WHERE id = :enterpriseID");
        query.setParameter("newCategory", category);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }

    // FIXME: Image o imageID?
    @Override
    public void updateEnterpriseProfileImage(long enterpriseID, long imageId) {
        Image image = imageDao.getImage(imageId).orElseThrow(ImageNotFoundException::new);

        Query query = em.createQuery("UPDATE Enterprise SET image = :image WHERE id = :enterpriseID");
        query.setParameter("image", image);
        query.setParameter("enterpriseID", enterpriseID);
        query.executeUpdate();
    }
}
