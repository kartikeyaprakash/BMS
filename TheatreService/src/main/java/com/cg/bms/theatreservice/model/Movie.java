package com.cg.bms.theatreservice.model;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

	private String id;

	private String name;

	private String length;

	private String year;

	private String genre;

	private String director;

	private LocalDate relereleaseDate;

	private List<String> cast;

	private List<String> showTimeIds;

}
