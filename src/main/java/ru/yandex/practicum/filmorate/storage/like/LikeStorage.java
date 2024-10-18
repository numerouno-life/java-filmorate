package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface LikeStorage {

    void addLike(Long filmId, Long userId);

    boolean deleteLike(Long filmId, Long userId);

    Collection<Like> getLikesFilmId(Long filmId);
}
