package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        log.debug("POST /users with {}", user);
        User createdUser = userService.create(user);
        log.info("User created successfully {}", createdUser);
        return ResponseEntity.status(201).body(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> update(@RequestBody User user) {
        log.debug("PUT /users with {}", user);
        User updatedUser = userService.update(user);
        log.info("User updated successfully {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.debug("GET /users");
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        log.info("GET /users/{}", id);
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("PUT /users/{}/friends/{}", id, friendId);
        userService.addFriend(userService.getUserById(id), userService.getUserById(friendId));
        User user = userService.addFriend(userService.getUserById(id), userService.getUserById(friendId));
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("DELETE /users/{}/friends/{}", id, friendId);
        userService.removeFriend(userService.getUserById(id), userService.getUserById(friendId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long id) {
        log.debug("GET /users/{}/friends", id);
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("GET /users/{}/friends/common/{}", id, otherId);
        return ResponseEntity.ok(userService.getCommonFriends(userService.getUserById(id),
                userService.getUserById(otherId)));
    }

}