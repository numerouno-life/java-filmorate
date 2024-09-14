package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Film addFilm(@RequestBody Film film) {
        log.debug("POST /films with {}", film);
        log.info("Film added successful {}", film);
        return filmService.addFilm(film);
    }

    //обновление фильма
    @PutMapping("/{id}")
    public Film update(@PathVariable Long id, @RequestBody Film film) {
        log.debug("PUT /films/{} with {}", id, film);
        if (!id.equals(film.getId())) {
            throw new IllegalArgumentException("ID in path and ID in request body do not match");
        }
        return filmService.update(film);
    }

    //получение всех фильмов
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("GET /films");
        return filmService.getAllFilms();
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
