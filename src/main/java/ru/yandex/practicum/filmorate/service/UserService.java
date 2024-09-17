package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return userStorage.getUserById(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("User or friend not found");
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }
        if ((user.getFriends().contains(friend.getId())) | (friend.getFriends().contains(user.getId()))) {
            log.error("Users are already friends");
            throw new DuplicatedDataException("Users are already friends");
        }
        Set<Long> friendsFrom = user.getFriends();
        Set<Long> friendsTo = friend.getFriends();
        friendsFrom.add(friend.getId());
        friendsTo.add(user.getId());
        user.setFriends(friendsFrom);
        log.info("User:{} and User:{} now friends", user, friend);

    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (userId == null || friendId == null) {
            throw new NotFoundException("User or friend not found");
        }

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }

        if (!user.getFriends().contains(friend.getId()) || !friend.getFriends().contains(user.getId())) {
            log.info("User:{} and User:{} are not friends,no removal required", friend.getId(), user.getId());
            return;
        }
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());

        log.info("User:{} removed from user's friends User:{}", friend.getId(), user.getId());

    }

    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Friends list not found for user id " + userId);
        }
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
