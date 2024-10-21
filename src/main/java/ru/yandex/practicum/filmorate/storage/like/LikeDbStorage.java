package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.mappers.LikeRowMapper;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private static final String ADD_LIKE_QUERY = "INSERT INTO likes (user_id, film_id) VALUES (?,?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE user_id =? AND film_id =?";
    private static final String GET_LIKE_BY_FILM_ID_QUERY = "SELECT * FROM likes WHERE film_id =?";
    private static final String UPDATE_FILM_RATING = "UPDATE films SET rating = rating + 1 WHERE id =?";
    private static final String UPDATE_SET_FILM_RATING = "UPDATE films SET rating = rating - 1 WHERE id =?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long filmId, Long userId) {
        log.info("Adding like to filmId {} by userId {}", filmId, userId);
        try {
            jdbcTemplate.update(ADD_LIKE_QUERY, userId, filmId);
            jdbcTemplate.update(UPDATE_FILM_RATING, filmId);
            log.info("Added like for film {} by the user {}", filmId, userId);
        } catch (Exception e) {
            log.error("Error occurred while adding like for user {} and film {}: {}", userId, filmId, e.getMessage());
        }

    }

    @Override
    public boolean deleteLike(Long userId, Long filmId) {
        log.info("Removing like for user {} and film {}", userId, filmId);
        int rowsAffected = jdbcTemplate.update(DELETE_LIKE_QUERY, userId, filmId);
        if (rowsAffected > 0) {
            jdbcTemplate.update(UPDATE_SET_FILM_RATING, filmId);
            log.info("Removed like for user {} from film {}", userId, filmId);
            return true;
        } else {
            log.warn("No like found for user {} and film {}", userId, filmId);
            return false;
        }
    }

    @Override
    public Collection<Like> getLikesFilmId(Long filmId) {
        log.info("Getting likes for film {}", filmId);
        Collection<Like> likes = jdbcTemplate.query(GET_LIKE_BY_FILM_ID_QUERY, new LikeRowMapper(), filmId);
        log.debug("Found {} likes for film {}", likes.size(), filmId);
        return likes;
    }

}
