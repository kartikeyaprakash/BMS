package com.cg.bms.bookingservice.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.bookingservice.exception.EntityNotFoundException;
import com.cg.bms.bookingservice.model.Booking;
import com.cg.bms.bookingservice.model.Seats;
import com.cg.bms.bookingservice.model.ShowTime;
import com.cg.bms.bookingservice.repository.BookingRepository;



@Service
@Transactional
public class BookingService {
	
	/**
	 * Check if list of seats are available in showTime selected
	 * Make booking: initialize list of seats to showTime
	 * 				 Lock the seats in theatre service for showTime, if available
	 * 
	 * Cancel Booking: Release the locks on seats for showTime
	 * 
	 * Update Booking : Update the booking
	 * 					for seat updation, check availability again 
	 * 
	 * Get Bookings: Only interaction with booking DB done here, no requirement for other service calls
	 * 
	 * 
	 */
	
	
	
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Value("${theatre-service.url}")
	private String theatreServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Seats getAllAvailableSeatsForBookingShow(String bookingId)
	{
		Booking booking = getBooking(bookingId);
		return restTemplate.getForObject(theatreServiceUrl+"/showTimes/{showTimeId}/seats", Seats.class, booking.getShowTimeId());
		
	}
	
	
	public Booking makeBooking(Booking booking)
	{
//		ShowTime bookingShow = restTemplate.getForObject("http://theatre-service/{showTimeId}/showTimes", ShowTime.class, booking.getShowTimeId());
//		
//		Set<String> lockedSeatIdsForBookingShow =bookingShow.getLockedSeatIds();
//		lockedSeatIdsForBookingShow.addAll(booking.getSeatIds());
//		bookingShow.setLockedSeatIds(lockedSeatIdsForBookingShow);
//		restTemplate.put("http://theatre-service/{showTimeId}/showTimes",booking.getShowTimeId(), bookingShow);
//		restTemplate.getForObject("http://theatre-service/{showTimeId}/seats?action=lock", Seats.class, booking.getShowTimeId());
		Booking savedBooking = bookingRepository.save(booking);
		restTemplate.getForObject(theatreServiceUrl+"/lock/{bookingId}", String.class, savedBooking.get_id());
		return savedBooking;
			
	}
	
	public Booking getBooking(String bookingId)
	{
		if(bookingId.length()==0)
			return Booking.builder()._id("dummy").build();
		try {
			return bookingRepository.findById(bookingId).get();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Booking not found for given identifier: "+bookingId, e);
		}
//		return foundBooking.isPresent() ? foundBooking.get() : null;
	}
	
	
	//For history, use booking date
	public List<Booking> getBookingsForUser(String userId)
	{	
		return bookingRepository.findByUserId(userId);
	}
	
	
	
	//can use an updateBookingDTO here
	public Booking updateBooking(String bookingId, Booking updatedBooking)
	{
		
		//only date and movieId is updated as of now
		Booking booking = Booking.builder()._id(bookingId).bookingDate(updatedBooking.getBookingDate()).movieId(updatedBooking.getMovieId()).build();
		return bookingRepository.save(booking);
	}
	
	public Booking deleteBooking(String bookingId)
	{
		
		//Seat logic here	
		Booking bookingToBeDeleted = getBooking(bookingId);
		restTemplate.getForObject(theatreServiceUrl+"/releaseAll/{bookingId}", String.class, bookingId);		
		bookingRepository.deleteById(bookingId);
		return bookingToBeDeleted;
	}
	
	public List<Booking> findBookingBeforeDateForUser(String userId, Date bookingDate)
	{
		return bookingRepository.findByUserIdAndBookingDateBefore(userId, bookingDate);
	}
	
	
	public List<Booking> getAllBookingForUser(String userId)
	{
		return bookingRepository.findByUserId(userId);
	}
	
	public List<Booking> findBookingAfterDateForUser(String userId, Date thresholdDate)
	{
		return bookingRepository.findByUserIdAndBookingDateAfter(userId, thresholdDate);
	}
	
	
	
	
	
	
	

}
