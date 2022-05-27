package com.cg.bms.bookingservice.exception;

public class EntityNotFoundException extends RuntimeException{
	
	
	public EntityNotFoundException(String message, Throwable err)
	{
		super(message, err);
	}

}
