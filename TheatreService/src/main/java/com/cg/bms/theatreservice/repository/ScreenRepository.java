package com.cg.bms.theatreservice.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cg.bms.theatreservice.model.Screen;

@EnableScan
public interface ScreenRepository extends CrudRepository<Screen, String>{

}
