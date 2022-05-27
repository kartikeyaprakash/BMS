package com.cg.bms.theatreservice.exception;

import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String TRACE = "trace";

	@Value("${service.trace}")
	private boolean printStackTrace;

	
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);

	}
	
	@ExceptionHandler(SeatNotAvailableForShowTimeException.class)
	public ResponseEntity<ErrorResponse> handleSeatNotAvailableForShowTimeException(SeatNotAvailableForShowTimeException e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.CONFLICT, request);

	}
	
	
	@ExceptionHandler(BookingNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleBookingNotFoundException(BookingNotFoundException e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);

	}
	
	@ExceptionHandler(RestTemplateException.class)
	public ResponseEntity<ErrorResponse> handleRestTemplateException(RestTemplateException e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.OK, request);

	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleDefaultException(Exception e, WebRequest request) {
		return buildErrorResponse(e, HttpStatus.SERVICE_UNAVAILABLE, request);

	}


	
//	
//	@ExceptionHandler(TheatreNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleTheatreNotFoundException(TheatreNotFoundException e, WebRequest request) {
//		return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
//
//	}
//	
//	
//	@ExceptionHandler(ScreenNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleScreenNotFoundException(ScreenNotFoundException e, WebRequest request) {
//		return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
//
//	}
//	
//	@ExceptionHandler(ShowTimeNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleShowTimeNotFoundException(ShowTimeNotFoundException e, WebRequest request) {
//		return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
//
//	}
//	
//	
//	@ExceptionHandler(SeatNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleSeatNotFoundException(SeatNotFoundException e, WebRequest request) {
//		return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
//
//	}
	
		

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
