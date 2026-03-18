package com.jayqqaa12.jbase.spring.mvc.resp;

public interface IResp {

  IResp response(int code);

  IResp response(int code, String msg);

  IResp response(int code, String msg, Object data);

}
