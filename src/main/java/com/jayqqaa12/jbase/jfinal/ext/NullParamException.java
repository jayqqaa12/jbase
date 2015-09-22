package com.jayqqaa12.jbase.jfinal.ext;

public class NullParamException extends RuntimeException {
	
	private static final long serialVersionUID = 6552469790406076583L;

	public NullParamException() {
		super("Plase check  send param  not is null");
	}

}
