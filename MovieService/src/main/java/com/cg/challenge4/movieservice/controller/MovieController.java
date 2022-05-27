package com.cg.challenge4.movieservice.controller;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.challenge4.movieservice.entity.Movie;
import com.cg.challenge4.movieservice.entity.MovieList;
import com.cg.challenge4.movieservice.repository.MoviePagingRepository;
import com.cg.challenge4.movieservice.service.MovieService;


import lombok.AllArgsConstructor;

@RestController
@RequestMapping("${movie-service.path:/api/v1/movie}")
public class MovieController {
	
	@Autowired
	private MovieService movieService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@GetMapping
	public ResponseEntity<MovieList> getAllMovies(){
		
		MovieList movielist = movieService.getAllMovies();
		if(movielist.getMovieList().isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<MovieList>(movielist, HttpStatus.OK);
		
		
	}
	
	@GetMapping("/{movieId}")
	public ResponseEntity<Movie> getMovie(@PathVariable(required = true, name = "movieId") String id){
		Movie movie = movieService.getMovieById(id);
		return new ResponseEntity<Movie>(movie,HttpStatus.OK);
	}
	
	@PostMapping
	  public ResponseEntity<Movie> createMovie(@Valid @RequestBody(required = true) Movie movie){
		  return new ResponseEntity<>(movieService.createMovie(movie),HttpStatus.CREATED);
	  }
	
	@PutMapping("/{movieId}")
	public ResponseEntity<Movie> updateMovie(@PathVariable("movieId") String movieId, @Valid @RequestBody(required = true) Movie movie){
		Movie updateMovie = movieService.updateMovie(movieId, movie);
		return new ResponseEntity<Movie>(updateMovie,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{id}")
	  public ResponseEntity<HttpStatus> deleteMovieById(@PathVariable("id") String id){
		movieService.deleteMovie(id);
		/*
		 * If a DELETE method is successfully applied, there are several response status codes possible:

			A 202 (Accepted) status code if the action will likely succeed but has not yet been enacted.
			A 204 (No Content) status code if the action has been enacted and no further information is to be supplied.
			A 200 (OK) status code if the action has been enacted and the response message includes a representation describing the status.
		 */
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@DeleteMapping
	  public ResponseEntity<HttpStatus> deleteAllMovie() {
		movieService.deleteAllMovies();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@GetMapping("/page")
	public ResponseEntity<MovieList> getAllMoviesPageable(
			@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize){
		MovieList movielist = movieService.getAllMoviesPageable(pageNo, pageSize);
		if(movielist.getMovieList().isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<MovieList>(movielist, HttpStatus.OK);
	}
	
	@GetMapping("/name")
	public ResponseEntity<MovieList> getAllMoviesByName(
			@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam String name){
		MovieList movielist = movieService.getAllMoviesByName(pageNo, pageSize,name);
		if(movielist.getMovieList().isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<MovieList>(movielist, HttpStatus.OK);
	}
	
	@GetMapping("/genre")
	public ResponseEntity<MovieList> getAllMoviesByGenre(
			@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam String genre){
		MovieList movielist = movieService.getAllMoviesByGenre(pageNo, pageSize,genre);
		if(movielist.getMovieList().isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<MovieList>(movielist, HttpStatus.OK);
	}
	@GetMapping("/year")
	public ResponseEntity<MovieList> getAllMoviesByYear(
			@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam String year){
		MovieList movielist = movieService.getAllMoviesByYear(pageNo, pageSize,year);
		if(movielist.getMovieList().isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<MovieList>(movielist, HttpStatus.OK);
	}
	
	@PutMapping("/{movieId}/showTime/{showTimeId}")
	public ResponseEntity<Movie> addShowTimeToMovie(@PathVariable(name = "movieId", required = true) String movieId,
			@PathVariable(name = "showTimeId", required = true) String showTimeId){
		Movie updateMovie = movieService.addShowTime(movieId, showTimeId);
		return new ResponseEntity<Movie>(updateMovie,HttpStatus.OK);
	}
	   

}
