package ru.yandex.practicum.filmorate.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MinimumDateValidator implements ConstraintValidator<MinimumDate, LocalDate> {

    private LocalDate minimumDate;

    @Override
    public void initialize(MinimumDate constraintAnnotation) {
        minimumDate = LocalDate.parse(constraintAnnotation.value(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate == null || !localDate.isBefore(minimumDate);
    }
}
