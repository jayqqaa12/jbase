package com.jayqqaa12.jbase.jfinal.ext.exception;

public class NullParamException extends JbaseErrorCodeException {
	
	private static final long serialVersionUID = 6552469790406076583L;

	public NullParamException() {
		
		super(ErrorCode.NULL_PARAM_ERROR);
	}

	public NullParamException(int code, String s) {
		super(code,s);
	}
}
