package com.cg.bms.theatreservice.exception;

public class TheatreNotFoundException extends RuntimeException {
	
	public TheatreNotFoundException(String message, Throwable e)
	{
		super(message,e);
	}

}
