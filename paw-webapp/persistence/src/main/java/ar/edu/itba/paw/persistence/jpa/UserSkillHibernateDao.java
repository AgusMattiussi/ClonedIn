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
import java.math.BigDecimal;
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

    @Override
    public boolean alreadyExists(Skill skill, User user) {
        Query query = em.createQuery("SELECT COUNT(us) FROM UserSkill AS us WHERE us.skill = :skill AND us.user = :user");
        query.setParameter("skill", skill);
        query.setParameter("user",user);

        return ((BigDecimal) query.getSingleResult()).longValue() > 0;
    }


    @Override
    public List<User> getUsersWithSkill(Skill skill) {
        TypedQuery<User> query = em.createQuery("SELECT us.user FROM UserSkill AS us WHERE us.skill = :skill", User.class);
        query.setParameter("skill", skill);

        return query.getResultList();
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
