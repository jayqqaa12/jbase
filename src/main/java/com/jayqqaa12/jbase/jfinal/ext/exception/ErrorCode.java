package com.jayqqaa12.jbase.jfinal.ext.exception;

/**
 * Created by 12 on 2016/3/28.
 *
 * 默认的几个错误
 */
public enum  ErrorCode {

    SERVER_ERROR(500,"SERVICE ERROR"),
    SERIVCE_RPC_ERROR(501,"SERIVCE_RPC_ERROR"),

    UNAUTHORIZED(401,"Unauthorized") ,
    FORBINDDEN(403,"Forbidden"),

    NOT_EXSIT_INTERFACE_ERROR(404,"INTERFACE NOT EXIST")  ,

    NULL_PARAM_ERROR(404,"NULL_PARAM_ERROR PLEASE CHECK SEND PARAM")  ,

    NULL_MODE_ERROR(405,"NULL_MODE_ERROR THE  MODEL IS NULL PLEASE CHECK DATABASE"),

    ;


    public  int code ;
    public String msg ;

    ErrorCode(int code,String msg){

        this.code=code;
        this.msg=msg;
    }



}
