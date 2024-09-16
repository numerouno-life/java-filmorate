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

    @NotNull(message = "Email must be filled in")
    @Email(message = "Email validation error")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Login must be filled in")
    @NotBlank(message = "Login cannot be empty")
    private String login;

    @NotBlank
    private String name;

    @NotNull(message = "Date of birth must be filled in")
    @Past(message = "Date must not be greater than current date")
    private LocalDate birthday;

    private Set<Long> friends;
}
