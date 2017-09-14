package com.jayqqaa12.jbase.exception;
public class JbaseRPCException extends JbaseErrorCodeException {

	private static final long serialVersionUID = 1L;

	public JbaseRPCException(Throwable cause) {
		super(ErrorCode.SERIVCE_RPC_ERROR.code, cause.getMessage());
	}

}
