package com.cg.bms.bookingservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.bookingservice.exception.EntityNotFoundException;
import com.cg.bms.bookingservice.model.Booking;
import com.cg.bms.bookingservice.repository.BookingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
	
	@Mock
	private BookingRepository bookingRepository;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Value("${theatre-service.url}")
	private String theatreServiceUrl;

	@InjectMocks
	private BookingService bookingService;


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
	void givenBookingToAddShouldReturnAddedCity() {
		when(bookingRepository.save(any())).thenReturn(booking1);
		bookingService.makeBooking(booking1);
		verify(bookingRepository, times(1)).save(any());
		verify(restTemplate, times(1)).getForObject(theatreServiceUrl+"/lock/{bookingId}", String.class, booking1.get_id());
		
	}
	
	
	@Test
	void getBookingByIdShouldReturnBooking()
	{
		when(bookingRepository.findById(any())).thenReturn(Optional.ofNullable(booking1));
		assertThat(bookingService.getBooking(booking1.get_id())).isEqualTo(booking1);
	}
	
	@Test
	void getBookingByIdThrowsNotFoundException()
	{
		when(bookingRepository.findById(any())).thenThrow(new NoSuchElementException());
//		assertThrows(EntityNotFoundException.class,()->{
//			bookingService.getBooking(booking1.get_id());
//			
//		} , "Entity not found for given identifier: "+booking1.get_id());
		
		try 
		{
			bookingService.getBooking(booking1.get_id());
			fail("Expected an EntityNotFoundException to be thrown");
		}catch(EntityNotFoundException e)
		{
			assertEquals(e.getMessage(), "Booking not found for given identifier: "+booking1.get_id());
		}
		
	}
	
	
	
	
	@AfterEach
	void tearDown()
	{
		booking1= null;
		booking2= null;
		bookingList = null;
	}

}
