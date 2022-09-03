package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.Category;

import java.util.Optional;

public interface CategoryDao {

    Category create (String name);

    Optional<Category> findByName(String name);

    Optional<Category> findById(long id);
}
