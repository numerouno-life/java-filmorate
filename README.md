# Filmorate: Система рекомендаций фильмов

![Java](https://img.shields.io/badge/Java-21-%23ED8B00?logo=openjdk)
![Spring](https://img.shields.io/badge/Spring_Boot-3.2-%236DB33F?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-%234169E1?logo=postgresql)
![Hibernate](https://img.shields.io/badge/Hibernate-6-%2359666C?logo=hibernate)

## 🎬 Основные возможности

### 🎥 Управление контентом
- Полнофункциональное REST API для фильмов и пользователей
- Система лайков и рейтингов
- Жанровая классификация и возрастные рейтинги (MPA)

### 🤖 Рекомендательная система
- Алгоритм рекомендаций на основе схожих предпочтений пользователей
- Персонализированные подборки
- ТОП популярных фильмов

### ⚡ Оптимизации
- Кеширование часто запрашиваемых данных
- Оптимизированные SQL-запросы для работы с большими объемами данных
- 80% покрытие интеграционными тестами

## 🛠 Технологический стек

| Компонент       | Технологии                          |
|-----------------|-------------------------------------|
| Backend         | Java 21, Spring Boot 3, Hibernate 6 |
| База данных     | PostgreSQL 16                       |
| Документация    | Swagger UI                          |
| Развертывание   | Docker, Docker Compose              |

## 🗄 Схема базы данных

Примеры запросов для основных операций вашего приложения.

Запрос пользователя по ID
SELECT f.name,
       f.description,
       f.releaseDate,
       f.duration,
       m.rating AS mpa_rating,
       g.description AS genre
FROM films f
JOIN mpa m ON f.mpa_id = m.id
JOIN film_genres fg ON f.id = fg.film_id
JOIN genres g ON fg.genre_id = g.id
WHERE f.id = ?;
---------------
2. Запрос пользователя по ID
SELECT u.email,
       u.login,
       u.name,
       u.birthday
FROM users u
WHERE u.id = ?;
---------------
4. Поиск всех друзей пользователя по ID
SELECT u.id,
       u.login,
       u.name
FROM users u
JOIN friendships f ON u.id = f.friend_id
WHERE f.user_id = ? AND f.status = 'CONFIRMED';
---------------
5. Поиск всех фильмов, которым пользователь поставил лайк
SELECT f.name,
       f.description,
       f.releaseDate
FROM films f
JOIN likes l ON f.id = l.films_id
WHERE l.user_id = ?;
---------------
6. Запрос всех жанров для конкретного фильма
SELECT g.description AS genre
FROM genres g
JOIN film_genres fg ON g.id = fg.genre_id
WHERE fg.film_id = ?;

Schema
![Database schema](https://github.com/numerouno-life/java-filmorate/blob/main/ER-diagramme.png).
