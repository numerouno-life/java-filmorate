package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = "id")
public class User {

    private Long id;

    @NotNull(message = "Email должен быть заполнен")
    @Email(message = "Ошибка валидации email")
    @NotBlank(message = "Email должен быть заполнен")
    private String email;

    @NotNull(message = "Логин должен быть заполнен")
    @NotBlank(message = "Логин должен быть заполнен")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения должна быть заполнена")
    @Past(message = "Ошибка валидации даты рождения, дата должна быть меньше текущей даты")
    private LocalDate birthday;

    private Set<Long> friends;
}
