package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
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

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User addFriend(User userFrom, User userTo) {
        if (userFrom.getFriends() == null) {
            userFrom.setFriends(new HashSet<>());
        }
        if (userTo.getFriends() == null) {
            userTo.setFriends(new HashSet<>());
        }
        if ((userFrom.getFriends().contains(userTo.getId())) | (userTo.getFriends().contains(userFrom.getId()))) {
            log.error("Users are already friends");
            throw new DuplicatedDataException("Users are already friends");
        }
        Set<Long> friendsFrom = userFrom.getFriends();
        Set<Long> friendsTo = userTo.getFriends();
        friendsFrom.add(userTo.getId());
        friendsTo.add(userFrom.getId());
        userFrom.setFriends(friendsFrom);
        userTo.setFriends(friendsTo);
        userStorage.updateUser(userFrom);
        userStorage.updateUser(userTo);
        log.info("User:{} and User:{} now friends", userFrom, userTo);

        return userFrom;
    }

    public User removeFriend(User userFrom, User userTo) {
        if (userFrom == null || userTo == null) {
            throw new NotFoundException("User or friend not found");
        }

        if (userFrom.getFriends() == null) {
            userFrom.setFriends(new HashSet<>());
        }
        if (userTo.getFriends() == null) {
            userTo.setFriends(new HashSet<>());
        }

        if (!userFrom.getFriends().contains(userTo.getId()) || !userTo.getFriends().contains(userFrom.getId())) {
            log.info("User:{} and User:{} are not friends,no removal required", userTo.getId(), userFrom.getId());
            return userFrom;
        }

        userFrom.getFriends().remove(userTo.getId());
        userTo.getFriends().remove(userFrom.getId());

        userStorage.updateUser(userFrom);
        userStorage.updateUser(userTo);

        log.info("User:{} removed from user's friends User:{}", userTo.getId(), userFrom.getId());

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
        log.trace("Mutual friends of the user:{}", commonFriends.retainAll(otherFriends));

        return commonFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
