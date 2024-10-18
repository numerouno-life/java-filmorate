package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmGenre {
    private Long filmId;
    private Long genreId;
}
