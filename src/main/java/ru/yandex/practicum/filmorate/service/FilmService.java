package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        log.debug("Attempting to add film: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            String msg = "Film with ID: " + filmId + " not found.";
            log.error(msg);
            throw new NotFoundException(msg);
        }
        if (film.getLikes().contains(userId)) {
            String msg = "User with ID: " + userId + " has already liked this film.";
            log.warn(msg);
            throw new RuntimeException(msg);
        }
        film.getLikes().add(userId);
        log.info("User with ID: {} liked film with ID: {}", userId, filmId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            String msg = "Film with ID: " + filmId + " not found.";
            log.error("Film with ID: {} not found.", filmId);
            throw new NotFoundException(msg);
        }
        if (!film.getLikes().contains(userId)) {
            String msg = "User with ID: " + userId + " has not liked this film.";
            log.warn(msg);
            throw new RuntimeException(msg);
        }
        film.getLikes().remove(userId);
        log.info("User with ID: {} removed like from film with ID: {}", userId, filmId);
        return film;
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
