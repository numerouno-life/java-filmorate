package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.concurrent.Immutable;

@Immutable
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final String message;
    private final int error;
}
