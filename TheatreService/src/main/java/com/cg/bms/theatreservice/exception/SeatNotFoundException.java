package com.cg.bms.theatreservice.exception;

public class SeatNotFoundException extends RuntimeException{
	
	public SeatNotFoundException(String message, Throwable e)
	{
		super(message,e);
	}


}
