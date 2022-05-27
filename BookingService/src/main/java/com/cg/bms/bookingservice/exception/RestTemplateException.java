package com.cg.bms.bookingservice.exception;

public class RestTemplateException extends RuntimeException{
	
	public RestTemplateException(String message, Throwable err)
	{
		super(message,err);
	}
	
	public RestTemplateException(String message)
	{
		super(message);
	}

}
