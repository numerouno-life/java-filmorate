package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 1;

    //добавление фильма
    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.debug("POST /films with {}", film);
        validate(film);

        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Film added successful {}", film);
        return film;
    }

    //обновление фильма
    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.debug("PUT /films with {}", newFilm);
        if (newFilm.getId() == null || !films.containsKey(newFilm.getId())) {
            String msg = "No movie with this ID was found.";
            log.error(msg);
            throw new ValidationException(msg);
        }

        validate(newFilm);
        films.put(newFilm.getId(), newFilm);

        log.info("Film update successful {}", newFilm);
        return newFilm;
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> allFilms() {
        log.debug("GET /films");
        return films.values();
    }

    private long getNextId() {
        return currentId++;
    }

    void validate(Film film) {
        log.debug("Validation started for {}", film);
        if (film.getName().isBlank()) {
            String msg = "Film name should not be empty! film=" + film;
            log.error(msg);
            throw new ValidationException(msg);
        }

        if (film.getDescription().length() > 200) {
            String msg = "Max length description - 200 symbols.";
            log.error(msg);
            throw new ValidationException(msg);
        }

        LocalDate movieBirthday = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film.getReleaseDate().isBefore(movieBirthday)) {
            String msg = "release date - no earlier than December 28, 1895";
            log.error(msg);
            throw new ValidationException(msg);
        }

        if (film.getDuration() <= 0) {
            String msg = "The length of the movie must be a positive number.";
            log.error(msg);
            throw new ValidationException(msg);
        }
    }
}
