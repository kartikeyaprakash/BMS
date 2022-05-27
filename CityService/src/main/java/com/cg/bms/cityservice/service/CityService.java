package com.cg.bms.cityservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.cityservice.exceptions.EntityNotFoundException;
import com.cg.bms.cityservice.model.Cities;
import com.cg.bms.cityservice.model.City;
import com.cg.bms.cityservice.repository.CityRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
@Transactional
public class CityService {
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${theatre-service.url}")
	private String theatreServiceUrl;
	
	
	public City addCity(City city)
	{
		return cityRepository.save(city);
	}
	
	public Cities getAllCities()
	{
		List<City> cities = new ArrayList<>();
		cityRepository.findAll().forEach(cities::add);
		return Cities.builder().cities(cities).build();
	}
	
	public City getCity(String cityId)
	{
		if(cityId.length()==0)
			return City.builder()._id("dummy").build();
		try {
			return cityRepository.findById(cityId).get();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("City to be updated not found for the given identifier", e);
		}	
	}
	
	
	public City updateCity(String cityId, City updatedCity)
	{
		City foundCity = getCity(cityId);
		foundCity.setName(updatedCity.getName());
		foundCity.setState(updatedCity.getState());
		foundCity.setTheatreIds(updatedCity.getTheatreIds());
		return cityRepository.save(foundCity);
	}
	
	
	//Delete all Theatres in Theatre DB
	@CircuitBreaker(name = "externalService")
	public void deleteCity(String cityId)
	{
		City cityToDelete = getCity(cityId);
		List<String> theatreIds = cityToDelete.getTheatreIds();
		for(String theatreId: theatreIds)
			restTemplate.delete(theatreServiceUrl+"/theatres/{theatreId}", theatreId);
		cityRepository.deleteById(cityId);
	}

}
