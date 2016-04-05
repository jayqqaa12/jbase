package com.jayqqaa12.jbase.jfinal.ext.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;

/**
 * 统一XSS处理
 * @author L.cm
 */
public class XssHandler extends Handler
{

	// 排除的url，使用的target.startsWith匹配的
	private String exclude;

	public XssHandler(String exclude)
	{
		this.exclude = exclude;
	}

	@Override
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled)
	{
		if (target.indexOf(".") >= 0 || (StrKit.notBlank(target) && target.startsWith(exclude)))
		{
			nextHandler.handle(target, request, response, isHandled);
		}
		else
		{
			nextHandler.handle(target, new HttpServletRequestWrapper(request), response, isHandled);
		}
	}
}
