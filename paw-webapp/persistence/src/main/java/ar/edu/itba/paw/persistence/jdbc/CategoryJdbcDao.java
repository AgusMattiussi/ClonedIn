package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.persistence.CategoryDao;
import ar.edu.itba.paw.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class CategoryJdbcDao implements CategoryDao {

    private static final String CATEGORY_TABLE = "rubro";
    private static final String ID = "id";
    private static final String NAME = "nombre";

    private static final RowMapper<Category> CATEGORY_MAPPER = ((resultSet, rowNum) ->
            new Category(resultSet.getLong(ID),
                        resultSet.getString(NAME)));

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    @Autowired
    public CategoryJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName(CATEGORY_TABLE)
                .usingGeneratedKeyColumns(ID);
    }


    @Override
    public Category create(String name) {
        Optional<Category> existing = findByName(name);
        if(existing.isPresent())
            return existing.get();

        final Map<String, Object> values = new HashMap<>();
        values.put(NAME, name);

        Number categoryId = insert.executeAndReturnKey(values);

        return new Category(categoryId.longValue(), name);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return template.query("SELECT * FROM rubro WHERE nombre = ?",
                new Object[]{ name }, CATEGORY_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Category> findById(long id) {
        return template.query("SELECT * FROM rubro WHERE id = ?",
                new Object[]{ id }, CATEGORY_MAPPER).stream().findFirst();
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> allCategories = template.query("SELECT * FROM rubro", CATEGORY_MAPPER);
        return allCategories;
    }
}
