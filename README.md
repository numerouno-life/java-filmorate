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

## 🛠 Примеры SQL-запросов

### 1. Получение информации о фильме с жанрами и рейтингом
```sql
SELECT 
    f.name,
    f.description,
    f.release_date,
    f.duration,
    m.rating AS mpa_rating,
    g.description AS genre
FROM films f
JOIN mpa m ON f.mpa_id = m.id
JOIN film_genres fg ON f.id = fg.film_id
JOIN genres g ON fg.genre_id = g.id
WHERE f.id = ?;
Возвращает полную информацию о конкретном фильме, включая его рейтинг MPA и жанры

### 2. Получение данных пользователя
```sql
SELECT 
    u.email,
    u.login,
    u.name,
    u.birthday
FROM users u
WHERE u.id = ?;
Базовый запрос для получения профиля пользователя

3. Список друзей пользователя
```sql
SELECT 
    u.id,
    u.login,
    u.name
FROM users u
JOIN friendships f ON u.id = f.friend_id
WHERE f.user_id = ? 
AND f.status = 'CONFIRMED';
Возвращает только подтвержденных друзей (двусторонняя дружба)

4. Фильмы, понравившиеся пользователю
```sql
SELECT 
    f.name,
    f.description,
    f.release_date
FROM films f
JOIN likes l ON f.id = l.film_id
WHERE l.user_id = ?
ORDER BY f.release_date DESC;
Список фильмов с сортировкой по дате выхода (новые первыми)

5. Жанры конкретного фильма
```sql
SELECT 
    g.description AS genre
FROM genres g
JOIN film_genres fg ON g.id = fg.genre_id
WHERE fg.film_id = ?;
Возвращает все жанры, связанные с указанным фильмом

6. ТОП-10 популярных фильмов
```sql
SELECT 
    f.id,
    f.name,
    COUNT(l.user_id) AS likes_count
FROM films f
LEFT JOIN likes l ON f.id = l.film_id
GROUP BY f.id, f.name
ORDER BY likes_count DESC
LIMIT 10;
Рейтинг фильмов по количеству лайков
Schema
![Database schema](https://github.com/numerouno-life/java-filmorate/blob/main/ER-diagramme.png).
