package com.cg.bms.theatreservice.exception;

public class CityNotFoundException extends RuntimeException{
	
	
	public CityNotFoundException(String message, Throwable err)
	{
		super(message, err);
	}

}
