package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Primary
@Repository
@Transactional
public class UserSkillHibernateDao implements UserSkillDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addSkillToUser(Skill skill, User user) {
        UserSkill userSkill = new UserSkill(user, skill);
        em.persist(userSkill);
    }


    //FIXME: Chequear si funciona
    @Override
    public boolean alreadyExists(String skillDescription, long userID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM aptitudUsuario au JOIN aptitud a ON au.idAptitud = a.id " +
                "WHERE a.descripcion = :skillDescription AND au.idUsuario = :userID", Long.class);
        query.setParameter("skillDescription", skillDescription);
        query.setParameter("userID",userID);

        return ((Long) query.getSingleResult()) > 0;
    }

    @Override
    public boolean alreadyExists(long skillID, long userID) {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM aptitudUsuario WHERE idAptitud = :skillID AND idUsuario = :userID", Long.class);
        query.setParameter("skillID", skillID);
        query.setParameter("userID",userID);

        return ((Long) query.getSingleResult()) > 0;
    }

    @Override
    public boolean alreadyExists(Skill skill, User user) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(us) FROM UserSkill AS us WHERE us.skill = :skill AND us.user = :user", Long.class);
        query.setParameter("skill", skill);
        query.setParameter("user",user);

        return query.getSingleResult() > 0;
    }

    @Override
    public List<User> getUsersWithSkill(String skillDescription) {
        Query query = em.createNativeQuery("SELECT * FROM usuario u JOIN aptitudUsuario au ON u.id = au.idUsuario JOIN aptitud a ON a.id = au.idAptitud " +
                "WHERE a.descripcion = :skillDescription", User.class);
        query.setParameter("skillDescription", skillDescription);

        return (List<User>) query.getResultList();
    }

    @Override
    public List<User> getUsersWithSkill(long skillID) {
        Query query = em.createNativeQuery("SELECT * FROM usuario u JOIN aptitudUsuario au ON u.id = au.idUsuario WHERE au.idAptitud = :skillID", User.class);
        query.setParameter("skillID", skillID);

        return (List<User>) query.getResultList();
    }

    @Override
    public List<User> getUsersWithSkill(Skill skill) {
        TypedQuery<User> query = em.createQuery("SELECT us.user FROM UserSkill AS us WHERE us.skill = :skill", User.class);
        query.setParameter("skill", skill);

        return query.getResultList();
    }

    @Override
    public List<Skill> getSkillsForUser(long userID) {
        Query query = em.createNativeQuery("SELECT a.id, a.descripcion FROM usuario u JOIN aptitudUsuario au ON u.id = au.idUsuario " +
                        "JOIN aptitud a ON a.id = au.idAptitud WHERE au.idUsuario = ?", Skill.class);

        return (List<Skill>) query.getResultList();
    }

    @Override
    public List<Skill> getSkillsForUser(User user) {
        TypedQuery<Skill> query = em.createQuery("SELECT us.skill FROM UserSkill AS us WHERE us.user = :user", Skill.class);
        query.setParameter("user", user);

        return query.getResultList();
    }


    @Override
    public void deleteSkillFromUser(long userID, long skillID) {
        Query query = em.createNativeQuery("DELETE FROM aptitudUsuario WHERE idUsuario = :userID AND idAptitud = :skillID");
        query.setParameter("userID", userID);
        query.setParameter("skillID", skillID);
        query.executeUpdate();
    }

    @Override
    public void deleteSkillFromUser(User user, Skill skill) {
        Query query = em.createQuery("DELETE FROM UserSkill WHERE user = :user AND skill = :skill");
        query.setParameter("user", user);
        query.setParameter("skill", skill);
        query.executeUpdate();
    }


}
