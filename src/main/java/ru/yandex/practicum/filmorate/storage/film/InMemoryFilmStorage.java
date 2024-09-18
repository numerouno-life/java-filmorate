package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 1;

    @Override
    public Film addFilm(Film film) {
        if (film.getId() != null && films.containsKey(film.getId())) {
            throw new DuplicatedDataException("Film with ID " + film.getId() + " already exists");
        }

        if (film.getId() == null) {
            film.setId(getNextId());
        }

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public String removeFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.trace("remove film with id: {}", film.getId());
            films.remove(film.getId());
            return "Film with id: " + film.getId() + " was removed";
        }
        throw new NotFoundException("Film with id: " + film.getId() + " not found");
    }

    @Override
    public Film update(Film film) {
        log.info("Update film");
        if (film.getId() == 0 || film.getId() < 0) {
            log.error("ID cannot be less than 0");
            throw new ConditionsNotMetException("ID cannot be less than 0");
        }
        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setDuration(film.getDuration());
            oldFilm.setName(film.getName());
            oldFilm.setReleaseDate(film.getReleaseDate());
            oldFilm.setLikes(film.getLikes());
            return oldFilm;
        }
        throw new NotFoundException("Film with ID:" + film.getId() + " not found");
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Returned all films");
        return films.values();
    }

    @Override
    public Film getFilmById(long id) {
        log.info("Get film by id {}", id);
        if (!films.containsKey(id)) {
            String msg = "Movie with ID:" + id + " not found.";
            log.error("Movie with ID:{} not found.", id);
            throw new NotFoundException(msg);
        }
        return films.get(id);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}