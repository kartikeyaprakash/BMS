package com.cg.bms.theatreservice.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//need @JsonPropert WRITE_ONLY instead of JsonIgnore, so that ids are deserialized, but not visible.
//Replace all @JsonIgnores with above


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Booking {
	
	
	
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	private String theatreId;
	private String screenId;
	private String userId;
	private String movieId;
	private String cityId;
	private String showTimeId;
	private List<String> seatIds;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date bookingDate;
	
	
	
	

}
