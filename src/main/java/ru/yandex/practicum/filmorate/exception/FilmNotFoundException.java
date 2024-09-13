package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends NotFoundException {
    private static final String MSG_TEMPLATE = "Фильм с id=%d не был найден";

    public FilmNotFoundException(Long filmId) {
        super(MSG_TEMPLATE.formatted(filmId));
    }
}
