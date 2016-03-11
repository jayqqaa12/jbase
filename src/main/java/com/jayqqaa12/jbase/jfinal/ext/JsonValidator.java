package com.jayqqaa12.jbase.jfinal.ext;

import com.jayqqaa12.jbase.jfinal.ext.Validator;
import com.jayqqaa12.model.json.SendJson;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public abstract class JsonValidator extends Validator
{
	protected static final String ERROR_MSG = "code";

	protected void addError(int code )
	{
		super.addError("code", code+"");
	}
	
	protected void validateRequiredString(String field, int errorCode) {
		String value = controller.getPara(field);
		if (value == null || "".equals(value.trim()))
			addError(errorCode);
	}
	protected void validateString(String field, int minLen, int maxLen, int errorCode)
	{
		validateStringValue(controller.getPara(field), minLen, maxLen, errorCode);
	}
 
	private void validateStringValue(String value, int minLen, int maxLen, int errorCode) {
		if (StrKit.isBlank(value)) {
			addError(errorCode);
			return ;
		}
		if (value.length() < minLen || value.length() > maxLen)
			addError(errorCode);
	}
	
	@Override
	protected void handleError(Controller c)
	{
		c.renderJson(new SendJson(Integer.parseInt((String)c.getAttr("code"))) .toJson());
		
	}

 
}
