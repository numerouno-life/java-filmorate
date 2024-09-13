package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.AUGUST;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    private FilmController filmController;
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenFilmFieldsAreOkThenDoNotThrowValidationException() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.now())
                .duration(144L)
                .likes(Set.of(120L))
                .build();

        assertDoesNotThrow(() -> filmController.validate(film));
    }

    @Test
    public void whenFilmNameIsEmptyThenThrowValidationException() {
        Film film = Film.builder()
                .name("")
                .description("Description")
                .releaseDate(LocalDate.now())
                .duration(144L)
                .likes(Set.of(120L))
                .build();

        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void shouldThrowExceptionIfDescriptionIsTooLong() {
        Film film = Film.builder()
                .name("Name")
                .description("At the beginning of May 192... in Leningrad at an abandoned dacha on the Krestovka River "
                        + "a murder occurs. Criminal investigation officer Vasily Vitalievich Shelga "
                        + "discovers a stabbed man with signs of torture. In the spacious basement of the dacha "
                        + "some physical and chemical experiments were carried out. It is suggested that "
                        + "that the murdered man was a certain engineer Pyotr Petrovich Garin.")
                .releaseDate(LocalDate.now())
                .duration(144L)
                .likes(Set.of(120L))
                .build();

        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void shouldThrowExceptionIfMovieBirthdayIsBeforeMovieBirthDay() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1894, AUGUST, 22))
                .duration(144L)
                .likes(Set.of(120L))
                .build();

        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    public void shouldThrowExceptionIfDurationIsNegative() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.now())
                .duration(-30L)
                .likes(Set.of(120L))
                .build();

        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }
}
