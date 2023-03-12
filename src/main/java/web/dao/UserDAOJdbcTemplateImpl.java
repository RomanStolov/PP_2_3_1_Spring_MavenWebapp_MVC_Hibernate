package web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import web.model.User;

import java.util.List;

@Repository
public class UserDAOJdbcTemplateImpl implements UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAOJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users",
                new BeanPropertyRowMapper<>(User.class));
    }

    // Этот метод не доделал !!!
    @Override
    public void saveUser(User user) {
        jdbcTemplate.update(
                "INSERT INTO users VALUES(?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getSurname(), user.getAge(), user.getEmail());
    }

    @Override
    public User getUser(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id=?",
                        new Object[]{id},
                        new BeanPropertyRowMapper<>(User.class))
                .stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public void updateUser(Long id, User user) {
        jdbcTemplate.update(
                "UPDATE users SET name=?, surname=?, age=?, email=? WHERE id=?",
                user.getName(), user.getSurname(), user.getAge(), user.getEmail(), user.getId());
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
    }


}
