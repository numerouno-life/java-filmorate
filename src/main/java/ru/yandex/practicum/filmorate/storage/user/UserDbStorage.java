package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private static final String USER_SQL = "SELECT * FROM users";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> rowMapper;

    @Override
    public User createUser(User user) {
        final String sql = "INSERT INTO users (name, login, birthday, email) VALUES (?, ?, ?, ?)";
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql,
                    new String[]{"id"}
            );
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setDate(3, Date.valueOf(user.getBirthday()));
            preparedStatement.setString(4, user.getEmail());
            return preparedStatement;
        }, generatedKeyHolder);

        long userId = Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();
        user.setId(userId);
        log.info("User created: {}", user);
        return user;
    }

    @Override
    public String removeUser(User user) {
        final String sql = "Delete from users where id = ?";
        int result = jdbcTemplate.update(sql, user.getId());
        if (result > 0) {
            return "User deleted";
        } else {
            return "User not found";
        }
    }

    @Override
    public User updateUser(User user) {
        final String sql = "UPDATE users SET name = ?, login = ?, birthday = ?, email = ? WHERE id = ?";

        try {
            if (jdbcTemplate.update(
                    sql,
                    user.getName(), user.getLogin(), Date.valueOf(user.getBirthday()), user.getEmail(), user.getId()
            ) > 0) {
                log.info("User updated: {}", user);
                return user;
            } else {
                throw new NotFoundException("User not found with ID: " + user.getId());
            }
        } catch (DataAccessException e) {
            throw new DataException("Error updating user");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return jdbcTemplate.query(USER_SQL, rowMapper);
    }

    @Override
    public User getUserById(Long id) {
        try {
            return jdbcTemplate.queryForObject(USER_SQL.concat(" WHERE id = ?"), rowMapper, id);
        } catch (Exception e) {
            throw new NotFoundException("User not found with ID: " + id);
        }
    }

    @Override
    public boolean deleteUserById(Long id) {
        final String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public Collection<User> getUserFriends(Long userId) {
        getUserById(userId);
        final String sql = "SELECT * FROM users WHERE id IN " +
                "(SELECT friend_id FROM friendships WHERE user_id = ?)";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    @Override
    public Collection<User> getMutualFriends(User userFrom, User userTo) {
        final String sql = "SELECT * FROM users WHERE id IN " +
                "(SELECT friend_id FROM friendships WHERE user_id = ?) AND id IN " +
                "(SELECT friend_id FROM friendships WHERE user_id = ?)";
        return jdbcTemplate.query(sql, rowMapper, userFrom.getId(), userTo.getId());
    }
}