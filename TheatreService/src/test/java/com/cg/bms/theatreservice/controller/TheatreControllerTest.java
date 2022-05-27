package com.cg.bms.theatreservice.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cg.bms.theatreservice.JsonUtil;
import com.cg.bms.theatreservice.exception.EntityNotFoundException;
import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.model.Theatres;
import com.cg.bms.theatreservice.repository.TheatreRepository;
import com.cg.bms.theatreservice.service.TheatreService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TheatreController.class)
public class TheatreControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private TheatreService theatreService;

	private Theatre theatre1;
	private Theatre theatre2;
	private List<Theatre> theatreList;
	private List<Theatre> theatreListByCity;

	@BeforeEach
	void setUp() throws Exception {
		theatreList = new ArrayList<>();
		theatre1 = Theatre.builder()._id("1").cityId("c1").name("t1").build();
		theatre2 = Theatre.builder()._id("2").cityId("c2").name("t2").build();
		theatreList.add(theatre1);
		theatreList.add(theatre2);
		theatreListByCity = new ArrayList<>();
		theatreListByCity.add(theatre2);

	}

	@Test
	public void createTheatreWhenPostMethod() throws Exception {

		when(theatreService.addTheatre(theatre1)).thenReturn(theatre1);

		mockMvc.perform(post("/theatres").contentType("application/json").content(JsonUtil.toJson(theatre1)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is(theatre1.getName()))).andDo(MockMvcResultHandlers.print());
		

	}

	@Test
	public void getAllTheatresOnGetMethod() throws Exception {
		Theatres theatres = Theatres.builder().theatres(theatreList).build();
		when(theatreService.getAllTheatres()).thenReturn(theatres);
		mockMvc.perform(MockMvcRequestBuilders.get("/theatres").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.theatres[0].name", is(theatre1.getName())))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	public void getTheatreOnGetMethod() throws Exception {
		when(theatreService.getTheatre(theatre1.get_id())).thenReturn(theatre1);
		mockMvc.perform(MockMvcRequestBuilders.get("/theatres"+"/" + theatre1.get_id())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(theatre1.getName()))).andDo(MockMvcResultHandlers.print());
		;
	}

	@Test
	public void updateTheatreTestWhenPutMethod() throws Exception {
		theatre2.set_id(null);
		Theatre updatedTheatre = theatre2;
		when(theatreService.getTheatre(theatre1.get_id())).thenReturn(theatre1);
		when(theatreService.updateTheatre(theatre1.get_id(), updatedTheatre)).thenReturn(updatedTheatre);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/theatres"+"/" + theatre1.get_id())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(updatedTheatre));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is(updatedTheatre.getName()))).andDo(MockMvcResultHandlers.print());
		
	}

	@Test
	public void deteleTheatreTest() throws Exception {
		when(theatreService.getTheatre(theatre1.get_id())).thenReturn(theatre1);
		mockMvc.perform(MockMvcRequestBuilders.delete("/theatres"+"/" + theatre1.get_id())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void getThetareById_notFound() throws Exception {
		try {
			when(theatreService.getTheatre(anyString())).thenThrow(new EntityNotFoundException(
					"Entity not found for the given identifier", new Throwable("NoSuchElementException")));
		} catch (EntityNotFoundException e) {

			mockMvc.perform(
					MockMvcRequestBuilders.get("/theatres"+"/random1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
					.andExpect(result -> assertEquals("Entity not found for the given identifier",
							result.getResolvedException().getMessage()))
					.andDo(MockMvcResultHandlers.print());

		}

	}

	@Test
	public void getTheatresByCityOnGetMethod() throws Exception {

		when(theatreService.getTheatresByCity("c2")).thenReturn(Theatres.builder().theatres(theatreListByCity).build());

		mockMvc.perform(MockMvcRequestBuilders.get("/cities/" + theatre2.getCityId() + "/theatres")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath(("$.theatres[0].name"), is(theatre2.getName())));

	}

	@AfterEach
	void tearDown() throws Exception {
		theatre2 = null;
		theatre1 = null;
		theatreList = null;
	}

}
