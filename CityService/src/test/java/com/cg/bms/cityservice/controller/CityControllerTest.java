package com.cg.bms.cityservice.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cg.bms.cityservice.JsonUtil;
import com.cg.bms.cityservice.exceptions.EntityNotFoundException;
import com.cg.bms.cityservice.model.Cities;
import com.cg.bms.cityservice.model.City;
import com.cg.bms.cityservice.repository.CityRepository;
import com.cg.bms.cityservice.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CityController.class)
class CityControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;

	@MockBean
	private CityService cityService;

	//@MockBean
	private CityRepository cityRepository;
	
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
	public void getAllCitiesOnGetMethod() throws Exception {
		Cities cities = Cities.builder().cities(cityList).build();
		when(cityService.getAllCities()).thenReturn(cities);
		mockMvc.perform(MockMvcRequestBuilders.get("/cities").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.cities[0].name", is(city1.getName())));
	}
	
	@Test
	public void getCityOnGetMethod() throws Exception {
		when(cityService.getCity(city1.get_id())).thenReturn(city1);
		mockMvc.perform(MockMvcRequestBuilders.get("/cities"+"/"+city1.get_id())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(city1.getName())));
	}
	
	@Test
	public void createCityWhenPostMethod() throws Exception {

		when(cityService.addCity(city1)).thenReturn(city1);

		mockMvc.perform(post("/cities").contentType("application/json").content(JsonUtil.toJson(city1)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is(city1.getName())));

	}
	
	@Test
	public void updateCitytestWhenPutMethod() throws Exception {
		city2.set_id(null);
		City updatedCity = city2;
		when(cityService.getCity(city1.get_id())).thenReturn(city1);
		when(cityService.updateCity(city1.get_id(), updatedCity)).thenReturn(updatedCity);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/cities"+"/"+city1.get_id())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(updatedCity));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is(updatedCity.getName())));
	}
	
	@Test
	public void deteleCitytest() throws Exception {
		when(cityService.getCity(city1.get_id())).thenReturn(city1);
		mockMvc.perform(MockMvcRequestBuilders.delete("/cities"+"/"+city1.get_id())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	@Test
	public void getCityById_notFound() throws Exception {
		try {
			when(cityService.getCity(anyString()))
					.thenThrow(new EntityNotFoundException("Entity not found for the given identifier",new Throwable("NoSuchElementException")) );
		} catch (EntityNotFoundException e) {

			mockMvc.perform(
					MockMvcRequestBuilders.get("/cities"+"/random1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
					.andExpect(result -> assertEquals("Entity not found for the given identifier",
							result.getResolvedException().getMessage()));

		}

	}
	
	@Test
	public void deleteCityById_notFound() throws Exception {
		try {
			when(cityService.getCity(anyString()))
					.thenThrow(new EntityNotFoundException("City to be deleted not found for the given identifier",new Throwable("NoSuchElementException")) );
		} catch (EntityNotFoundException e) {

			mockMvc.perform(
					MockMvcRequestBuilders.delete("/cities"+"/random1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
					.andExpect(result -> assertEquals("City to be deleted not found for the given identifier",
							result.getResolvedException().getMessage()));

		}

	}
	


	@AfterEach
	void tearDown() throws Exception {
		city1 = null;
		city2 = null;
		cityList = null;
	}

	
}
