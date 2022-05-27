package com.cg.challenge4.movieservice.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cg.challenge4.movieservice.entity.Movie;

public interface MoviePagingRepository extends PagingAndSortingRepository<Movie, String> {
	@EnableScan
	@EnableScanCount
	Page<Movie> findByName(String name, Pageable pageable);

	@EnableScan
	@EnableScanCount
	Page<Movie> findByYear(String year, Pageable pageable);

	@EnableScan
	@EnableScanCount
	Page<Movie> findByGenre(String genre, Pageable pageable);

	@EnableScan
	@EnableScanCount
	Page<Movie> findAll(Pageable pageable);

}
