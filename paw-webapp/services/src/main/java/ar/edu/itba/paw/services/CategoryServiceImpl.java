package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Category create(String name) {
        Category c = categoryDao.create(name);
        LOGGER.info("Category created: {}", c.getName());
        LOGGER.debug("Category created: {}", c);
        return c;
    }

    @Override
    public Optional<Category> findByName(String name) {
        if(name == null || name.isEmpty())
            return Optional.empty();
        return categoryDao.findByName(name);
    }

    @Override
    public Optional<Category> findById(long id) {
        return categoryDao.findById(id);
    }

    @Override
    public PaginatedResource<Category> getAllCategories(String nameLike, int page, int pageSize) {
        List<Category> categoryList = categoryDao.getAllCategories(nameLike, page-1, pageSize);
        long categoryCount = this.getCategoryCount();
        long maxPages = (long) Math.ceil((double) categoryCount / pageSize);

        return new PaginatedResource<>(categoryList, page, maxPages);
    }

    @Override
    public long getCategoryCount() {
        return categoryDao.getCategoryCount();
    }
}
