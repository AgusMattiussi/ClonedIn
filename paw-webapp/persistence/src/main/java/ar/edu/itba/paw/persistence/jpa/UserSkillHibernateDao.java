package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.SkillDao;
import ar.edu.itba.paw.interfaces.persistence.UserSkillDao;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.Skill;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserSkill;
import ar.edu.itba.paw.models.exceptions.SkillNotFoundException;
import ar.edu.itba.paw.models.exceptions.UserIsNotProfileOwnerException;
import ar.edu.itba.paw.models.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
@Transactional
public class UserSkillHibernateDao implements UserSkillDao {

    @PersistenceContext
    private EntityManager em;

    //TODO: Cambiar para seguir filosofia JPA

    @Autowired
    private SkillDao skillDao;
    @Autowired
    private UserService userService;

    @Override
    public void addSkillToUser(Skill skill, User user) {
        UserSkill userSkill = new UserSkill(user, skill);
        em.persist(userSkill);
    }


    //FIXME: Chequear si funciona
    @Override
    public boolean alreadyExists(String skillDescription, long userID) {
        Optional<Skill> skill = skillDao.findByDescription(skillDescription);
        if(!skill.isPresent())
            return false;

        User user = userService.findById(userID).orElseThrow(UserNotFoundException::new);

        TypedQuery<Long> query = em.createQuery("SELECT COUNT(us) FROM UserSkill AS us WHERE us.skill = :skill AND us.user = :user", Long.class);
        query.setParameter("skill", skill.get());
        query.setParameter("user",user);

        return query.getSingleResult() > 0;
    }

    @Override
    public boolean alreadyExists(long skillID, long userID) {
        Optional<Skill> skill = skillDao.findById(skillID);
        if(!skill.isPresent())
            return false;
        User user = userService.findById(userID).orElseThrow(UserNotFoundException::new);

        TypedQuery<Long> query = em.createQuery("SELECT COUNT(us) FROM UserSkill AS us WHERE us.skill = :skill AND us.user = :user", Long.class);
        query.setParameter("skill", skill.get());
        query.setParameter("user",user);

        return query.getSingleResult() > 0;
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
        Skill skill = skillDao.findByDescription(skillDescription).orElseThrow(SkillNotFoundException::new);

        TypedQuery<User> query = em.createQuery("SELECT us.user FROM UserSkill AS us WHERE us.skill = :skill", User.class);
        query.setParameter("skill", skill);

        return query.getResultList();
    }

    @Override
    public List<User> getUsersWithSkill(long skillID) {
        Skill skill = skillDao.findById(skillID).orElseThrow(SkillNotFoundException::new);

        TypedQuery<User> query = em.createQuery("SELECT us.user FROM UserSkill AS us WHERE us.skill = :skill", User.class);
        query.setParameter("skill", skill);

        return query.getResultList();
    }

    @Override
    public List<Skill> getSkillsForUser(long userID) {
        User user = userService.findById(userID).orElseThrow(UserNotFoundException::new);

        TypedQuery<Skill> query = em.createQuery("SELECT us.skill FROM UserSkill AS us WHERE us.user = :user", Skill.class);
        query.setParameter("user", user);

        return query.getResultList();
    }

    @Override
    public void deleteSkillFromUser(long userID, long skillID) {
        User user = userService.findById(userID).orElseThrow(UserNotFoundException::new);
        Skill skill = skillDao.findById(skillID).orElseThrow(SkillNotFoundException::new);

        Query query = em.createQuery("DELETE FROM UserSkill WHERE user = :user AND skill = :skill");
        query.setParameter("user", user);
        query.setParameter("skill", skill);
        query.executeUpdate();
    }
}
