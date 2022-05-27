package com.cg.challenge4.webapp.model;

import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//lockedByShowTimeId : empty if seatstatus is available


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Seat {
	
	
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	private Integer row;
	private Integer column;
	private List<String> lockedByShowTimeIds;
	//private SeatStatus seatStatus;
	private String screenId;
	

}
