package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //добавление фильма
    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        log.debug("POST /films with {}", film);
        validate(film);
        log.info("Film added successful {}", film);
        return ResponseEntity.ok(filmService.addFilm(film));
    }

    //обновление фильма
    @PutMapping
    public ResponseEntity<Film> update(@RequestBody Film film) {
        log.debug("PUT /films with {}", film);
        validate(film);
        log.info("Film update successful {}", film);
        return ResponseEntity.ok(filmService.update(film));
    }

    //получение всех фильмов
    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        log.debug("GET /films");
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("PUT /{}/like/{}", id, userId);
        filmService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("DELETE /{}like/{}", id, userId);
        filmService.removeLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("GET /popular?count={count}");
        return filmService.getMostPopularFilms(count);
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
