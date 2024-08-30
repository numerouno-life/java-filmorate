package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;

    //создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.debug("POST /users with {}", user);
        validate(user);

        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("User created successful {}", user);
        return user;
    }

    //обновление пользователя
    @PutMapping
    public User update(@RequestBody User newUser) {
        log.debug("PUT /users with {}", newUser);
        if (newUser.getId() == null || !users.containsKey(newUser.getId())) {
            throw new ValidationException("User with ID:" + newUser.getId() + " not found.");
        }

        validate(newUser);
        users.put(newUser.getId(), newUser);

        log.info("User update successful {}", newUser);
        return newUser;
    }

    //получение списка всех пользователей
    @GetMapping
    public Collection<User> allUsers() {
        log.debug("GET /users");
        return users.values();
    }

    private long getNextId() {
        return currentId++;
    }

    void validate(User user) {
        log.debug("Validation started for {}", user);
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String msg = "email cannot be empty and must contain the @ symbol" + user;
            log.error(msg);
            throw new ValidationException(msg);
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String msg = "login cannot be empty or contain spaces";
            log.error(msg);
            throw new ValidationException(msg);
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            String msg = "date of birth cannot be in the future";
            log.error(msg);
            throw new ValidationException(msg);
        }
    }
}
