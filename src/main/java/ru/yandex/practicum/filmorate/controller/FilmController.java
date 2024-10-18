package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        if (film.getDuration() < 1) {
            throw new IllegalArgumentException("Duration must be greater than or equal to 1");
        }
        Film createdFilm = filmService.addFilm(film);
        return new ResponseEntity<>(createdFilm, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        Film updatedFilm = filmService.update(film);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("GET /films");
        return filmService.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLikeToFilm(filmService.getFilmById(id), userService.getUserById(userId));
        log.debug("PUT /{}/like/{}", id, userId);
        return filmService.getFilmById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("DELETE /{}/like/{}", id, userId);
        filmService.removeLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count,
                                      @RequestParam(required = false) Integer genreId) {
        return filmService.getMostPopularFilms(count, genreId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable Long id) {
        Film film = filmService.getFilmById(id);
        return film != null ? ResponseEntity.ok(film) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}