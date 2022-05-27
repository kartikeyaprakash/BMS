package com.cg.bms.cityservice.exceptions;

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
