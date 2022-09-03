package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.interfaces.services.CategoryService;
import ar.edu.itba.paw.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
        return categoryDao.findByName(name);
    }

    @Override
    public Optional<Category> findById(long id) {
        return categoryDao.findById(id);
    }
}
