package com.cg.challenge4.movieservice.exception.handler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cg.challenge4.movieservice.exception.MovieNotFoundException;

@ControllerAdvice
public class MovieExceptionHandler extends ResponseEntityExceptionHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errorList = ex.getBindingResult().getFieldErrors().stream().map(fe -> fe.getDefaultMessage())
				.collect(Collectors.toList());

		Map<String, Object> errorBody = new LinkedHashMap<>();
		errorBody.put("dataerror", "Problem in data received");
		errorBody.put("timestamp", LocalDateTime.now());
		errorBody.put("errors", errorList);

		log.error("An invalid request was rejected for reason: {} {}", status, errorBody.toString());
		return new ResponseEntity<>(errorBody, HttpStatus.UNSUPPORTED_MEDIA_TYPE);

	}

	@ExceptionHandler(MovieNotFoundException.class)
	public ResponseEntity<?> UserEntityFoundException(MovieNotFoundException ex) {

		Map<String, Object> errorBody = new LinkedHashMap<>();
		errorBody.put("error", "Not Found");
		errorBody.put("timestamp", LocalDateTime.now());
		errorBody.put("details", ex.getMessage());
		log.error("An invalid request was rejected for reason: {}", ex.getMessage());
		return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> AnyException(Exception ex) {

		Map<String, Object> errorBody = new LinkedHashMap<>();
		errorBody.put("timestamp", LocalDateTime.now());
		errorBody.put("details", ex.getMessage());

		log.error("An invalid request was rejected for reason: {}", ex.getMessage());

		return new ResponseEntity<>(errorBody, HttpStatus.SERVICE_UNAVAILABLE);
	}

}
