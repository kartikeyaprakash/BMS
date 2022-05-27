package com.cg.challenge4.webapp.controller;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cg.challenge4.webapp.model.Booking;
import com.cg.challenge4.webapp.model.Seat;
import com.cg.challenge4.webapp.model.Seats;

@RestController
public class BookingController {

	@Value("${booking-service.url}")
	private String bookingServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	@PostMapping("/bookings")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Booking> makeBooking(@RequestBody Booking booking, HttpServletRequest request) {
		
    	String authorization = request.getHeader("Authorization");
        String accessToken = authorization.substring("Bearer ".length()).trim();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		HttpEntity<Booking> httpEntity = new HttpEntity<>(booking, headers);


		Booking newBooking = restTemplate.postForObject(bookingServiceUrl+"/bookings", httpEntity, Booking.class);
		return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
	}

	@GetMapping("/bookings/{bookingId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Booking> getBookingById(@PathVariable("bookingId") String bookingId) {
		Booking newBooking = restTemplate.getForObject(bookingServiceUrl + "/bookings"+ "/" + bookingId,
				Booking.class);
		return new ResponseEntity<>(newBooking, HttpStatus.OK);
	}

	@GetMapping("/availableSeats/{bookingId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Seats> getAvailableSeatsForBookingShow(@PathVariable("bookingId") String bookingId) {
		
		Seats newSeats = restTemplate.getForObject(bookingServiceUrl + "/availableSeats"+ "/" + bookingId, Seats.class);
		return new ResponseEntity<>(newSeats, HttpStatus.OK);
	}

	@PutMapping("/bookings/{bookingId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Booking> updateBooking(@PathVariable("bookingId") String bookingId,
			@RequestBody Booking updatedBooking) {
//		restTemplate.put(bookingServiceUrl + "/" + bookingId + "/bookings", updatedBooking);
		HttpEntity<Booking> request = new HttpEntity<>(updatedBooking);
		ResponseEntity<Booking> response = restTemplate
				  .exchange(bookingServiceUrl + "/bookings"+ "/" + bookingId , HttpMethod.PUT, request, Booking.class);
		
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

	@DeleteMapping("/bookings/{bookingId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Booking> cancelBooking(@PathVariable("bookingId") String bookingId) {
		Booking booking = restTemplate.getForObject(bookingServiceUrl  + "/bookings"+ "/" + bookingId,
				Booking.class);
		restTemplate.delete(bookingServiceUrl + "/" + bookingId + "/bookings");
		return new ResponseEntity<>(booking, HttpStatus.OK);

	}

}
