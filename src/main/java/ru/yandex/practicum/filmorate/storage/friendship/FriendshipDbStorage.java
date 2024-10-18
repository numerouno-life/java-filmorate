package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        try {
            final String sql = "INSERT INTO friendships (user_id, friend_id) VALUES (?, ?)";

            jdbcTemplate.update(sql, userId, friendId);
        } catch (Exception e) {
            log.error("Error adding friend request: {}", e.getMessage());
        }

    }

    @Override
    public boolean deleteFriend(Long userId, Long friendId) {

        final String sql = "DELETE FROM friendships WHERE user_id = ? AND friend_id = ?";
        try {
            jdbcTemplate.update(sql, userId, friendId);
            return true;
        } catch (Exception e) {
            log.error("Users are not friends or deletion failed");
            return false;
        }
    }


}
