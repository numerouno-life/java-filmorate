package ru.yandex.practicum.filmorate.storage.filmgenre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {
    private static final String ADD_FILM_GENRE_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String REMOVE_FILM_GENRE_QUERY = "DELETE FROM film_genres WHERE film_id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilmGenre(long filmId, long genreId) {
        log.info("Adding genre {} to film {}", genreId, filmId);
        try {
            jdbcTemplate.update(ADD_FILM_GENRE_QUERY, filmId, genreId);
        } catch (DuplicateKeyException e) {
            log.warn("Genre is already added to film {}", genreId);
        } catch (DataAccessException e) {
            throw new DataException("Error adding genre to film");
        }
        log.info("Genre added to film {}", genreId);
    }

    @Override
    public Collection<Genre> getAllFilmGenresByFilmId(Long filmId) {
        log.info("Getting all genres for film {}", filmId);
        final String sql = "SELECT g.id as id, g.name as name from film_genres fg " +
                "LEFT JOIN genres g on fg.genre_id = g.id WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, new GenreRowMapper(), filmId);
    }

    @Override
    public void deleteAllFilmGenresByFilmId(Long filmId) {
        log.info("Removing genre {}", filmId);
        try {
            jdbcTemplate.update(REMOVE_FILM_GENRE_QUERY, filmId);
        } catch (DuplicateKeyException e) {
            log.error("Genre is already removed from film {}", filmId);
        }
    }

    @Override
    public Map<Long, Collection<Genre>> getAllFilmGenres(Collection<Film> films) {
        final String sql = "select fg.film_id as film_id, g.id as genre_id, g.name as name " +
                "from film_genres fg " +
                "left join genres g on fg.genre_id = g.id " +
                "where fg.film_id in (%s)";

        Map<Long, Collection<Genre>> filmGenresMap = new HashMap<>();
        Collection<String> ids = films.stream().map(film -> String.valueOf(film.getId())).collect(Collectors.toList());

        jdbcTemplate.query(String.format(sql, String.join(",", ids)), rs -> {
            Genre genre = new Genre(rs.getLong("genre_id"), rs.getString("name"));

            Long filmId = rs.getLong("film_id");

            filmGenresMap.putIfAbsent(filmId, new ArrayList<>());
            filmGenresMap.get(filmId).add(genre);
        });

        return filmGenresMap;
    }

}
