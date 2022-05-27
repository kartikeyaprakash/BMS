package com.cg.bms.theatreservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.theatreservice.exception.BookingNotFoundException;
import com.cg.bms.theatreservice.exception.EntityNotFoundException;
import com.cg.bms.theatreservice.exception.SeatNotAvailableForShowTimeException;
import com.cg.bms.theatreservice.exception.SeatNotFoundException;
import com.cg.bms.theatreservice.model.Booking;
import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.model.Seat;
import com.cg.bms.theatreservice.model.ShowTime;
import com.cg.bms.theatreservice.repository.SeatRepository;


@Service
@Transactional
public class SeatService {
	
	
	@Autowired
	private SeatRepository seatRepository;
	
	@Autowired
	private ScreenService screenService;
	
	@Autowired
	private ShowTimeService showTimeService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${booking-service.url}")
	private String bookingServiceUrl;


	
	//When seat is added initially, status is available and lockedByShowTimeIds are an empty set
	public Seat addSeat(Seat seat)
	{
		List<String> emptySet = new ArrayList<>();
		seat.setLockedByShowTimeIds(emptySet);
		//seat.setSeatStatus(SeatStatus.AVAILABLE);
		Seat addedSeat = seatRepository.save(seat);
		addSeatToScreen(addedSeat);
		return addedSeat;
	}
	
	public void addSeatToScreen(Seat seat)
	{
		Screen parentScreen = screenService.getScreen(seat.getScreenId());
		List<String> seatIds = parentScreen.getSeatIds();
		seatIds.add(seat.get_id());
		parentScreen.setSeatIds(seatIds);
		screenService.updateScreen(parentScreen.get_id(), parentScreen);
	}
	
	public Seat getSeat(String seatId)
	{
		if(seatId.length()==0)
			return Seat.builder()._id("dummy").build();
		try {
			return seatRepository.findById(seatId).get();
		} catch (NoSuchElementException e) {
			throw new EntityNotFoundException("Seat not found for the given identifier: "+seatId, e);
		}
//		if(foundSeat.isPresent())
//			return foundSeat.get();
//		
//		//custom exc
//		return null;

	}
	
	public List<Seat> getAvailableSeatsForShowTime(String showTimeId)
	{
		List<Seat> availableSeats = new ArrayList<>();
		ShowTime showTime = showTimeService.getShowTime(showTimeId);
		Screen screen = screenService.getScreen(showTime.getScreenId());
		for(String seatId: screen.getSeatIds())
		{
			if(isSeatAvailableForShowTime(showTimeId, seatId))
				availableSeats.add(getSeat(seatId));
		}
		return availableSeats;
	}
	
	public Seat updateSeat(String seatId, Seat updatedSeat)
	{
		
		//first check if seat exists for this seatId
		Seat seat = getSeat(seatId);
		
		
		//check idempotency
		updatedSeat.set_id(seatId);
		return seatRepository.save(updatedSeat);
	}
		
	
	//should delete Seat from all showtimes in screen as well
	public void deleteSeatFromScreen(Seat seat)
	{
		Screen parentScreen = screenService.getScreen(seat.getScreenId());
		List<String> seatIds = parentScreen.getSeatIds();
		seatIds.remove(seat.get_id());
		parentScreen.setShowTimeIds(seatIds);
		
		List<String> showTimeIds = parentScreen.getShowTimeIds();
		
		//deleting seat in every showTime in the Screen
		for(String showTimeId: showTimeIds)
		{
			ShowTime showTime = showTimeService.getShowTime(showTimeId);
			List<String> seatIdsForShowTime = showTime.getLockedSeatIds();
			seatIdsForShowTime.remove(seat.get_id());
			showTime.setLockedSeatIds(seatIdsForShowTime);
			showTimeService.updateShowTime(showTimeId, showTime);
		}
		screenService.updateScreen(parentScreen.get_id(), parentScreen);
	}
	
	
	public Seat deleteSeat(String seatId)
	{
		Seat seatToBeDeleted = getSeat(seatId);
		deleteSeatFromScreen(seatToBeDeleted);
		seatRepository.deleteById(seatId);
		return seatToBeDeleted;
	}
	
	public boolean isSeatAvailableForShowTime(String showTimeId, String seatId)
	{
		Seat seat = getSeat(seatId);
		return !seat.getLockedByShowTimeIds().contains(showTimeId);
	}
	
	public void lockSeatForShowTime(String showTimeId, String seatId)
	{
		Seat seat = getSeat(seatId);
		List<String> lockedByShowTimeIds = seat.getLockedByShowTimeIds();
		lockedByShowTimeIds.add(showTimeId);
		seat.setLockedByShowTimeIds(lockedByShowTimeIds);
		updateSeat(seat.get_id(), seat);
		
	}
	
	public void lockSeatsForBooking(String bookingId) throws Exception
	{
		Booking booking;
		
		booking = restTemplate.getForObject(bookingServiceUrl+"/bookings/{bookingId}", Booking.class, bookingId);
		List<String> lockedSeatIds = booking.getSeatIds();
		ShowTime bookingShowTime = showTimeService.getShowTime(booking.getShowTimeId());
		List<String> lockedSeatIdsForBookingShowTime = bookingShowTime.getLockedSeatIds();
		for(String seatId: lockedSeatIds)
		{
			if(isSeatAvailableForShowTime(booking.getShowTimeId(), seatId))
				lockSeatForShowTime(booking.getShowTimeId(), seatId);
			else 
				throw new SeatNotAvailableForShowTimeException("Seat is not available for showTime");

		}
		lockedSeatIdsForBookingShowTime.addAll(lockedSeatIds);
		bookingShowTime.setLockedSeatIds(lockedSeatIdsForBookingShowTime);
		showTimeService.updateShowTime(booking.getShowTimeId(), bookingShowTime);
	}
	
	
	//incorrect function, only booking seats have to be locked for booking show time
	public void lockSeatsForShowTime(String showTimeId) throws Exception
	{
		for(String seatId: showTimeService.getShowTime(showTimeId).getLockedSeatIds())
		{
			if(isSeatAvailableForShowTime(showTimeId, seatId))
				lockSeatForShowTime(showTimeId, seatId);
			else 
				throw new SeatNotAvailableForShowTimeException("Seat is not available for showTime");
			//throw exception in else block, seat is not available
		}
	}
	
	
	public void releaseAllSeatsForShowTime(String showTimeId)
	{
		ShowTime showTime = showTimeService.getShowTime(showTimeId);
		List<String> lockedSeatIds = showTime.getLockedSeatIds();
		for(String seatId: lockedSeatIds)
			releaseLockOnSeatForShowTime(showTimeId, seatId);
		lockedSeatIds.clear();
		showTime.setLockedSeatIds(lockedSeatIds);
		showTimeService.updateShowTime(showTimeId, showTime);
		
	}
	
	public void releaseSeatsForBooking(String bookingId)
	{
		Booking booking = restTemplate.getForObject(bookingServiceUrl+"/bookings/{bookingId}", Booking.class, bookingId);
		ShowTime showTime = showTimeService.getShowTime(booking.getShowTimeId());
		List<String> lockedSeatIdsForShowTime = showTime.getLockedSeatIds();
		List<String> lockedSeatIdsForBooking = booking.getSeatIds();
		for(String seatId: lockedSeatIdsForBooking )
			releaseLockOnSeatForShowTime(booking.getShowTimeId(), seatId);
		lockedSeatIdsForShowTime.removeAll(lockedSeatIdsForBooking);
		showTime.setLockedSeatIds(lockedSeatIdsForShowTime);
		showTimeService.updateShowTime(booking.getShowTimeId(), showTime);

	}
	
	public void releaseLockOnSeatForShowTime(String showTimeId, String seatId)
	{
		Seat seat = getSeat(seatId);
		List<String> lockedByShowTimeIds = seat.getLockedByShowTimeIds();
		lockedByShowTimeIds.remove(showTimeId);
		seat.setLockedByShowTimeIds(lockedByShowTimeIds);
		updateSeat(seat.get_id(), seat);
	}
	
	


}
