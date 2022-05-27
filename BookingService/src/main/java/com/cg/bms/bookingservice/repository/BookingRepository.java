package com.cg.bms.bookingservice.repository;

import java.util.Date;
import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cg.bms.bookingservice.model.Booking;


@EnableScan
public interface BookingRepository extends CrudRepository<Booking, String> {
	
	public List<Booking> findByUserId(String userId); 
	public List<Booking> findByUserIdAndBookingDateBefore(String userId, Date bookingDate);
	public List<Booking> findByUserIdAndBookingDateAfter(String userId, Date thresholdDate);

}
