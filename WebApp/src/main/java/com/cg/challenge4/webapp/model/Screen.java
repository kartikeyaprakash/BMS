package com.cg.challenge4.webapp.model;

import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Screen {
	
	
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	private List<String> showTimeIds;
	private String theatreId;
	private List<String> seatIds;


}
