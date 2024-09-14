package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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
        log.info("Film added successful {}", film);
        return ResponseEntity.ok(filmService.addFilm(film));
    }

    //обновление фильма
    @PutMapping
    public ResponseEntity<Film> update(@RequestBody Film film) {
        log.debug("PUT /films with {}", film);
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
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("PUT /{}/like/{}", id, userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.debug("DELETE /{}like/{}", id, userId);
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("GET /popular?count={count}");
        return filmService.getMostPopularFilms(count);
    }


}
