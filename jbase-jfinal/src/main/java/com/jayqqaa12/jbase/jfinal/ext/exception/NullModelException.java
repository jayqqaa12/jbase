package com.jayqqaa12.jbase.jfinal.ext.exception;

import com.jayqqaa12.jbase.exception.ErrorCode;
import com.jayqqaa12.jbase.exception.JbaseErrorCodeException;

/**
 * 数据库中 获取必需的 Model 但是为null 
 * @author 12
 *
 */
public class NullModelException  extends JbaseErrorCodeException {

	private static final long serialVersionUID = 1L;

	public NullModelException( ) {
		super(ErrorCode.NULL_MODE_ERROR);
	}

}
