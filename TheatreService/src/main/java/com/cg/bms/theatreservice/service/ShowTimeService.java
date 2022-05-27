package com.cg.bms.theatreservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.theatreservice.exception.EntityNotFoundException;
import com.cg.bms.theatreservice.exception.ShowTimeNotFoundException;
import com.cg.bms.theatreservice.model.Movie;
import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.model.ShowTime;
import com.cg.bms.theatreservice.repository.ShowTimeRepository;


@Service
@Transactional
public class ShowTimeService {

	@Autowired
	private ShowTimeRepository showTimeRepository;

	@Autowired
	private ScreenService screenService;

	@Autowired
	private SeatService seatService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${movie-service.url}")
	private String movieServiceUrl;

	public ShowTime addShowTime(ShowTime showTime) {
		ShowTime addedShowTime = showTimeRepository.save(showTime);
		addShowTimeToScreen(addedShowTime);
		addShowTimeToMovie(addedShowTime);
		return addedShowTime;

	}

	public void addShowTimeToScreen(ShowTime showTime) {
		Screen parentScreen = screenService.getScreen(showTime.getScreenId());
		List<String> showTimeIds = parentScreen.getShowTimeIds();
		showTimeIds.add(showTime.get_id());
		parentScreen.setShowTimeIds(showTimeIds);
		screenService.updateScreen(parentScreen.get_id(), parentScreen);
	}

	private void addShowTimeToMovie(ShowTime showTime) {
		//TODO: add restcall
		String url = movieServiceUrl+"/{movieId}/showTime/{showTimeId}";
		restTemplate.exchange(url,HttpMethod.PUT,null, Movie.class,showTime.getMovieId(),showTime.get_id());

	}

	public ShowTime getShowTime(String showTimeId) {

		if (showTimeId.length() == 0)
			return ShowTime.builder()._id("dummy").build();
		try {
			return showTimeRepository.findById(showTimeId).get();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("ShowTime not found for the given identifier: " + showTimeId, e);
		}
//		if(foundShowTime.isPresent())
//			return foundShowTime.get();
//		
//		//custom exc
//		return null;
	}

	public List<ShowTime> getShowTimesForScreen(String screenId) {
		List<ShowTime> showTimesForScreen = new ArrayList<>();
		for (String showTimeId : screenService.getScreen(screenId).getShowTimeIds())
			showTimesForScreen.add(getShowTime(showTimeId));
		return showTimesForScreen;
	}

	public ShowTime updateShowTime(String showTimeId, ShowTime updatedShowTime) {
		// first check if showTime exists for given id
		ShowTime showTime = getShowTime(showTimeId);

		// check idempotency
		updatedShowTime.set_id(showTimeId);
		return showTimeRepository.save(updatedShowTime);
	}

	public void deleteShowTimeFromScreen(String showTimeId) {
		ShowTime showTimeToBeDeleted = getShowTime(showTimeId);
		Screen parentScreen = screenService.getScreen(showTimeToBeDeleted.getScreenId());
		List<String> showTimeIds = parentScreen.getShowTimeIds();
		showTimeIds.remove(showTimeId);
		parentScreen.setShowTimeIds(showTimeIds);
		screenService.updateScreen(parentScreen.get_id(), parentScreen);
	}

	public ShowTime deleteShowTime(String showTimeId) {
		ShowTime showTimeToBeDeleted = getShowTime(showTimeId);
		deleteShowTimeFromScreen(showTimeId);
		for (String lockedSeatId : showTimeToBeDeleted.getLockedSeatIds())
			seatService.releaseLockOnSeatForShowTime(showTimeId, lockedSeatId);
		showTimeRepository.deleteById(showTimeId);
		return showTimeToBeDeleted;
	}

}
