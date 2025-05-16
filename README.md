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

Примеры запросов для основных операций приложения.

erDiagram
    users ||--o{ friendships : "friends"
    users ||--o{ likes : "likes"
    films ||--o{ likes : "likes"
    films ||--o{ film_genres : "genres"
    films ||--|| mpa : "rating"
    genres ||--o{ film_genres : "films"
    
    users {
        int id PK
        varchar email
        varchar login
        varchar name
        date birthday
    }
    
    films {
        int id PK
        varchar name
        text description
        date release_date
        int duration
        int mpa_id FK
    }
📊 Примеры SQL-запросов

Schema
![Database schema](https://github.com/numerouno-life/java-filmorate/blob/main/ER-diagramme.png).
