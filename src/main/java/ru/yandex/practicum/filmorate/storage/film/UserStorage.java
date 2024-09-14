package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User create(User user);

    User updateUser(User user);

    Collection<User> getAllUser();

    User getUserById(Long id);
}
