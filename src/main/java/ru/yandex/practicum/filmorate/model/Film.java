package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.validators.MinimumDate;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Long id;

    @NotBlank(message = "Movie title cannot be empty")
    @NotNull(message = "Movie title must be filled in")
    private String name;

    @NotNull(message = "Description cannot be empty")
    @NotBlank(message = "Description must be filled in")
    @Length(max = 200, message = "Maximum length 200 characters")
    private String description;

    @NotNull(message = "Date cannot be empty")
    @PastOrPresent(message = "Date cannot be in the future")
    @MinimumDate
    private LocalDate releaseDate;

    @NotNull(message = "Film duration cannot be empty")
    @Min(1)
    private long duration;

    private Set<Long> likes;
}