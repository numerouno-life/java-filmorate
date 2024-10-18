package ru.yandex.practicum.filmorate.storage.friendship;

public interface FriendshipStorage {

    void addFriend(Long userId, Long friendId);

    boolean deleteFriend(Long userId, Long friendId);

}
