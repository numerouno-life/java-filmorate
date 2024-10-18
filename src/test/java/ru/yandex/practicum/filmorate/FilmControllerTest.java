package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

    }

    private String createJsonFilm() throws JsonProcessingException {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1L, "Комедия"));
        Film film = Film.builder()
                .id(1L)
                .name("Frog")
                .description("Crazy frog in snow forest")
                .releaseDate(LocalDate.of(2005, 4, 23))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .genres(genres)
                .build();
        return objectMapper.writeValueAsString(film);
    }

    @Test
    void testForGetFilms() throws Exception {
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(createJsonFilm()));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Frog"));
    }

    @Test
    void testForCreateFilm() throws Exception {
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(createJsonFilm()))
                .andExpect(status().isCreated());
    }

    @Test
    void testForUpdateFilm() throws Exception {
        // Сначала создаем фильм
        mockMvc.perform(post("/films").contentType(MediaType.APPLICATION_JSON).content(createJsonFilm()));

        // Обновляем фильм, добавляя duration
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1L, "Комедия"));
        Film updatedFilm = Film.builder()
                .id(1L)
                .name("Frog Updated")
                .description("Updated description")
                .releaseDate(LocalDate.of(2005, 4, 23)) // Дата выхода
                .duration(120)
                .mpa(new Mpa(1, "G")) // MPA
                .genres(genres) // Жанры
                .build();

        String filmJson = objectMapper.writeValueAsString(updatedFilm);

        mockMvc.perform(put("/films").contentType(MediaType.APPLICATION_JSON).content(filmJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Frog Updated"));
    }
}