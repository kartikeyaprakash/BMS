package com.cg.bms.bookingservice.controller;

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

import org.assertj.core.util.Arrays;
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

import com.cg.bms.bookingservice.JsonUtil;
import com.cg.bms.bookingservice.exception.EntityNotFoundException;
import com.cg.bms.bookingservice.model.Booking;
import com.cg.bms.bookingservice.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private BookingService bookingService;

	@Autowired
	private ObjectMapper objectMapper;

	private Booking booking1;
	private Booking booking2;
	private List<Booking> bookingList;

	@BeforeEach
	void setup() {
		bookingList = new ArrayList<>();

		List<String> seatIds = new ArrayList<>();
		seatIds.add("1S");
		seatIds.add("2S");
		booking1 = Booking.builder()._id("1").cityId("1C").theatreId("1T").movieId("1M").seatIds(seatIds)
				.screenId("1SC").showTimeId("1SH").build();

		booking2 = Booking.builder()._id("2").cityId("2C").theatreId("2T").movieId("2M").seatIds(seatIds)
				.screenId("2SC").showTimeId("2SH").build();

		bookingList.add(booking1);
		bookingList.add(booking2);

	}

	@Test
	public void createBookingWhenPostMethod() throws Exception {

		when(bookingService.makeBooking(booking1)).thenReturn(booking1);

		mockMvc.perform(post("/bookings").contentType("application/json").content(JsonUtil.toJson(booking1)))
				.andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$._id", is(booking1.get_id())));

	}

	@Test
	public void updateBookingsWhenPutMethod() throws Exception {
		booking2.set_id(null);
		Booking updatedBooking = booking2;
		when(bookingService.getBooking(booking1.get_id())).thenReturn(booking1);
		when(bookingService.updateBooking(booking1.get_id(), updatedBooking)).thenReturn(updatedBooking);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/bookings"+"/" + booking1.get_id())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(updatedBooking));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$._id", is(updatedBooking.get_id())));
	}

	@Test
	public void deteleBookingtest() throws Exception {
		when(bookingService.getBooking(booking1.get_id())).thenReturn(booking1);
		mockMvc.perform(MockMvcRequestBuilders.delete("/bookings"+"/" + booking1.get_id())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void getBookingById_notFound() throws Exception {
		try {
			when(bookingService.getBooking(anyString())).thenThrow(new EntityNotFoundException(
					"Entity not found for the given identifier", new Throwable("NoSuchElementException")));
		} catch (EntityNotFoundException e) {

			mockMvc.perform(
					MockMvcRequestBuilders.get("/bookings"+"/random1").contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound())
					.andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
					.andExpect(result -> {

						assertEquals("Entity not found for the given identifier",
								result.getResolvedException().getMessage());

					});

		}

	}
	

	
	@Test
	public void getBookingOnGetMethod() throws Exception {
		when(bookingService.getBooking(booking1.get_id())).thenReturn(booking1);
		mockMvc.perform(MockMvcRequestBuilders.get("/bookings"+"/"+booking1.get_id())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$._id", is(booking1.get_id())));
	}
	

	@AfterEach
	void tearDown() throws Exception {
		booking1 = null;
		booking2 = null;
		bookingList = null;
	}

}
