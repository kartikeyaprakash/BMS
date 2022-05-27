package com.cg.bms.cityservice.exceptions;

public class EntityNotFoundException extends RuntimeException{
	
	
	public EntityNotFoundException(String message, Throwable err)
	{
		super(message, err);
	}

}
