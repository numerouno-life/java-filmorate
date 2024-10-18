package ru.yandex.practicum.filmorate.storage.filmgenre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Map;

public interface FilmGenreStorage {

    void addFilmGenre(long filmId, long genreId);

    Collection<Genre> getAllFilmGenresByFilmId(Long filmId);

    void deleteAllFilmGenresByFilmId(Long filmId);

    Map<Long, Collection<Genre>> getAllFilmGenres(Collection<Film> films);
}
