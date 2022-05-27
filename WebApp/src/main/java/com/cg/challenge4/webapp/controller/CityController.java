package com.cg.challenge4.webapp.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cg.challenge4.webapp.model.Cities;
import com.cg.challenge4.webapp.model.City;
import com.cg.challenge4.webapp.model.Seat;


@RestController
public class CityController {
	@Value("${city-service.url}")
	private String cityServiceURL;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostMapping("/cities")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<City> addCity(@RequestBody City city) {
		City addedCity = restTemplate.postForObject(cityServiceURL+"/cities", city, City.class);
		return new ResponseEntity<>(addedCity, HttpStatus.CREATED);
	}

	@GetMapping("/cities")
//	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Cities> getAllCities() {
		
		
		Cities cities = restTemplate.getForObject(cityServiceURL+"/cities", Cities.class);
		return new ResponseEntity<>(cities, HttpStatus.OK);
	}

	@GetMapping("/cities/{cityId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<City> getCity(@PathVariable("cityId") String cityId) {

		City city = restTemplate.getForObject(cityServiceURL+"/cities/{cityId}", City.class, cityId);
			return new ResponseEntity<>(city, HttpStatus.OK);
		
	}
	/*
	 * Check best practice for PUT operation
	 */

	@PutMapping("/cities/{cityId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<City> updateCity(@PathVariable("cityId") String cityId, @RequestBody City updatedCity) {
			//restTemplate.put(cityServiceURL+"/"+cityId+"/cities", updatedCity);
		HttpEntity<City> request = new HttpEntity<>(updatedCity);
		ResponseEntity<City> response = restTemplate
				  .exchange(cityServiceURL+"/cities"+"/"+cityId, HttpMethod.PUT, request, City.class);
			return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
		
	}

	@DeleteMapping("/cities/{cityId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<City> deleteCity(@PathVariable("cityId") String cityId) {
		
//			City deletedCity = restTemplate.getForObject(cityServiceURL+"/cities"+"/"+cityId, City.class);
//			restTemplate.delete(cityServiceURL+"/cities"+"/"+cityId);
//			return new ResponseEntity<>(deletedCity, HttpStatus.OK);
		HttpEntity<City> request = new HttpEntity<>(new City());
		ResponseEntity<City> response = restTemplate
				  .exchange(cityServiceURL+"/cities"+"/"+cityId, HttpMethod.DELETE,request, City.class);
		
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
		
	}

}
