package com.cg.bms.cityservice.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.cityservice.exceptions.EntityNotFoundException;
import com.cg.bms.cityservice.model.Cities;
import com.cg.bms.cityservice.model.City;
import com.cg.bms.cityservice.service.CityService;

@RestController
public class CityController {

	@Autowired
	private CityService cityService;

	@PostMapping("/cities")
	public ResponseEntity<City> addCity(@RequestBody City city) {
		City addedCity = cityService.addCity(city);
		return new ResponseEntity<>(addedCity, HttpStatus.CREATED);
	}

	@GetMapping("/cities")
	public ResponseEntity<Cities> getAllCities() {
		return new ResponseEntity<>(cityService.getAllCities(), HttpStatus.OK);
	}

	@GetMapping("/cities/{cityId}")
	public ResponseEntity<City> getCity(@PathVariable("cityId") String cityId) {

		try {
			return new ResponseEntity<>(cityService.getCity(cityId), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Entity not found for the given identifier", e);
		}
	}

	@PutMapping("/cities/{cityId}")
	public ResponseEntity<City> updateCity(@PathVariable("cityId") String cityId, @RequestBody City updatedCity) {
	
			return new ResponseEntity<>(cityService.updateCity(cityId, updatedCity), HttpStatus.OK);
	}

	@DeleteMapping("/cities/{cityId}")
	public ResponseEntity<City> deleteCity(@PathVariable("cityId") String cityId) {
		try {
			City deletedCity = cityService.getCity(cityId);
			cityService.deleteCity(cityId);
			return new ResponseEntity<>(deletedCity, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("City to be deleted not found for the given identifier", e);
		}
	}

}
