package com.cg.bms.theatreservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.repository.ScreenRepository;

@ExtendWith(MockitoExtension.class)
class ScreenServiceTest {
	
	@Mock
	private ScreenRepository screenRepository;
	
	@InjectMocks
	private ScreenService screenService;
	
	@Mock
	private TheatreService theatreService;
	
	@MockBean
	private ShowTimeService showTimeService;
	
	@MockBean
	private SeatService seatService;
	
	
	private Screen screen1;
	private Screen screen2;
	private List<Screen> screenList;

	@BeforeEach
	void setUp() throws Exception {
		List<String> showTime1 = new ArrayList<>();
		showTime1.add("showTimeId1");
		List<String> showTime2 = new ArrayList<>();
		showTime1.add("showTimeId2");
		
		List<String> seat1 = new ArrayList<>();
		seat1.add("seatId1");
		seat1.add("seatId2");
		List<String> seat2 = new ArrayList<>();
		seat2.add("seatId1");
		seat2.add("seatId2");
		
		
		
		screen1 = new Screen("id1", showTime1, "theatreId1", seat1);
		screen2 = new Screen("id1", showTime2, "theatreId1", seat2);
		screenList = new ArrayList<>();
		screenList.add(screen1);
		screenList.add(screen2);
	}
	
	@Test
	public void givenScreenToAddShouldReturnAddedScreen() {
		when(screenRepository.save(any())).thenReturn(screen1);
		List<String> screenIdList = new ArrayList<>();
		screenIdList.add(screen1.get_id());
		Theatre theatre = new Theatre("th1", "theatre", screenIdList, "city1");
		when(theatreService.getTheatre(anyString())).thenReturn(theatre);
		when(theatreService.updateTheatre(theatre.get_id(),theatre)).thenReturn(theatre);
		screenService.addScreen(screen1);
		verify(screenRepository,times(1)).save(any());
		
	}
	
	


	@AfterEach
	void tearDown() throws Exception {
		screen1 = null;
		screen2 = null;	
		screenList = null;
	}

	
}
