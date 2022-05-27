package com.cg.challenge4.movieservice.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {
	
    @Override
    public String convert(final LocalDate time) {
        return time.toString();
    }
    @Override
    public LocalDate unconvert(final String stringValue) {
        return LocalDate.parse(stringValue);
    }
}