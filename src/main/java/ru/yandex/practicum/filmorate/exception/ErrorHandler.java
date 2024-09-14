package ru.yandex.practicum.filmorate.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ConstraintViolationException e) {
        log.error("validation error {}", e.getMessage());
        return new ErrorResponse("ConstraintViolationException " + e.getMessage(), 400);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        log.error("Not found error: {}", exception.getMessage(), exception);
        return new ErrorResponse("Resource not found: " + exception.getMessage(), 404);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.error("Validation error: {}", errorMessage);
        return new ErrorResponse(errorMessage, 400);
    }

    @ExceptionHandler(NotFoundUserException.class)
    @ResponseStatus(HttpStatus.OK)
    public ErrorResponse handleNotFoundException(NotFoundUserException e) {
        log.error("User not found {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(),201);
    }

    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicatedDataException(DuplicatedDataException e) {
        log.error("Duplicate error: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), 400);
    }

    @ExceptionHandler(ConditionsNotMetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConditionsNotMetException(ConditionsNotMetException e) {
        log.error("Conditions exception error: {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage(), 400);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowableException(final Throwable e) {
        log.error("Unexpected error: {}", e.getMessage(), e);
        return new ErrorResponse("Произошла непредвиденная ошибка.", 500);
    }
}
