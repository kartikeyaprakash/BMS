package com.cg.challenge4.movieservice.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cg.challenge4.movieservice.JsonUtil;
import com.cg.challenge4.movieservice.entity.Movie;
import com.cg.challenge4.movieservice.entity.MovieList;
import com.cg.challenge4.movieservice.exception.MovieNotFoundException;
import com.cg.challenge4.movieservice.repository.MovieRepository;
import com.cg.challenge4.movieservice.service.MovieService;
import com.cg.challenge4.movieservice.service.MovieServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

//@AutoConfigureMockMvc
//@ContextConfiguration(classes = { MovieController.class, MovieServiceImpl.class })
//@WebMvcTest
@ExtendWith(SpringExtension.class)

class MovieControllerTest {

	MockMvc mockMvc;

	ObjectMapper mapper;

	@Mock
	MovieServiceImpl movieService;

	@InjectMocks
	MovieController movieController;

	@MockBean
	MovieRepository movieRepository;

	@Value("${movie-service.path:api/v1/movie}")
	private String movieServiceUrl;

	private Movie movie1;
	private Movie movie2;
	private List<Movie> movieList;
	private MovieList movieListWrapper;

	@BeforeEach
	public void setUp() {
		movieServiceUrl = "/" + movieServiceUrl;
		movieList = new ArrayList<>();
		List<String> actorList1 = new ArrayList<>();
		actorList1.add("Actor1.1");
		actorList1.add("Actor1.2");
		
		List<String> actorList2 = new ArrayList<>();
		actorList2.add("Actor2.1");
		actorList2.add("Actor2.2");
		movie1 = new Movie("Random1", "movie1", "2hr", "2022", "Drama", "Director2", LocalDate.of(2022, 1, 12),
				actorList1,null);
		movie2 = new Movie("Random2", "Movie2", "1hr30min", "2022", "Action", "Director2", LocalDate.of(2022, 2, 13),
				actorList2,null);
		movieList.add(movie1);
		movieList.add(movie2);
		movieListWrapper = new MovieList();
		movieListWrapper.setMovieList(movieList);

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(movieController).dispatchOptions(true).build();
	}

	@Test
	public void getAllMoviesOnGetMethod() throws Exception {
		when(movieService.getAllMovies()).thenReturn(movieListWrapper);
		mockMvc.perform(MockMvcRequestBuilders.get(movieServiceUrl).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.movieList[0].name", is(movie1.getName())));
	}

	@Test
	public void getMovieOnGetMethod() throws Exception {
		when(movieService.getMovieById(movie1.getId())).thenReturn(movie1);
		mockMvc.perform(MockMvcRequestBuilders.get(movieServiceUrl + "/" + movie1.getId())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(movie1.getName())));
	}

	@Test
	public void createMovieWhenPostMethod() throws Exception {

		when(movieService.createMovie(any(Movie.class))).thenReturn(movie1);

		mockMvc.perform(post(movieServiceUrl).contentType("application/json").content(JsonUtil.toJson(movie2)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is(movie1.getName())));

	}

	@Test
	public void updateMovietestWhenPutMethod() throws Exception {
		movie2.setId(null);
		Movie updatedMovie = movie2;
		when(movieService.getMovieById(movie1.getId())).thenReturn(movie1);
		when(movieService.updateMovie(movie1.getId(), updatedMovie)).thenReturn(updatedMovie);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put(movieServiceUrl + "/" + movie1.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(updatedMovie));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is(updatedMovie.getName())));
	}

	@Test
	public void deteleMovietest() throws Exception {
		when(movieService.getMovieById(movie1.getId())).thenReturn(movie1);
		mockMvc.perform(MockMvcRequestBuilders.delete(movieServiceUrl + "/" + movie1.getId())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	@Test
	public void getMovieById_notFound() throws Exception {
		try {
			when(movieService.getMovieById(anyString()))
					.thenThrow(new MovieNotFoundException("Movie with id: " + "Random5" + " is not present"));
		} catch (MovieNotFoundException e) {

			mockMvc.perform(
					MockMvcRequestBuilders.get(movieServiceUrl + "/Random5").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof MovieNotFoundException))
					.andExpect(result -> assertEquals("Movie with id: " + "Random5" + " is not present",
							result.getResolvedException().getMessage()));

		}

	}

	@Test
	public void deleteMovieById_notFound() throws Exception {
//		when(movieService.getMovieById(anyString()))
//				.thenThrow(new MovieNotFoundException("Movie with id: " + "Random5" + " is not present"));
//		doNothing().when(movieService).deleteMovie(movie1.getId());
		try {
			doThrow(MovieNotFoundException.class).when(movieService).deleteMovie(movie1.getId());
		} catch (MovieNotFoundException e) {
			mockMvc.perform(MockMvcRequestBuilders.delete(movieServiceUrl + "/" + "Random5")
					.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof MovieNotFoundException))
					.andExpect(result -> assertEquals("Movie with id: " + "Random5" + " is not present",
							result.getResolvedException().getMessage()));
		}

	}

	@AfterEach
	public void tearDown() {
		movie1 = null;
		movie2 = null;
		movieList = null;
	}
}
