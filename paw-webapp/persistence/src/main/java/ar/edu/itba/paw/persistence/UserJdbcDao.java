package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {

    private static final RowMapper<User> USER_MAPPER = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("email"),
                    rs.getString("password"));

    private final JdbcTemplate template;
    private final SimpleJdbcInsert insert;

    @Autowired
    public UserJdbcDao(final DataSource ds){
        this.template = new JdbcTemplate(ds);
        this.insert = new SimpleJdbcInsert(ds)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User create(final String email, final String password) {
        final Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("password", password);
        Number userId = insert.executeAndReturnKey(values);

        return new User(userId.longValue(), email, password);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return template.query("SELECT * FROM users WHERE email = ?",
                new Object[]{ email }, USER_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findById(final long userId) {
        return template.query("SELECT * FROM users WHERE id = ?",
                new Object[]{ userId }, USER_MAPPER).stream().findFirst();
    }
}
