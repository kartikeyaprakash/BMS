package com.cg.challenge4.movieservice.service;



import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.cg.challenge4.movieservice.entity.Movie;
import com.cg.challenge4.movieservice.exception.MovieNotFoundException;
import com.cg.challenge4.movieservice.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {
	
	@Mock
	private MovieRepository movieRepository;
	
	@Autowired
	@InjectMocks
	private MovieServiceImpl movieService;
	
	private Movie movie1;
	private Movie movie2;
	List<Movie> movieList;
	
	@BeforeEach
	public void setUp() {
		movieList = new ArrayList<>();
		
		List<String> actorList1 = new ArrayList<>();
		actorList1.add("Actor1.1");
		actorList1.add("Actor1.2");
		
		List<String> actorList2 = new ArrayList<>();
		actorList2.add("Actor2.1");
		actorList2.add("Actor2.2");

		movie1 = new Movie("Random1", "movie1", "2hr", "2022", "Drama", "Director2", LocalDate.of(2022, 1, 12), actorList1,null);
		movie2 = new Movie("Random2", "Movie2", "1hr30min", "2022", "Action", "Director2", LocalDate.of(2022,2,13), actorList2,null);
		movieList.add(movie1);
		movieList.add(movie2);
		
	}
	
	

	@Test
	public void givenMovieToAddShouldReturnAddedMovie() {
		
		when(movieRepository.save(any())).thenReturn(movie1);
		movieService.createMovie(movie1);
		verify(movieRepository,times(1)).save(any());
		
	}
	
	@Test
	public void givenGetAllMovieShouldReturnListOfAllMovies() {
		movieRepository.save(movie1);
		when(movieRepository.findAll()).thenReturn(movieList);
		List<Movie> movieList1 = movieService.getAllMovies().getMovieList();
		assertEquals(movieList1, movieList);
		verify(movieRepository,times(1)).save(movie1);
		verify(movieRepository,times(1)).findAll();
		
	}
	
	@Test
	public void givenIdThenShouldReturnMovieOfThatId() {
		when(movieRepository.findById("Random1"))
		.thenReturn(Optional.ofNullable(movie1));
		assertThat(movieService.getMovieById(movie1.getId())).isEqualTo(movie1);
	}
	
	@Test
	public void givenIdToDeleteThenShouldDeleteTheMovie() {
		when(movieRepository.findById(movie1.getId())).thenReturn(Optional.ofNullable(movie1));
		movieService.deleteMovie(movie1.getId());
		verify(movieRepository,times(1)).deleteById(movie1.getId());
	}
	
	@Test
	public void givenIdShouldUpdateUserIfFound() {
		when(movieRepository.findById(movie1.getId())).thenReturn(Optional.ofNullable(movie1));

		movie2.setId(null);
		movieService.updateMovie(movie1.getId(), movie2);
		
		Movie updatedMovie = movieService.getMovieById(movie1.getId());
		assertThat(updatedMovie.getName()).isEqualTo(movie2.getName());
		verify(movieRepository,times(2)).findById(movie1.getId());
	}
	
	@Test
	public void shouldThrowExceptionWhenMovieDoesntExist() {
		assertThrows(MovieNotFoundException.class, 
				() -> {
					when(movieRepository.findById(movie1.getId())).thenReturn(Optional.ofNullable(null));
					movieService.updateMovie(movie1.getId(), movie1);
				});
	}
	
	@AfterEach
	public void tearDown() {
		movie1 = null;
		movie2 = null;
		movieList = null;
	}

}
