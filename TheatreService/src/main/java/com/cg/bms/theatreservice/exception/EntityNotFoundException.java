package com.cg.bms.theatreservice.exception;

public class EntityNotFoundException extends RuntimeException{
	
	
	public EntityNotFoundException(String message, Throwable err)
	{
		super(message, err);
	}

}
