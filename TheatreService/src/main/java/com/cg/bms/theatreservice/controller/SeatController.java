package com.cg.bms.theatreservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.theatreservice.model.Seat;
import com.cg.bms.theatreservice.model.Seats;
import com.cg.bms.theatreservice.service.SeatService;

@RestController
public class SeatController {
	
	
	@Autowired
	private SeatService seatService;
	
	
	@PostMapping("/seats")
	public ResponseEntity<Seat> addSeat(@RequestBody Seat seat)
	{
		return new ResponseEntity<>(seatService.addSeat(seat), HttpStatus.CREATED);
	}
	
	@GetMapping("/showTimes/{showTimeId}/seats")
	public  ResponseEntity<Seats> getSeatsForShowTime(@PathVariable("showTimeId") String showTimeId, @RequestParam Optional<String> action) throws Exception
	{
		if(!action.isPresent())
			return new ResponseEntity<>(new Seats(seatService.getAvailableSeatsForShowTime(showTimeId)), HttpStatus.OK);
		else if(action.get().equals("lock"))
			seatService.lockSeatsForShowTime(showTimeId);
		
		
		//think about this
		//this shouldnt be invoked anymore, as we would not lock seats for showtime at controller level, only by booking
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PutMapping("/seats/{seatId}")
	public ResponseEntity<Seat> updateSeat(@PathVariable("seatId") String seatId, @RequestBody Seat updatedSeat)
	{
		return new ResponseEntity<>(seatService.updateSeat(seatId, updatedSeat), HttpStatus.OK);
	}

	@DeleteMapping("/seats/{seatId}")
	public ResponseEntity<Seat> deleteSeat(@PathVariable("seatId") String seatId)
	{
		return new ResponseEntity<>(seatService.deleteSeat(seatId), HttpStatus.OK);
	}
	
	@GetMapping("/release")
	public ResponseEntity<String> releaseLockOnSeat(@RequestParam String showTimeId, @RequestParam String seatId)
	{
		seatService.releaseLockOnSeatForShowTime(showTimeId, seatId);
		return new ResponseEntity<>("Released Seat", HttpStatus.OK);
	}
	
//	@GetMapping("/{showTimeId}/releaseAll")
//	public ResponseEntity<String> releaseLockOnAllSeatsForShowTime(@PathVariable("showTimeId") String showTimeId)
//	{
//		theatreService.releaseAllSeatsForShowTime(showTimeId);
//		return new ResponseEntity<>("Released All Seats", HttpStatus.OK);
//	}
//	
	@GetMapping("/releaseAll/{bookingId}")
	public ResponseEntity<String> releaseLockOnAllSeatsForBooking(@PathVariable("bookingId") String bookingId)
	{
		seatService.releaseSeatsForBooking(bookingId);
		return new ResponseEntity<>("Released All Seats For booking", HttpStatus.OK);
	}
	
	@GetMapping("/lock/{bookingId}")
	public ResponseEntity<String> lockSeatsForBooking(@PathVariable("bookingId") String bookingId) throws Exception
	{	
		seatService.lockSeatsForBooking(bookingId);
		return new ResponseEntity<>("Locked All Seats for booking", HttpStatus.OK);
	}
	


}
