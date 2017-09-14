package com.jayqqaa12.jbase.exception;

public class JbaseErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = 692099006894911532L;

	private int code;

	
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
