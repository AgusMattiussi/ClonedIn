package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.EnterpriseDao;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.Enterprise;
import ar.edu.itba.paw.models.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Primary
@Repository
public class EnterpriseHibernateDao implements EnterpriseDao {
    //FIXME: Cambiar esto
    public static final Category DEFAULT_CATEGORY = new Category(0L, "testcategory");

    @PersistenceContext
    private EntityManager em;

    @Override
    public Enterprise create(String email, String name, String password, String location, String categoryName, String description) {
        final Enterprise enterprise = new Enterprise(email, name, password, location, DEFAULT_CATEGORY, description, null);
        em.persist(enterprise);
        return enterprise;
    }

    @Override
    public Optional<Enterprise> findByEmail(String email) {
        final TypedQuery<Enterprise> query = em.createQuery("from Enterprise as e where e.email = :email", Enterprise.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Enterprise> findById(long enterpriseId) {
        return Optional.ofNullable(em.find(Enterprise.class, enterpriseId));
    }

    @Override
    public void changePassword(String email, String password) {

    }

    @Override
    public boolean enterpriseExists(String email) {
        return false;
    }

    @Override
    public void updateName(long enterpriseID, String newName) {

    }

    @Override
    public void updateDescription(long enterpriseID, String newDescription) {

    }

    @Override
    public void updateLocation(long enterpriseID, String newLocation) {

    }

    @Override
    public void updateCategory(long enterpriseID, String newCategoryName) {

    }

    @Override
    public void updateEnterpriseProfileImage(long enterpriseID, long imageId) {

    }
}
