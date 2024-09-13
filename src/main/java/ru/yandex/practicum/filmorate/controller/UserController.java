package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //создание пользователя
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.debug("POST /users with {}", user);
        validate(user);
        log.info("User created successful {}", user);
        return ResponseEntity.ok(userService.create(user));
    }

    //обновление пользователя
    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        log.debug("PUT /users with {}", user);
        validate(user);
        log.info("User update successful {}", user);
        return ResponseEntity.ok(userService.update(user));
    }

    //получение списка всех пользователей
    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.debug("GET /users");
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("PUT /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
        return ResponseEntity.ok("Friend added");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("DELETE /users/{}/friends/{}", id, friendId);
        userService.removeFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    //список пользователей, являющихся его друзьями.
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id) {
        log.debug("GET /users/{}/friends", id);
        return ResponseEntity.ok(userService.getFriends(id));
    }

    //список друзей, общих с другим пользователем.
    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("GET /users/{}/friends/common/{}", id, otherId);
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
    }

    void validate(User user) {
        log.debug("Validation started for {}", user);
        if (!user.getEmail().contains("@")) {
            String msg = "email cannot be empty and must contain the @ symbol" + user;
            log.error(msg);
            throw new ValidationException(msg);
        }

        if (user.getLogin().contains(" ")) {
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
