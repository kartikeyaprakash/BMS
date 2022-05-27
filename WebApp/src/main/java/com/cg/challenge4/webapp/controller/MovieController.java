package com.cg.challenge4.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cg.challenge4.webapp.exception.RestTemplateResponseErrorHandler;
import com.cg.challenge4.webapp.model.Movie;
import com.cg.challenge4.webapp.model.MovieList;
import com.cg.challenge4.webapp.model.Seat;

@RestController
@RequestMapping("/movie")
public class MovieController {
	
	@Value("${movie-service.url}")
	private String movieServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	 

	@GetMapping()
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public List<Movie> getAllMovies() {
		MovieList list = restTemplate.getForObject(movieServiceUrl, MovieList.class);

		return list.getMovieList();
	}
	
	@GetMapping("/{movieId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<?> getMovie(@PathVariable("movieId") String id){
		
		Movie movie = restTemplate.getForObject(movieServiceUrl+"/"+id, Movie.class);
		return new ResponseEntity<Movie>(movie,HttpStatus.OK);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	  public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
		Movie m = restTemplate.postForObject(movieServiceUrl, movie, Movie.class);
		  return new ResponseEntity<>(m,HttpStatus.CREATED);
	  }
	
	@PutMapping("/{movieId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Movie> updateMovie(@PathVariable("movieId") String movieId, @RequestBody Movie movie){
//		restTemplate.put(movieServiceUrl+"/"+movieId,movie);
		HttpEntity<Movie> request = new HttpEntity<>(movie);
		ResponseEntity<Movie> response = restTemplate
				  .exchange(movieServiceUrl+"/"+movieId, HttpMethod.PUT, request, Movie.class);
		return new ResponseEntity<Movie>(response.getBody(),HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{movieId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	  public ResponseEntity<HttpStatus> deleteMovieById(@PathVariable("movieId") String id){
		restTemplate.delete(movieServiceUrl+"/"+id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	
	@DeleteMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	  public ResponseEntity<HttpStatus> deleteAllMovie() {
		restTemplate.delete(movieServiceUrl);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
	}
	

}
