package com.cg.bms.bookingservice.controller;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.bookingservice.model.Booking;
import com.cg.bms.bookingservice.model.Seats;
import com.cg.bms.bookingservice.repository.BookingRepository;
import com.cg.bms.bookingservice.service.BookingService;


@RestController
public class BookingController {
	
	
	@Autowired
	private BookingService bookingService;
	
	
	@PostMapping("/bookings")
	public ResponseEntity<Booking> makeBooking(@RequestBody Booking booking)
	{
//    	String authorization = request.getHeader("Authorization");
//        String accessToken = authorization.substring("Bearer ".length()).trim();
//        String[] parts = accessToken.split("\\.");
//        String decoded = new String(Base64.getUrlDecoder().decode(parts[1]));
//        JSONObject payload = new JSONObject(decoded);
//        booking.setUserId((String)payload.get("username"));
		return new ResponseEntity<>(bookingService.makeBooking(booking), HttpStatus.CREATED);
	}
	
	@GetMapping("/bookings/{bookingId}")
	public ResponseEntity<Booking> getBookingById(@PathVariable("bookingId") String bookingId)
	{
		return new ResponseEntity<>(bookingService.getBooking(bookingId), HttpStatus.OK);
	}
	
	
	@GetMapping("/availableSeats/{bookingId}")
	public ResponseEntity<Seats> getAvailableSeatsForBookingShow(@PathVariable("bookingId")String bookingId)
	{
		return new ResponseEntity<>(bookingService.getAllAvailableSeatsForBookingShow(bookingId), HttpStatus.OK);
	}
	
	
	@PutMapping("/bookings/{bookingId}")
	public ResponseEntity<Booking> updateBooking(@PathVariable("bookingId") String bookingId, @RequestBody Booking updatedBooking)
	{
		return new ResponseEntity<>(bookingService.updateBooking(bookingId, updatedBooking), HttpStatus.OK);
	}
	
	@DeleteMapping("/bookings/{bookingId}")
	public ResponseEntity<Booking> cancelBooking(@PathVariable("bookingId") String bookingId)
	{
		return new ResponseEntity<>(bookingService.deleteBooking(bookingId), HttpStatus.OK);

	}
	
	
        
    
    
}
