package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            log.error("User with id: {} already exist", user.getId());
        }
        log.info("Making the user");
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("User added {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Update user");
        if (user.getId() == null || !users.containsKey(user.getId())) {
            String msg = "User with ID:" + user.getId() + " not found.";
            log.error("User with ID:{} not found.", user.getId());
            throw new NotFoundException(msg);
        }
        users.put(user.getId(), user);
        log.info("user with ID: {} was update", user.getId());
        return user;
    }

    @Override
    public Collection<User> getAllUser() {
        log.info("Returned all users");
        return users.values();
    }

    public User getUserById(Long id) {
        log.info("Получен запрос на получение пользоваетля с id: {}", id);
        if (users.containsKey(id)) {
            log.trace("Пользоваетль с id: {} найден", id);
            return users.get(id);
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    private long getNextId() {
        return currentId++;
    }
}
