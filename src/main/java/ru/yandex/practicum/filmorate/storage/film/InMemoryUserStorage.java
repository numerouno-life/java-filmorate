package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            String msg = "User with ID:" + user.getId() + " not found.";
            throw new NotFoundException(msg);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAllUser() {
        return users.values();
    }

    @Override
    public User getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            String msg = "User with ID:" + userId + " not found.";
            throw new NotFoundException(msg);
        }
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        update(user);
        update(friend);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        update(user);
        update(friend);
    }

    @Override
    public List<User> getFriends(Long userId) {
        User user = getUserById(userId);
        Set<Long> friendsId = user.getFriends();
        return friendsId.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> userFriends = users.get(userId).getFriends();
        Set<Long> otherFriends = users.get(otherId).getFriends();
        Set<Long> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherFriends);

        return commonFriends.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }


    private long getNextId() {
        return currentId++;
    }


}
