package com.cg.bms.bookingservice.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//lockedByShowTimeId : empty if seatstatus is available

//@Document(value = "seat")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat {
	
//	@Id
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	private Integer row;
	private Integer column;
	private List<String> lockedByShowTimeIds;
	//private SeatStatus seatStatus;
	private String screenId;
	

}
