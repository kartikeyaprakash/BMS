package com.cg.bms.cityservice.exceptions;

import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String TRACE = "trace";

	@Value("${service.trace}")
	private boolean printStackTrace;

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCityNotFoundException(EntityNotFoundException e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);

	}
	
	@ExceptionHandler(RestTemplateException.class)
	public ResponseEntity<ErrorResponse> handleRestTemplateException(RestTemplateException e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.OK, request);

	}
	
	@ExceptionHandler(CallNotPermittedException.class)
	public ResponseEntity<ErrorResponse> apiHandleException(CallNotPermittedException e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.SERVICE_UNAVAILABLE, request);

	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleDefaultException(Exception e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.SERVICE_UNAVAILABLE, request);

	}
	
	

	private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, HttpStatus httpStatus,
			WebRequest request) {
		return buildErrorResponse(exception, exception.getMessage(), httpStatus, request);
	}

	
	private ResponseEntity<ErrorResponse> buildErrorResponse(Exception exception, String message, HttpStatus httpStatus,
			WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), exception.getMessage());

		if (printStackTrace) {
			errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
		}
		return ResponseEntity.status(httpStatus).body(errorResponse);
	}

//	private boolean isTraceOn(WebRequest request) {
//		String[] value = request.getParameterValues(TRACE);
//		return Objects.nonNull(value) && value.length > 0 && value[0].contentEquals("true");
//	}

}
