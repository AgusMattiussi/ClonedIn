package ar.edu.itba.paw.persistence.jpa;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
@Primary
@Repository
@CacheConfig(cacheNames = "images-cache")
public class ImageHibernateDao implements ImageDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(key = "#id", unless = "#result == null")
    public Optional<Image> getImage(long id) {
        return Optional.of(em.find(Image.class, id));
    }

    @Override
    @Cacheable(key = "#result.id")
    public Image uploadImage(byte[] bytes) {
        Image image = new Image(bytes);
        em.persist(image);
        em.flush();
        return image;
    }
}
