package com.cg.challenge4.movieservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cg.challenge4.movieservice.entity.Movie;
import com.cg.challenge4.movieservice.entity.MovieList;
import com.cg.challenge4.movieservice.exception.MovieNotFoundException;
import com.cg.challenge4.movieservice.repository.MoviePagingRepository;
import com.cg.challenge4.movieservice.repository.MovieRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
	
	private MovieRepository movieRepository;
	private MoviePagingRepository moviePagingRepository;

	@Override
	public MovieList getAllMovies() {
		List<Movie> movies =  new ArrayList<>();
		movieRepository.findAll().forEach(movies::add);
		MovieList list = new MovieList();
		list.setMovieList(movies);
		return list;
	}

	@Override
	public Movie getMovieById(String id) {
		Optional<Movie> movie = movieRepository.findById(id);
		if(movie.isPresent())
			return movie.get();
		//TODO: Add string format
		throw new MovieNotFoundException("Movie with id: "+id+" is not present");
		
	}

	@Override
	public Movie createMovie(Movie movie) {
		
		return movieRepository.save(movie);
	}

	@Override
	public Movie updateMovie(String id, Movie movie) {
		Movie updateMovie = getMovieById(id);
		updateMovie.setCast(movie.getCast());
		updateMovie.setDirector(movie.getDirector());
		updateMovie.setGenre(movie.getGenre());
		updateMovie.setLength(movie.getLength());
		updateMovie.setName(movie.getName());
		updateMovie.setReleaseDate(movie.getReleaseDate());
		updateMovie.setYear(movie.getYear());
		
		return movieRepository.save(updateMovie);
	}

	@Override
	public void deleteMovie(String id) {
		Movie movie = getMovieById(id);
		movieRepository.deleteById(id);
		
	}

	@Override
	public void deleteAllMovies() {
		movieRepository.deleteAll();
	}

	@Override
	public MovieList getAllMoviesPageable(Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Movie> pagedResult = moviePagingRepository.findAll(paging);
		
		MovieList movieListWrapper = new MovieList();
		movieListWrapper.setMovieList(new ArrayList<Movie>());
		if(pagedResult.hasContent()) {
			List<Movie> movieList = pagedResult.getContent();
			movieListWrapper.setMovieList(movieList);
            return movieListWrapper;
        } 
		return movieListWrapper;
	}

	@Override
	public MovieList getAllMoviesByGenre(Integer pageNo, Integer pageSize, String genre) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Movie> pagedResult = moviePagingRepository.findByGenre(genre,paging);
		
		MovieList movieListWrapper = new MovieList();
		movieListWrapper.setMovieList(new ArrayList<Movie>());
		if(pagedResult.hasContent()) {
			List<Movie> movieList = pagedResult.getContent();
			movieListWrapper.setMovieList(movieList);
            return movieListWrapper;
        } 
		return movieListWrapper;
	}

	@Override
	public MovieList getAllMoviesByYear(Integer pageNo, Integer pageSize, String year) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Movie> pagedResult = moviePagingRepository.findByYear(year, paging);
		
		MovieList movieListWrapper = new MovieList();
		movieListWrapper.setMovieList(new ArrayList<Movie>());
		if(pagedResult.hasContent()) {
			List<Movie> movieList = pagedResult.getContent();
			movieListWrapper.setMovieList(movieList);
            return movieListWrapper;
        } 
		return movieListWrapper;
	}

	@Override
	public MovieList getAllMoviesByName(Integer pageNo, Integer pageSize, String name) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Movie> pagedResult = moviePagingRepository.findByName(name, paging);
		
		MovieList movieListWrapper = new MovieList();
		movieListWrapper.setMovieList(new ArrayList<Movie>());
		if(pagedResult.hasContent()) {
			List<Movie> movieList = pagedResult.getContent();
			movieListWrapper.setMovieList(movieList);
            return movieListWrapper;
        } 
		return movieListWrapper;
	}

	@Override
	public Movie addShowTime(String movieId, String showTimeId) {
		Movie updateMovie = getMovieById(movieId);
		List<String> showTimeList = updateMovie.getShowTimeIds();
		if(showTimeList == null) {
			showTimeList = new ArrayList<>();
			showTimeList.add(showTimeId);
		}else {
			showTimeList.add(showTimeId);
		}
		return movieRepository.save(updateMovie);
	}

	

}
