package com.cg.bms.theatreservice.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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


import com.cg.bms.theatreservice.JsonUtil;
import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.repository.ScreenRepository;
import com.cg.bms.theatreservice.service.ScreenService;

@WebMvcTest(ScreenController.class)
class ScreenControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private ScreenService screenService;

	@MockBean
	private ScreenRepository screenRepository;

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
	public void createScreenOnPostMethod() throws Exception {
		when(screenService.addScreen(screen1)).thenReturn(screen1);
		mockMvc.perform(post("/screens").contentType("application/json").content(JsonUtil.toJson(screen1)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$._id", is(screen1.get_id())));
	}

	@Test
	public void getScreenForTheatreListOnGetMethod() throws Exception {
		when(screenService.getScreensForTheatre(anyString())).thenReturn(screenList);
		mockMvc.perform(MockMvcRequestBuilders.get("/theatres/theatreId1/screens").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$[0]._id", is(screen1.get_id())));
		
	}
	
	@Test
	public void updateScreenOnPutMethod() throws Exception {
		screen2.set_id(null);
		Screen updatedScreen = screen2;
		when(screenService.updateScreen(screen1.get_id(), updatedScreen)).thenReturn(updatedScreen);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/screens"+"/"+screen1.get_id())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(updatedScreen));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$._id", is(updatedScreen.get_id())));
		
		
	}
	
	@Test
	public void deleteScreenOnDeleteMethod() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/screens/{screenId}",screen1.get_id())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	

	@AfterEach
	void tearDown() throws Exception {
		screen1 = null;
		screen2 = null;
		screenList = null;

	}

}
