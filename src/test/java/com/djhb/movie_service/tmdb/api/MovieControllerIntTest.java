package com.djhb.movie_service.tmdb.api;
import com.djhb.movie_service.tmdb.Model.Movie;

import com.djhb.movie_service.tmdb.Repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MovieRepository movieRepository;

    @BeforeEach
    void cleanUp() {
        movieRepository.deleteAllInBatch();
    }

    @Test
    void givenMovie_whenCreateMovie_thenReturnSavedMovie() throws Exception {

        Movie movie = new Movie();
        movie.setName("Eagle");
        movie.setDirector("Karthik Gattamneni");
        movie.setActors(List.of("Ravi Teja", "Kavya Thapar", "Praneetha Patnaik"));

        var response = mockMvc.perform(
                        post("/movies")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(movie)));



                response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("$.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", is(movie.getActors())));
    }


    @Test
    void givenMovieId_whenFetchMovie_thenReturnMovie() throws Exception {


        Movie movie = new Movie();
        movie.setName("Eagle");
        movie.setDirector("Karthik Gattamneni");
        movie.setActors(List.of("Ravi Teja", "Kavya Thapar", "Praneetha Patnaik"));

        Movie savedMovie = movieRepository.save(movie);

        var response = mockMvc.perform(get("/movies/" +  savedMovie.getId()));

       response.andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(savedMovie.getId().intValue())))
               .andExpect(jsonPath("$.name", is(movie.getName())))
               .andExpect(jsonPath("$.director", is(movie.getDirector())))
               .andExpect(jsonPath("$.actors", is(movie.getActors())));


    }

    @Test
    void givenMovieId_whenUpdateMovie_thenReturnUpdatedMovie() throws Exception {
        Movie movie = new Movie();
        movie.setName("Eagle");
        movie.setDirector("Karthik Gattamneni");
        movie.setActors(List.of("Ravi Teja", "Kavya Thapar", "Praneetha Patnaik"));

        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        movie.setActors(List.of("Ravi Teja", "Kavya Thapar", "Praneetha Patnaik", "Hassain"));

        var response = mockMvc.perform(put("/movies/" +id )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        response.andDo(print())
                .andExpect(status().isOk());

        var fetchresponse = mockMvc.perform(get("/movies/" +id));

        fetchresponse.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("$.director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", is(movie.getActors())));


    }

    @Test
    void givenMovieId_whenDeleteRequest_thenMovieRemovedFromDb() throws Exception {

        Movie movie = new Movie();
        movie.setName("Eagle");
        movie.setDirector("Karthik Gattamneni");
        movie.setActors(List.of("Ravi Teja", "Kavya Thapar", "Praneetha Patnaik"));

        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        mockMvc.perform(delete("/movies/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());  // <-- FIXED (204)

        assertFalse(movieRepository.findById(id).isPresent());
    }

}
