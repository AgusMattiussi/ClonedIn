package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.interfaces.persistence.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ImageJdbcDao implements ImageDao {
    private static final String IMAGE_TABLE = "imagen";

    private static final String ID = "id";

    private static final String BYTES = "bytes";

    private static final RowMapper<Image> IMAGE_ROW_MAPPER = ((resultSet, rowNum) ->
            new Image(resultSet.getLong(ID),
                    resultSet.getBytes(BYTES)));

    private final JdbcTemplate template;

    private final SimpleJdbcInsert imageInsert;

    @Autowired
    public ImageJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.imageInsert = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE)
                .usingGeneratedKeyColumns(ID);
    }


    @Override
    public Optional<Image> getImage(long id) {
        return template.query("SELECT * FROM imagen WHERE id = ?",
                new Object[]{ id }, IMAGE_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Image uploadImage(byte[] bytes) {
        final Map<String, Object> values = new HashMap<>();
        values.put(BYTES, bytes);

        Number imageId = imageInsert.executeAndReturnKey(values);

        return new Image(imageId.longValue(), bytes);
    }
}
