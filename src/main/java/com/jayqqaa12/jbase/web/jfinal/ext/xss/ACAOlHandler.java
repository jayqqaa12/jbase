package com.jayqqaa12.jbase.jfinal.ext.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class ACAOlHandler extends Handler {
 
    private String www;

	public ACAOlHandler(String www) {
    	
    	this.www=www;
	}

	@Override
    public void handle(String target, HttpServletRequest request,
            HttpServletResponse response, boolean[] isHandled) {
         
    	//正式 环境 请设置 成 需要 指定的 域名 
        response.addHeader("Access-Control-Allow-Origin", www); 
 
        nextHandler.handle(target, request, response, isHandled);
    }
 
}