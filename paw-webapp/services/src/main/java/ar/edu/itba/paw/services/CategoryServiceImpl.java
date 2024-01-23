package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Category create(String name) {
        return categoryDao.create(name);
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
    public PaginatedResource<Category> getAllCategories(int page, int pageSize) {
        List<Category> categoryList = categoryDao.getAllCategories(page-1, pageSize);
        long categoryCount = this.getCategoryCount();
        long maxPages = categoryCount / pageSize + 1;

        return new PaginatedResource<>(categoryList, page, maxPages);
    }

    @Override
    public long getCategoryCount() {
        return categoryDao.getCategoryCount();
    }
}
