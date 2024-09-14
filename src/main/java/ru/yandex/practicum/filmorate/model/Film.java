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
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    @NotNull(message = "Название фильма не может быть не заполнено")
    private String name;

    @NotNull(message = "Описание не может быть пустым")
    @NotBlank(message = "Описание не может быть пустым")
    @Length(max = 200, message = "Длина не может быть более 200 символов")
    private String description;

    @NotNull(message = "Дата должна быть заполнена")
    @PastOrPresent(message = "Дата не может быть в будущем")
    @MinimumDate
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность фильма должна быть заполнена")
    @Min(1)
    private long duration;

    private Set<Long> likes = new HashSet<>();
}
