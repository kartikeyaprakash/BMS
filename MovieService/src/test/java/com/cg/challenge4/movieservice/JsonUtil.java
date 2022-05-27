package com.cg.challenge4.movieservice;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;



public class JsonUtil {
	public static byte[] toJson(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
	            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);;
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
}
