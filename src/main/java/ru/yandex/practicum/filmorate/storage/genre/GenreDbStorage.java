package ru.yandex.practicum.filmorate.storage.genre;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String genre = "select * from genres";

    @Override
    public Genre getGenreById(long genreId) {
        try {
            return jdbcTemplate.queryForObject(genre.concat(" where id = ?"), new GenreRowMapper(), genreId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query(genre.concat(" order by id asc"), new GenreRowMapper());
    }
}
