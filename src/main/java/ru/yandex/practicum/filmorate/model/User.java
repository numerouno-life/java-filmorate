package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    @NonNull
    @NotBlank()
    @Email()
    private String email;
    @NonNull
    @Pattern(regexp = "\\S+")
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
}
