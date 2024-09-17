package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film addFilm(Film film) {
        log.debug("Attempting to add film: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        log.info("Update film");
        return filmStorage.update(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLikeToFilm(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        if (film.getLikes().contains(user.getId())) {
            log.error("The user {} has already liked this movie {}", user, film);
            throw new ConditionsNotMetException("The user has already liked this movie");
        }
        log.info("User {} add like to film {}", user, film);
        film.getLikes().add(user.getId());
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userService.getUserById(userId);
        if (film == null) {
            String msg = "Film with ID: " + filmId + " not found.";
            log.error("Film with ID: {} not found.", filmId);
            throw new NotFoundException(msg);
        }
        if (user == null) {
            throw new NotFoundException("User with id: " + userId + " not found.");
        }
        if (!film.getLikes().contains(userId)) {
            String msg = "User with ID: " + userId + " has not liked this film.";
            log.warn(msg);
            throw new RuntimeException(msg);
        }
        film.getLikes().remove(userId);
        log.info("User with ID: {} removed like from film with ID: {}", userId, filmId);
    }

    public List<Film> getMostPopularFilms(int count) {
        Collection<Film> films = filmStorage.getAllFilms();
        if (films == null || films.isEmpty()) {
            return Collections.emptyList();
        }
        return films.stream()
                .sorted((f1, f2) -> Integer.compare(
                        Optional.ofNullable(f2.getLikes()).orElse(Collections.emptySet()).size(),
                        Optional.ofNullable(f1.getLikes()).orElse(Collections.emptySet()).size()
                ))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

}