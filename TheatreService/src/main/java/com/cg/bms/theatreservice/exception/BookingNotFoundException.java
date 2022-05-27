package com.cg.bms.theatreservice.exception;

public class BookingNotFoundException extends RuntimeException{
	
	public BookingNotFoundException(String message, Throwable e)
	{
		super(message,e);
	}

}
