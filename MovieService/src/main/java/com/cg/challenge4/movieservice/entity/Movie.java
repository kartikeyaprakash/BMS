package com.cg.challenge4.movieservice.entity;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.cg.challenge4.movieservice.config.LocalDateConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBTable(tableName = "Movie")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Movie {

	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	private String id;

	@NotBlank(message = "Name cannot be null")
	@DynamoDBAttribute
	private String name;

	@NotBlank(message = "Length cannot be null")
	@DynamoDBAttribute
	private String length;

	@NotBlank(message = "Year cannot be null")
	@DynamoDBAttribute
	private String year;

	@NotBlank(message = "Genre cannot be null")
	@DynamoDBAttribute
	private String genre;

	@NotBlank(message = "Director cannot be null")
	@DynamoDBAttribute
	private String director;

	
	@DynamoDBAttribute
	@DynamoDBTypeConverted(converter = LocalDateConverter.class)
	private LocalDate releaseDate;

	@DynamoDBAttribute
	private List<String> cast;

	@DynamoDBAttribute
	private List<String> showTimeIds;

}