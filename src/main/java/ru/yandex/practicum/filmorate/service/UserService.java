package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    public User create(User user) {
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }

    public void addFriend(Long userId, Long friendId) {
        friendshipStorage.addFriend(userId, getUserById(friendId).getId());
    }

    public Collection<User> getFriends(Long userId) {
        return userStorage.getUserFriends(userId);
    }

    public void removeFriend(User userFrom, User userTo) {
        friendshipStorage.deleteFriend(userFrom.getId(), userTo.getId());
    }

    public Collection<User> getMutualFriends(User userFrom, User userTo) {
        return userStorage.getMutualFriends(userFrom, userTo);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public User update(User user) {
        return userStorage.updateUser(user);
    }

    public String removeUser(User user) {
        return userStorage.removeUser(user);
    }
}