package com.cg.bms.theatreservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.bms.theatreservice.exception.EntityNotFoundException;
import com.cg.bms.theatreservice.exception.ScreenNotFoundException;
import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.repository.ScreenRepository;


@Service
@Transactional
public class ScreenService {
	
	
	@Autowired
	private ScreenRepository screenRepository;
	
	@Autowired
	private TheatreService theatreService;
	
	@Autowired 
	private ShowTimeService showTimeService;
	
	@Autowired
	private SeatService seatService;
	
	
	
	public Screen addScreen(Screen screen)
	{
		Screen addedScreen =  screenRepository.save(screen);
		addScreenToTheatre(addedScreen);
		return addedScreen;
	}
	
	public void addScreenToTheatre(Screen screen)
	{
		Theatre parentTheatre= theatreService.getTheatre(screen.getTheatreId());
		List<String> screenIds = parentTheatre.getScreenIds();
		screenIds.add(screen.get_id());
		parentTheatre.setScreenIds(screenIds);
		theatreService.updateTheatre(parentTheatre.get_id(), parentTheatre);
		
	}
	
	public Screen getScreen(String screenId)
	{
		if(screenId.length()==0)
			return Screen.builder()._id("dummy").build();
		try {
			return screenRepository.findById(screenId).get();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Screen not found for the given identifier: "+screenId, e);
		}
//		if(foundScreen.isPresent())
//			return foundScreen.get();
//		
//		//custom exc
//		return null;
	}
	
	public List<Screen> getScreensForTheatre(String theatreId)
	{
		List<Screen> result = new ArrayList<>();
		List<String> screenIds = theatreService.getTheatre(theatreId).getScreenIds();
		for(String screenId: screenIds)
			result.add(getScreen(screenId));
		return result;
	}
	
	public Screen updateScreen(String screenId, Screen updatedScreen)
	{
		
		//first check if screen exists for given screenId
		Screen screen = getScreen(screenId);
		
		//check for idempotency here
		updatedScreen.set_id(screenId);
		return screenRepository.save(updatedScreen);
	}
	
	public void deleteScreenFromTheatre(String screenId)
	{
		Screen screenToDelete = getScreen(screenId);
		Theatre parentTheatre  = theatreService.getTheatre(screenToDelete.getTheatreId());
		List<String> screenIdsForTheatre = parentTheatre.getScreenIds();
		screenIdsForTheatre.remove(screenId);
		parentTheatre.setScreenIds(screenIdsForTheatre);
		theatreService.updateTheatre(parentTheatre.get_id(), parentTheatre);
	}
	
	public Screen deleteScreen(String screenId)
	{
		Screen screenToDelete = getScreen(screenId);
		deleteScreenFromTheatre(screenId);
		for(String showTimeId: screenToDelete.getShowTimeIds())
			showTimeService.deleteShowTime(showTimeId);

		for(String seatId: screenToDelete.getSeatIds())
			seatService.deleteSeat(seatId);
		
		screenRepository.deleteById(screenId);
		return screenToDelete;
	}


}
