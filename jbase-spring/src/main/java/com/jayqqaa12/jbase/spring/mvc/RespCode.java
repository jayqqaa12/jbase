package com.jayqqaa12.jbase.spring.mvc;

/**
 * Created by yhj on 17/2/23.
 */
public abstract class RespCode {

    public static final int SUCCESS = 200;  //  | 成功 |


    // 1000-1199 内部异常 服务器相关错误
    public static final int SERVER_ERROR = 1000;  //  | 服务器内部异常 或未指定异常|
    public static final int GATEWAY_ERROR=1100;       // |网关访问异常|
    public static final int DB_SQL_ERROR= 1101; // sql执行异常
    public static final int PARAM_ERROR=1102; //参数异常
    public static final int RETRY_ERROR =1103; // 重试异常
    public static final int RETRY_LOCK_ERROR=1104;// 幂等性异常

    
    // 1300-1399 请求相关异常
    public static final int RESOURCE_NOT_FOUND = 1300;  //  | 请求没有被找到 |
    public static final int REQ_METHOD_NOT_ALLOWED = 1301;  //  | 方法不被允许 |
    public static final int REQ_MEDIA_UNSUPPORTED = 1302;  //  | 不支持的媒体类型 |
    public static final int BAD_REQ = 1303;  //  | BAD REQUEST |
    public static final int REQ_METHOD_GET = 1304;  //  | 请求必须是GET请求 |
    public static final int REQ_METHOD_POST = 1305;  //  | 请求必须是POST请求 |
    public static final int REQ_METHOD_PUT = 1306;  //  | 请求必须是PUT请求 |
    public static final int REQ_METHOD_DELETE = 1307;  //  | 请求必须是DELETE请求 |

    public static final int REQ_JSON_FORMAT_ERROR=1310;// JSON 解析异常


    // 引用外部 SDK 相关错误  1500-1599
    public static final int INNER__CONTENT_SCAN_ERROR = 1500;    //没有配置图片检测类型



    // 1600-1999 内部模块异常

    public static final int AUTH_ERROR=1600;       // |没有权限|
    public static final int AUTH_USER_NOT_FOUND=1601; //用户不存在
    public static final int AUTH_USER_LOGIN_ERROR=1602; //用户名或密码错误

    public static final int AUTH_TOKEN_EXPIRED=1603; //TOKEN 过期
    public static final int AUTH_TOKEN_SIGN_ERROR=1604;//TOKEN 签名异常




    public static final int VALIDATE_CODE_VALIDATE_ERROR =1700;  //验证码错误
    public static final int VALIDATE_CODE_PAST_DUE =1701;         //验证码过期

    public static final int SMS_ERROR=1702; //发送失败

    public static final int SMS_LIMIT=1703;                //超过限制
    public static final int SMS_PHONE_ERROR=1704;//手机号错误



    //2000+ 业务模块自定义异常 继承这个类 最好定义在一个公共模块里防止 冲突
    


    
}
