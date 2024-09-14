package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
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
            String msg = "Movie with ID:" + filmId + " not found.";
            throw new NotFoundException(msg);
        }
        film.getLikes().add(userId); // Добавляем пользователя в Set лайков
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            String msg = "Movie with ID:" + filmId + " not found.";
            throw new NotFoundException(msg);
        }
        film.getLikes().remove(userId);// Убираем пользователя из Set лайков
        return film;
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

}
