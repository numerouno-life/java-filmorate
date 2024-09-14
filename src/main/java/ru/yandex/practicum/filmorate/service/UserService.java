package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllUser() {
        return userStorage.getAllUser();
    }

    public User getUserById(Long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        return user;
    }

    public User addFriend(User userFrom, User userTo) {
        if (userFrom.getFriends() == null) {
            userFrom.setFriends(new HashSet<>());
        }
        if (userTo.getFriends() == null) {
            userTo.setFriends(new HashSet<>());
        }
        if ((userFrom.getFriends().contains(userTo.getId())) | (userTo.getFriends().contains(userFrom.getId()))) {
            log.error("Пользователи в друзьях");
            throw new DuplicatedDataException("Пользователи уже в друзьях");
        }
        Set<Long> friendsFrom = userFrom.getFriends();
        Set<Long> friendsTo = userTo.getFriends();
        friendsFrom.add(userTo.getId());
        friendsTo.add(userFrom.getId());
        userFrom.setFriends(friendsFrom);
        userTo.setFriends(friendsTo);
        userStorage.updateUser(userFrom);
        userStorage.updateUser(userTo);
        log.info("Пользователь {} добавлен в друзья к пользовалю {}", userFrom, userTo);

        return userFrom;
    }

    public User removeFriend(User userFrom, User userTo) {
        if (userFrom.getFriends() == null || userTo.getFriends() == null) {
            log.error("Один из пользователей не имеет списка друзей");
            throw new NotFoundException("Один из пользователей не найден в списке друзей");
        }
        if (!userFrom.getFriends().contains(userTo.getId()) || !userTo.getFriends().contains(userFrom.getId())) {
            log.error("Пользователи не являются друзьями");
            throw new NotFoundException("Пользователи не являются друзьями");
        }
        Set<Long> friendsFrom = userFrom.getFriends();
        Set<Long> friendsTo = userTo.getFriends();
        friendsFrom.remove(userTo.getId());
        friendsTo.remove(userFrom.getId());
        userFrom.setFriends(friendsFrom);
        userTo.setFriends(friendsTo);
        userStorage.updateUser(userFrom);
        userStorage.updateUser(userTo);
        log.info("Пользователь {} удален из друзей пользователя {}", userTo, userFrom);

        return userFrom;
    }

    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        Set<Long> friendsId = user.getFriends();
        if (friendsId == null || friendsId.isEmpty()) {
            log.info("No friends found for user with id {}", userId);
            return Collections.emptyList();
        }
        return friendsId.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(User userFrom, User userTo) {
        Set<Long> userFriends = getUserById(userFrom.getId()).getFriends();
        Set<Long> otherFriends = getUserById(userTo.getId()).getFriends();
        Set<Long> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);
        log.trace("Общие друзья пользователя {}", commonFriends.retainAll(otherFriends));

        return commonFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
