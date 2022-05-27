package com.cg.bms.theatreservice.exception;

public class ShowTimeNotFoundException extends RuntimeException{
	
	public ShowTimeNotFoundException(String message, Throwable e)
	{
		super(message,e);
	}


}
