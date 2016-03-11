package com.jayqqaa12.jbase.jfinal.ext;

import com.jayqqaa12.jbase.util.Validate;
import com.jfinal.core.Controller;

public abstract class Validator extends com.jfinal.validate.Validator
{

	/***
	 * 默认 error msg
	 */
	protected static final String ERROR_MSG = "msg";
	
	
	protected boolean isEmpty(String key){
		
		return Validate.isEmpty(controller.getPara(key));
	}

	@Override
	protected void handleError(Controller c)
	{
		c.renderJson(ERROR_MSG, c.getAttr(ERROR_MSG));

	}

	protected void addError(String errorMessage)
	{
		super.addError(ERROR_MSG, errorMessage);
	}

	protected void validateRequiredString(String field, String errorMessage)
	{
		super.validateRequiredString(field, ERROR_MSG, errorMessage);
	}

	protected void validateString(String field, int minLen, int maxLen, String errorMessage)
	{
		super.validateString(field, minLen, maxLen, ERROR_MSG, errorMessage);
	}

	protected void validateEmail(String field, boolean notBlank)
	{
		if (notBlank) super.validateEmail(field, ERROR_MSG, "email 格式错误 请重新输入");
		else
		{
			String value = controller.getPara(field);
			if (!Validate.isEmpty(value)) super.validateEmail(field, ERROR_MSG, "email 格式错误 请重新输入");
		}

	}

	protected void validateInteger(String field, int min, int max, String errorMessage)
	{
		super.validateInteger(field, min, max, ERROR_MSG, errorMessage);
	}


}
