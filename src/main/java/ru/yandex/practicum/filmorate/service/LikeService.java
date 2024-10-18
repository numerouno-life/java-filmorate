package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

@Service
@AllArgsConstructor
public class LikeService {
    private final LikeStorage likeStorage;

    public void addLike(long filmId, long userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(long userId, long filmId) {
        likeStorage.deleteLike(userId, filmId);
    }

}
