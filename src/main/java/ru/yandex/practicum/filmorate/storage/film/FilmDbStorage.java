package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.DataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmgenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String FILMS_MPA_SQL =
            "SELECT f.*, m.id AS mpa_id, m.name AS mpa_name " +
                    "FROM films f " +
                    "LEFT JOIN mpa m ON f.mpa_id = m.id";

    private final JdbcTemplate jdbcTemplate;
    private final FilmGenreStorage filmGenreStorage;
    private final LikeDbStorage likeDbStorage;
    private final MpaStorage mpaStorage;

    @Override
    public Film addFilm(Film film) {
        final String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id, rating) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        log.info("Adding film {}", film);
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setObject(3, film.getReleaseDate());
                ps.setLong(4, film.getDuration());
                ps.setLong(5, film.getMpa().getId());
                ps.setLong(6, film.getRating());
                return ps;
            }, generatedKeyHolder);
        } catch (DataAccessException e) {
            log.error("Error adding film {}: {}", film, e.getMessage());
            throw new DataException("Error adding film");
        }
        long filmId = Objects.requireNonNull(generatedKeyHolder.getKey()).longValue();
        film.setId(filmId);
        log.info("Added film with id {}", filmId);
        return addFields(film);
    }

    @Override
    public String removeFilm(Film film) {
        log.info("Removing film {}", film);
        try {
            jdbcTemplate.update("DELETE FROM films WHERE id =?", film.getId());
            jdbcTemplate.update("DELETE FROM film_genres WHERE film_id =?", film.getId());
            jdbcTemplate.update("DELETE FROM likes WHERE film_id =?", film.getId());
            log.info("Removed film {}", film);
        } catch (DataAccessException e) {
            log.error("Error removing film {}: {}", film, e.getMessage());
            throw new DataException("Error removing film");
        }
        return "Film with ID:" + film.getId() + " removed";
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Updating film {}", film);
        final String sql = "UPDATE films " +
                "SET name =?, release_date =?, description =?, duration =?, mpa_id =?, rating =? WHERE id =?";
        filmGenreStorage.deleteAllFilmGenresByFilmId(film.getId());
        int result = jdbcTemplate.update(sql,
                film.getName(),
                film.getReleaseDate(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRating(),
                film.getId()
        );

        if (result == 0) {
            log.warn("Film with ID {} not found for update", film.getId());
            throw new NotFoundException("Film with ID " + film.getId() + " not found for update");
        }

        return addFields(film);
    }

    @Override
    public Film getFilmById(long id) {
        log.info("Fetching film by id {}", id);
        List<Film> films = jdbcTemplate.query(FILMS_MPA_SQL.concat(" WHERE f.id = ?"), new FilmRowMapper(), id);
        if (!films.isEmpty()) {
            Collection<Genre> filmGenres = filmGenreStorage.getAllFilmGenresByFilmId(id);
            return films.get(0).toBuilder().genres(filmGenres).build();
        }
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Fetching all films");
        Collection<Film> films = jdbcTemplate.query(FILMS_MPA_SQL, new FilmRowMapper());
        return setFilmGenres(films);
    }

    private Film addFields(Film film) {
        long filmId = film.getId();
        long mpaId = film.getMpa().getId();
        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> filmGenreStorage.addFilmGenre(filmId, genre.getId()));
        }
        Collection<Genre> filmGenres = filmGenreStorage.getAllFilmGenresByFilmId(film.getId());
        Mpa filmMpa = mpaStorage.getMpaById(mpaId);
        Collection<Like> filmLikes = likeDbStorage.getLikesFilmId(filmId);
        if (filmLikes == null) {
            filmLikes = new ArrayList<>();
        }
        return film.toBuilder().mpa(filmMpa).genres(filmGenres).likes(filmLikes).build();
    }

    private Collection<Film> setFilmGenres(Collection<Film> films) {
        Map<Long, Collection<Genre>> filmGenresMap = filmGenreStorage.getAllFilmGenres(films);
        films.forEach(film -> {
            Long filmId = film.getId();
            film.setGenres(filmGenresMap.getOrDefault(filmId, new ArrayList<>()));
            Collection<Like> filmLikes = likeDbStorage.getLikesFilmId(filmId);
            if (filmLikes == null) {
                filmLikes = new ArrayList<>();
            }
            film.setLikes(filmLikes);
        });

        return films;
    }
}