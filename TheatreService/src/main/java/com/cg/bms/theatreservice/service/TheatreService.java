package com.cg.bms.theatreservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.theatreservice.exception.CityNotFoundException;
import com.cg.bms.theatreservice.exception.EntityNotFoundException;
import com.cg.bms.theatreservice.exception.ErrorResponse;
import com.cg.bms.theatreservice.exception.TheatreNotFoundException;
import com.cg.bms.theatreservice.model.Booking;
import com.cg.bms.theatreservice.model.City;
import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.model.Seat;
import com.cg.bms.theatreservice.model.SeatStatus;
import com.cg.bms.theatreservice.model.ShowTime;
import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.model.Theatres;
import com.cg.bms.theatreservice.repository.ScreenRepository;
import com.cg.bms.theatreservice.repository.SeatRepository;
import com.cg.bms.theatreservice.repository.ShowTimeRepository;
import com.cg.bms.theatreservice.repository.TheatreRepository;

@Service
@Transactional
public class TheatreService {
	
	@Autowired
	private TheatreRepository theatreRepository;
	
	@Autowired
	private ScreenService screenService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${city-service.url}")
	private String cityServiceUrl;

	
	
	public Theatre addTheatre(Theatre theatre)
	{
		Theatre addedTheatre = theatreRepository.save(theatre);
		addTheatreToCity(addedTheatre);
		return addedTheatre;
		
	}
	
	public Theatre getTheatre(String theatreId)
	{
		if(theatreId.length()==0)
			return Theatre.builder()._id("dummy").build();
		try {
			return theatreRepository.findById(theatreId).get();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Theatre not found for the given identifier: "+theatreId, e);
		}
		
	}
	
	public Theatres getAllTheatres()
	{
		List<Theatre> theatres = new ArrayList<>();
		theatreRepository.findAll().forEach(theatres::add);
		return Theatres.builder().theatres(theatres).build();

	}
	
	public City getCityOfTheatre(String theatreId)
	{
		
		String cityId = getTheatre(theatreId).getCityId();
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Accept", "application/json");
//		
//		HttpEntity<String> httpEntity = new HttpEntity<>(headers);
		
		
		return restTemplate.getForObject(cityServiceUrl+"/cities/{cityId}", City.class, cityId);
		
		
		//return restTemplate.exchange("http://city-service/{cityId}/cities", HttpMethod.GET, httpEntity, City.class, cityId).getBody();
	}
	
	
	//Update Theatre DTO to be different, will not include City ID. I
	public Theatre updateTheatre(String theatreId, Theatre updatedTheatre)
	{
		updatedTheatre.set_id(theatreId);
		return theatreRepository.save(updatedTheatre);
	}
	
	public Theatre deleteTheatre(String theatreId)
	{
		Theatre theatreToBeDeleted = getTheatre(theatreId);
		removeTheatreFromCity(theatreId);
		for(String screenId: theatreToBeDeleted.getScreenIds())
			screenService.deleteScreen(screenId);
		theatreRepository.deleteById(theatreId);
		return theatreToBeDeleted;
	}
	
	
	public void addTheatreToCity(Theatre theatre)
	{
		City cityOfTheatre = getCityOfTheatre(theatre.get_id());
		List<String> theatreIds = cityOfTheatre.getTheatreIds();
		theatreIds.add(theatre.get_id());
		cityOfTheatre.setTheatreIds(theatreIds);
	
		restTemplate.put(cityServiceUrl+"/cities/{cityId}", cityOfTheatre, theatre.getCityId());
	}
	
	
	public void removeTheatreFromCity(String theatreId)
	{
		City cityOfTheatre = getCityOfTheatre(theatreId);
		List<String> theatreIds = cityOfTheatre.getTheatreIds();
		theatreIds.remove(theatreId);
		cityOfTheatre.setTheatreIds(theatreIds);
	
		restTemplate.put(cityServiceUrl+"/cities/{cityId}", cityOfTheatre, getTheatre(theatreId).getCityId());
	}
	
	public Theatres getTheatresByCity(String cityId)
	{
	
		City city = restTemplate.getForObject(cityServiceUrl+"/cities/{cityId}", City.class, cityId);
		List<String> theatreIds = city.getTheatreIds();
		List<Theatre> theatres = new ArrayList<>();
		for(String theatreId: theatreIds)
		{
			theatres.add(getTheatre(theatreId));
		}
		return Theatres.builder().theatres(theatres).build();
		
	}
	
	
	
	
	
	

	

	
}
