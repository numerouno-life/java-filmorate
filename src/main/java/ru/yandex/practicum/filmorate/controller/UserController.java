package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User create(@Valid @RequestBody User user) {
        log.debug("POST /users with {}", user);
        User createdUser = userService.create(user);
        log.info("User created successfully {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.debug("PUT /users with {}", user);
        User updatedUser = userService.update(user);
        log.info("User updated successfully {}", updatedUser);
        return updatedUser;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.debug("GET /users");
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        log.info("GET /users/{}", id);
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("PUT /users/{}/friends/{}", id, friendId);
        User user = userService.getUserById(id);
        User friend = userService.getUserById(friendId);

        if (user == null || friend == null) {
            throw new NotFoundException("User or friend not found");
        }

        return userService.addFriend(user, friend);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.debug("DELETE /users/{}/friends/{}", id, friendId);
        User user = userService.getUserById(id);
        User friend = userService.getUserById(friendId);

        if (user == null || friend == null) {
            throw new NotFoundException("User or friend not found");
        }
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException("Friend not found in the user's friend list");
        }
        return userService.removeFriend(user, friend);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.debug("GET /users/{}/friends", id);
        User user = userService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("Friends list not found for user id " + id);
        }
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.debug("GET /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(userService.getUserById(id), userService.getUserById(otherId));
    }

}