package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Category;
import ar.edu.itba.paw.models.utils.PaginatedResource;
import java.util.Optional;

public interface CategoryService {

    Category create (String name);

    //TODO: Agregar boolean de 'exactMatch' y metodo para buscar aproximado
    Optional<Category> findByName(String name);

    Optional<Category> findById(long id);

    PaginatedResource<Category> getAllCategories(String nameLike, int page, int pageSize);

    long getCategoryCount();
}
