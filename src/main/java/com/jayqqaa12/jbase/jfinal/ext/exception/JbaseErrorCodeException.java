package com.jayqqaa12.jbase.jfinal.ext.exception;

public class JbaseErrorCodeException extends RuntimeException {

	protected int code;

	/**
	 * 
	 */
	private static final long serialVersionUID = 692099006894911532L;

	
	public JbaseErrorCodeException(Throwable cause) {

		super(cause);
		this.code=500;
	}

	public JbaseErrorCodeException(int code) {
		this.code = code;
	}



	public JbaseErrorCodeException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public JbaseErrorCodeException(ErrorCode error) {
		this(error.code,error.msg);
	}

	public int getErrorCode(){
		
		return code;
	}
}
