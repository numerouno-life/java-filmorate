package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
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
        if (user.getId() == null || !users.containsKey(user.getId())) {
            log.error("User with ID:{} not found.", user.getId());
            throw new NotFoundException("User with ID: " + user.getId() + " not found.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAllUser() {
        log.info("Returned all users");
        return users.values();
    }

    public User getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        throw new NotFoundException("Not found user with ID:" + userId);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
