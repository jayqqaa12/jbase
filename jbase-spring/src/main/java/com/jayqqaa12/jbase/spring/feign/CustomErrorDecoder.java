package com.jayqqaa12.jbase.spring.feign;

import com.alibaba.fastjson.JSON;
import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.mvc.Resp;
import com.jayqqaa12.jbase.spring.mvc.RespCode;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String s, Response response) {
    try {
      if (response.body() != null) {
        String body = Util.toString(response.body().asReader());
        Resp req = JSON.parseObject(body, Resp.class);
        if (req.getCode() != RespCode.SUCCESS) {
          return new BusinessException(req.getCode(), req.getMsg(), null);
        }
      }
    } catch (IOException e) {
      return e;
    }
    return new Exception("UNKOWN ERROR");
  }


}
