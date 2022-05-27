package com.cg.bms.cityservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.cityservice.model.City;
import com.cg.bms.cityservice.repository.CityRepository;


@ExtendWith(MockitoExtension.class)
class CityServiceTest {

	@Mock
	private CityRepository cityRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private CityService cityService;

	@Value("${theatre-service.url}")
	private String theatreServiceUrl;

	private City city1;
	private City city2;
	private List<City> cityList;

	@BeforeEach
	void setUp() throws Exception {
		cityList = new ArrayList<>();
		List<String> theatre1 = new ArrayList<>();
		theatre1.add("theatreId1");
		List<String> theatre2 = new ArrayList<>();
		theatre2.add("theatreId1");
		
		city1 = new City("cityId1", "city1", "state1", theatre1);
		city2 = new City("cityId2", "city2", "state2", theatre2);
		cityList.add(city1);
		cityList.add(city2);
	}

	@Test
	void givenCityToAddShouldReturnAddedCity() {
		when(cityRepository.save(any())).thenReturn(city1);
		cityService.addCity(city1);
		verify(cityRepository, times(1)).save(any());
	}

	@Test
	public void givenGetAllCityShouldReturnListOfAllCities() {
		cityRepository.save(city1);
		when(cityRepository.findAll()).thenReturn(cityList);
		List<City> cityList1 = cityService.getAllCities().getCities();
		assertEquals(cityList1, cityList);
		verify(cityRepository, times(1)).save(city1);
		verify(cityRepository, times(1)).findAll();

	}

	@Test
	public void givenIdThenShouldReturnCityOfThatId() {
		when(cityRepository.findById(city1.get_id())).thenReturn(Optional.ofNullable(city1));
		assertThat(cityService.getCity(city1.get_id())).isEqualTo(city1);

	}

	@Test
	public void givenIdToDeleteThenShouldDeleteTheMovie() {
		when(cityRepository.findById(city1.get_id())).thenReturn(Optional.ofNullable(city1));
		cityService.deleteCity(city1.get_id());
		verify(cityRepository, times(1)).deleteById(city1.get_id());
	}
	
	@Test
	public void givenIdShouldUpdateCityIfFound() {
		when(cityRepository.findById(city1.get_id())).thenReturn(Optional.ofNullable(city1));

		city2.set_id(null);
		cityService.updateCity(city1.get_id(), city2);
		
		City updatedCity = cityService.getCity(city1.get_id());
		assertThat(updatedCity.getName()).isEqualTo(city2.getName());
		verify(cityRepository,times(2)).findById(city1.get_id());
	}

	@AfterEach
	void tearDown() throws Exception {
		city1 = null;
		city2 = null;
		cityList = null;
	}

}
