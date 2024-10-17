package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("filmService")
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;
    private final GenreService genreService;

    public Film addFilm(Film film) {
        log.debug("Attempting to add film: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        log.info("Updating film: {}", film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLikeToFilm(Film filmId, User userId) {
        log.info("Add like for film {} by the user {}", filmId, userId);
        userStorage.getUserById(userId.getId());
        filmStorage.getFilmById(filmId.getId());
        likeStorage.addLike(filmId.getId(), userId.getId());
    }

    public boolean removeLike(Long filmId, Long userId) {
        log.info("User with ID: {} removed like from film with ID: {}", userId, filmId);
        return likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(int count, Integer genreId) {
        return getAllFilms().stream()
                .filter(film -> {
                    if (genreId != null) {
                        boolean matches = film.getGenres().contains(genreService.getGenreById(genreId));
                        log.debug("Film ID: {}, Matches genre: {}", film.getId(), matches);
                        return matches;
                    }
                    return true;
                })
                .sorted((film1, film2) -> {
                    int comparison = Integer.compare(film2.getLikes().size(), film1.getLikes().size());
                    log.debug("Comparing Film ID: {} with likes {} to Film ID: {} with likes {}",
                            film1.getId(), film1.getLikes().size(), film2.getId(), film2.getLikes().size());
                    return comparison;
                })
                .limit(count > 0 ? count : Integer.MAX_VALUE)
                .collect(Collectors.toList());
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new NotFoundException("Фильм с ID " + id + " не найден.");
        }
        return film;
    }
}