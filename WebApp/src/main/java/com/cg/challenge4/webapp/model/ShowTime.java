package com.cg.challenge4.webapp.model;

import java.util.Date;
import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowTime {
	
	
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date startTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date endTime;
	private String screenId;
	private String movieId;
	private List<String> lockedSeatIds;

}
