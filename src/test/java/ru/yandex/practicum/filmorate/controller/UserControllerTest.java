package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userController = new UserController(userService);
    }

    @Test
    public void whenUserFieldsAreOkThenDoNotThrowValidationException() {
        User user = User.builder()
                .email("practicum@yandex.ru")
                .login("login")
                .birthday(LocalDate.now())
                .name("name")
                .friends(Set.of(1L))
                .build();

        assertDoesNotThrow(() -> userController.validate(user));
    }

    @Test
    public void whenUserNameAndLoginIsEmptyThenThrowValidationException() {
        User user = User.builder()
                .email("practicum@yandex.ru")
                .login("")
                .birthday(LocalDate.now())
                .name("")
                .friends(Set.of(1L))
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    public void shouldThrowExceptionIfEmailIsBlankOrNotContainsSymbolAt() {
        User user = User.builder()
                .email("")
                .login("login")
                .birthday(LocalDate.now())
                .name("name")
                .friends(Set.of(1L))
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user));

        //Not symbol @
        User user1 = User.builder()
                .email("practicum")
                .login("login")
                .birthday(LocalDate.now())
                .name("name")
                .friends(Set.of(1L))
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user1));
    }

    @Test
    public void shouldThrowExceptionIfLoginIsEmptyOrContainsSpace() {
        User user = User.builder()
                .email("practicum@yandex.ru")
                .login("")
                .birthday(LocalDate.now())
                .name("name")
                .friends(Set.of(1L))
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user));

        //ContainsSpace
        User user1 = User.builder()
                .email("practicum@yandex.ru")
                .login(" ")
                .birthday(LocalDate.now())
                .name("name")
                .friends(Set.of(1L))
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user1));
    }

    @Test
    public void shouldThrowExceptionIfBirthdayInFuture() {
        User user = User.builder()
                .email("practicum@yandex.ru")
                .login("login")
                .birthday(LocalDate.now().plusDays(1))
                .name("name")
                .friends(Set.of(1L))
                .build();

        assertThrows(ValidationException.class, () -> userController.validate(user));
    }
}
