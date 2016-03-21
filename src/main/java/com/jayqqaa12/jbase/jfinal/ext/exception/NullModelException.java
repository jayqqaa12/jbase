package com.jayqqaa12.jbase.jfinal.ext.exception;

/**
 * 数据库中 获取必需的 Model 但是为null 
 * @author 12
 *
 */
public class NullModelException  extends JbaseErrorCodeException{

	public NullModelException( ) {
		super(405);
	}

	private static final long serialVersionUID = 1L;

}
