package com.cg.bms.cityservice.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cg.bms.cityservice.model.City;

@EnableScan
public interface CityRepository extends CrudRepository<City, String>{

}
