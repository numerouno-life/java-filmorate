1. Система рекомендации фильмов (Filmorate)
Особенности:
REST API для управления базой фильмов и пользователей с функцией рекомендаций
Реализована система лайков и рейтингов контента
Оптимизированы запросы к БД для работы с большими объемами данных
Разработал алгоритм рекомендаций на основе совпадений предпочтений пользователей
Настроил взаимодействие между микросервисами (основной сервис + рекомендации)
Добился 80% покрытия кода интеграционными тестами

Стек:
Java 21 Spring Boot 3 Hibernate PostgreSQL Docker Swagger
![Database schema](https://github.com/numerouno-life/java-filmorate/blob/main/ER-diagramme.png).

- Описание БД.
users — хранит информацию о пользователях (email, логин, имя, дата рождения).
films — содержит данные о фильмах (название, описание, дата выхода, продолжительность, рейтинг MPA).
genres — описывает жанры фильмов (например, драма, комедия).
mpa — рейтинги MPA (например, G, PG, R).
friendships — отображает связи между пользователями (дружба, её статус).
film_genres — связи между фильмами и их жанрами (многие ко многим).
likes — показывает, какие пользователи поставили лайки каким фильмам.

- Примеры запросов для основных операций вашего приложения.
1. Запрос пользователя по ID
```sql
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

2. Запрос пользователя по ID
SELECT u.email,
       u.login,
       u.name,
       u.birthday
FROM users u
WHERE u.id = ?;

3. Поиск всех друзей пользователя по ID
SELECT u.id,
       u.login,
       u.name
FROM users u
JOIN friendships f ON u.id = f.friend_id
WHERE f.user_id = ? AND f.status = 'CONFIRMED';

4. Поиск всех фильмов, которым пользователь поставил лайк
SELECT f.name,
       f.description,
       f.releaseDate
FROM films f
JOIN likes l ON f.id = l.films_id
WHERE l.user_id = ?;

5. Запрос всех жанров для конкретного фильма
SELECT g.description AS genre
FROM genres g
JOIN film_genres fg ON g.id = fg.genre_id
WHERE fg.film_id = ?;
