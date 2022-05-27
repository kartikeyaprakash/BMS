package com.cg.bms.theatreservice.model;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class City {
	
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	
	private String name;
	private String state;
	private List<String> theatreIds;

}
