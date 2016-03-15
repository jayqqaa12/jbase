package com.jayqqaa12.jbase.jfinal.ext.exception;

public class ErrorCodeException extends RuntimeException {

	protected int code;

	/**
	 * 
	 */
	private static final long serialVersionUID = 692099006894911532L;

	
	public ErrorCodeException(Throwable cause) {
		super(cause);
	}

	public ErrorCodeException(int code) {
		this.code = code;
	}

	public ErrorCodeException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public int getErrorCode(){
		
		return code;
	}
}
