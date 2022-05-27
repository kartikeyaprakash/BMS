package com.cg.challenge4.webapp.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cg.challenge4.webapp.model.Seat;
import com.cg.challenge4.webapp.model.Seats;

@RestController
public class SeatController {

	@Value("${theatre-service.url}")
	private String theatreServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	@PostMapping("/seats")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Seat> addSeat(@RequestBody Seat seat) {
		String uri = String.format("%s/seats", theatreServiceUrl);
		
		Seat newSeat = restTemplate.postForObject(uri, seat, Seat.class);
		return new ResponseEntity<>(newSeat, HttpStatus.CREATED);
	}

	@GetMapping("/showTimes/{showTimeId}/seats")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Seats> getSeatsForShowTime(@PathVariable("showTimeId") String showTimeId,
			@RequestParam Optional<String> action) {
		
//		Map<String, String> urlParams = new HashMap<>();
//		urlParams.put("showTimeId", showTimeId);
//		
//		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(theatreServiceUrl)
//				.queryParam("action", action.get());
		Seats seats = restTemplate.getForObject(theatreServiceUrl+ "/showTimes/{showTimeId}/seats", Seats.class, showTimeId);
		return new ResponseEntity<>(seats, HttpStatus.OK);
	}

	@PutMapping("/seats/{seatId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Seat> updateSeat(@PathVariable("seatId") String seatId, @RequestBody Seat updatedSeat) {
//		restTemplate.put(theatreServiceUrl+"/"+seatId+"/seats", updatedSeat);
		HttpEntity<Seat> request = new HttpEntity<>(updatedSeat);
		ResponseEntity<Seat> response = restTemplate
				  .exchange(theatreServiceUrl+"/seats"+"/"+seatId, HttpMethod.PUT, request, Seat.class);
		
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

	@DeleteMapping("/seats/{seatId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Seat> deleteSeat(@PathVariable("seatId") String seatId) {
		HttpEntity<Seat> request = new HttpEntity<>(new Seat());
		ResponseEntity<Seat> response = restTemplate
				  .exchange(theatreServiceUrl+"/seats"+"/"+seatId, HttpMethod.DELETE, request, Seat.class);
		
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

	@GetMapping("/release")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<String> releaseLockOnSeat(@RequestParam String showTimeId, @RequestParam String seatId) {
		//TODO: search for requestparam in resttemplate
		
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(theatreServiceUrl)
				.queryParam("showTimeId", showTimeId)
				.queryParam("seatId", seatId);
		String response = restTemplate.getForObject(builder.toUriString(), String.class);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/releaseAll/{bookingId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<String> releaseLockOnAllSeatsForBooking(@PathVariable("bookingId") String bookingId) {
		ResponseEntity<String> response = restTemplate.getForEntity(theatreServiceUrl+"/releaseAll"+"/"+bookingId, String.class);
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

	@GetMapping("/lock/{bookingId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<String> lockSeatsForBooking(@PathVariable("bookingId") String bookingId) {
		ResponseEntity<String> response = restTemplate.getForEntity(theatreServiceUrl+"/lock"+"/"+bookingId, String.class);
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

}
