package com.jayqqaa12.jbase.jfinal.ext.exception;

public class NullParamException extends ErrorCodeException {
	
	private static final long serialVersionUID = 6552469790406076583L;

	public NullParamException() {
		
		super(404,"Plase check  send param  not is null");
	}

}
