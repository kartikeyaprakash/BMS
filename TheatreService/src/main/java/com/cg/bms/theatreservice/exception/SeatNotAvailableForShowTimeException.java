package com.cg.bms.theatreservice.exception;


public class SeatNotAvailableForShowTimeException extends RuntimeException{
	
	public SeatNotAvailableForShowTimeException(String message)
	{
		super(message);
	}

}
