package com.cg.challenge4.movieservice.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cg.challenge4.movieservice.entity.Movie;

@EnableScan
public interface MovieRepository extends CrudRepository<Movie, String> {

}
