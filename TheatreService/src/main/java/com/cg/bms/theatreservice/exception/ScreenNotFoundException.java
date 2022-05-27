package com.cg.bms.theatreservice.exception;

public class ScreenNotFoundException extends RuntimeException {
	
	public ScreenNotFoundException(String message, Throwable e)
	{
		super(message,e);
	}

}
