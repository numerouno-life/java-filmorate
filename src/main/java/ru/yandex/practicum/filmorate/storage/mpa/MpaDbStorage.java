package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_SELECT_ALL_MPA = "SELECT * FROM mpa";

    @Override
    public Mpa getMpaById(long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_ALL_MPA.concat(" WHERE id =?"), new MpaRowMapper(), id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<Mpa> getAllMPAs() {
        final String sql =  SQL_SELECT_ALL_MPA.concat(" ORDER BY id ASC");
        return jdbcTemplate.query(sql, new MpaRowMapper());
    }
}
