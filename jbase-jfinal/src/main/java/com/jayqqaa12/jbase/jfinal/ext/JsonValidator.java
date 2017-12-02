package com.jayqqaa12.jbase.jfinal.ext;

import com.jayqqaa12.jbase.jfinal.ext.model.vo.SendJson;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public abstract class JsonValidator extends Validator {
	private static final String ERROR_MSG = "code";

	protected void addError(int code) {
		super.addError(ERROR_MSG, code + "");
	}

	protected void validateRequiredString(String field, int errorCode) {
		String value = controller.getPara(field);
		if (value == null || "".equals(value.trim())) addError(errorCode);
	}

	protected void validateString(String field, int minLen, int maxLen, int errorCode) {
		validateStringValue(controller.getPara(field), minLen, maxLen, errorCode);
	}


	private void validateStringValue(String value, int minLen, int maxLen, int errorCode) {
		if (StrKit.isBlank(value)) {
			addError(errorCode);
			return;
		}
		if (value.length() < minLen || value.length() > maxLen) addError(errorCode);
	}

	@Override
	protected void handleError(Controller c) {

		String code = c.getAttr(ERROR_MSG);

		if (StrKit.notBlank(code)) c.renderJson(new SendJson(Integer.parseInt(code)).toJson());

	}

}
