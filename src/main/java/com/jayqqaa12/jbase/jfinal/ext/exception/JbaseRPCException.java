package com.jayqqaa12.jbase.jfinal.ext.exception;

public class JbaseRPCException extends ErrorCodeException {

	public JbaseRPCException(Throwable cause) {
		super(cause);
		this.code = 505;
	}

	public JbaseRPCException() {
		super(505);
	}

	private static final long serialVersionUID = 1L;

}
