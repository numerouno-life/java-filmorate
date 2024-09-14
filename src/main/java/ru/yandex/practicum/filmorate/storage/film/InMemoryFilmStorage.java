package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        if (films.containsKey(film.getId())) {
            log.error("Film with id {} already exist", film.getId());
            throw new DuplicatedDataException("Film with id " + film.getId() + " already exist");
        }
        if (film.getId() == 0) {
            log.info("Making the film");
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Movie added {}", film);
            return film;
        } else {
            log.error("Unknown error while creating movie");
            throw new RuntimeException("Unknown error while creating movie");
        }
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
        if (film.getId() == null || !films.containsKey(film.getId())) {
            String msg = "Movie with ID:" + film.getId() + " not found.";
            log.error("Movie with ID: {} not found.", film.getId());
            throw new NotFoundException(msg);
        }
        films.put(film.getId(), film);
        log.info("Film with ID: {} was update", film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Returned all films");
        return films.values();
    }

    @Override
    public Film getFilmById(Long id) {
        log.info("Get film by id {}", id);
        if (!films.containsKey(id)) {
            String msg = "Movie with ID:" + id + " not found.";
            log.error("Movie with ID:{} not found.", id);
            throw new NotFoundException(msg);
        }
        return films.get(id);
    }

    private long getNextId() {
        return currentId++;
    }


}
