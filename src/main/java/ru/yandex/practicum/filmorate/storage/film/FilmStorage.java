package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    Film update(Film film);
    Collection<Film> getAllFilms();
    Film getFilmById(Long id);
    void addLike(Long id, Long userId);
    void removeLike(Long id, Long userId);
    List<Film> getMostPopularFilms(int count);
}
