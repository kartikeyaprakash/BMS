package com.cg.challenge4.webapp.model;

import java.time.LocalDate;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
	
	String id;
	String name;
	String length;
	String year;
	String genre;
	String director;
	LocalDate relereleaseDate;
	List<String> cast;

}
