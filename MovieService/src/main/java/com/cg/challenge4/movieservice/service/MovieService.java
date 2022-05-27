package com.cg.challenge4.movieservice.service;

import java.util.List;

import com.cg.challenge4.movieservice.entity.Movie;
import com.cg.challenge4.movieservice.entity.MovieList;

public interface MovieService {
	
	MovieList getAllMovies();
	Movie getMovieById(String id);
	Movie createMovie(Movie movie);
	Movie updateMovie(String id, Movie movie);
	void deleteMovie(String id);
	void deleteAllMovies();
	MovieList getAllMoviesPageable(Integer pageNo, Integer pageSize);
	MovieList getAllMoviesByGenre(Integer pageNo, Integer pageSize, String genre);
	MovieList getAllMoviesByYear(Integer pageNo, Integer pageSize, String year);
	MovieList getAllMoviesByName(Integer pageNo, Integer pageSize, String name);
	Movie addShowTime(String movieId,String showTimeId);

}
