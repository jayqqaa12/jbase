package com.jayqqaa12.jbase.spring.mvc;

/**
 * Created by yhj on 17/2/23.
 */
public abstract class RespCode {

    public static final int SUCCESS = 200;  //  | 成功 |


    
    /**
     * 认证相关
     */
    public static final int AUTH_FAIL = 210;  //  | 用户认证失败 |

    public static final int AUTH_TIMEOUT = 211;  //  | 用户认证超时 |
    public static final int AUTH_NOT_EXIST = 212;  //  | 用户不存在 |
    public static final int AUTH_PASSWORD = 213;  //  | 用户秘钥认证失败 |
    public static final int AUTH_NOT_AUTH = 214;  //  | 用户没有认证 |
    public static final int AUTO_NOT_REG = 215; //  用户手机没有注册


    public static final int AUTH_MSG_CODE_NEED = 221; //  | 需要短信验证码|
    public static final int AUTH_MSG_CODE_TIMEOUT = 222; //  | 短信验证码超时|
    public static final int AUTH_MSG_CODE_FAIL = 223; //  | 短信验证码失败 |

    public static final int AUTH_IMG_CODE_NEED = 224;//需要图片验证码
    public static final int AUTH_IMG_CODE_TIMEOUT = 225;//图片验证码超时
    public static final int AUTH_IMG_CODE_FAIL = 226;//图片验证码失败

    public static final int AUTH_GOO_CODE_NEED = 227;//需要谷歌验证码
    public static final int AUTH_GOO_CODE_TIMEOUT = 228;//谷歌验证码超时
    public static final int AUTH_GOO_CODE_FAIL = 229;//谷歌验证码失败


    /**
     * 权限相关
     */
    public static final int PERMISSION_DENY = 251;  //  | 没有访问权限 |
    public static final int PERMISSION_REQ = 252;  //  | 请联系管理员分配 |


    /**
     * 请求参数相关
     */


    public static final int REQ_MEDIA_UNSUPPORTED = 311;  //  | 不支持的媒体类型 |
    public static final int REQ_MEDIA_JSON = 312;  //  | 请求参数格式需要为application/json |

    public static final int REQ_METHOD_NOT_ALLOWED = 301;  //  | 方法不被允许 |
    public static final int REQ_METHOD_ERROR = 302;  //  | 请求方法错误 |
    public static final int REQ_METHOD_HTTP = 303;  //  | 请求必须是HTTP请求 |
    public static final int REQ_METHOD_GET = 304;  //  | 请求必须是GET请求 |
    public static final int REQ_METHOD_POST = 305;  //  | 请求必须是POST请求 |
    public static final int REQ_METHOD_PUT = 306;  //  | 请求必须是PUT请求 |
    public static final int REQ_METHOD_DELETE = 307;  //  | 请求必须是DELETE请求 |

    public static final int PARAM_ERROR = 320;  //  | 参数错误 |
    public static final int PARAM_TYPE_NUM = 321;  //  | 参数必须为数字 |
    public static final int PARAM_NUM_DOUBLE = 322;  //  | 参数为数字且必须是小数 |
    public static final int PARAM_NUM_INT = 323;  //  | 参数为字符串且必须是整数 |
    public static final int PARAM_NUM_FORMAT = 324;  //  | 参数为字符串且必须符合特定格式 |

    public static final int PARAM_TYPE_STRING = 340;  //  | 参数必须为字符串 |
    public static final int PARAM_STR_NOT_EMPTY = 341;  //  | 字符串不能为空 |
    public static final int PARAM_STR_TOO_LONG = 342;  //  | 参数为字符串且长度太长 |
    public static final int PARAM_STR_CH = 345;  //  | 参数为字符串且需要为中文 |
    public static final int PARAM_STR_EN = 346;  //  | 参数为字符串且需要为英文 |
    public static final int PARAM_STR_NUMBER = 347;  //  | 参数为字符串且需要为数字 |
    public static final int PARAM_STR_UNSPECIAL = 348;  //  | 参数为字符串且需要不能包含特殊字符 |
    public static final int PARAM_STR_EMAIL = 351;  //  | 参数为字符串且需要为邮箱 |
    public static final int PARAM_STR_IDCARD = 352;  //  | 参数为字符串且需要为身份证 |

    public static final int PARAM_TYPE_DATE = 360;  //  | 参数必须为时间类型 |
    public static final int PARAM_DATE_TIME = 362;  //  | 参数必须为时间类型(HH:mm:ss) |
    public static final int PARAM_DATE_DATE = 364;  //  | 参数必须为日期类型(yyyy-MM-dd) |
    public static final int PARAM_DATE_DATETIME = 366;  //  | 参数必须为长时间类型(yyyy-MM-dd HH:mm:ss) |

    /**
     * 资源相关
     */

    public static final int RESOURCE_ERROR = 400;  //  | 请求错误 |
    public static final int RESOURCE_NOT_FOUND = 404;  //  | 请求没有被找到 |


    /**
     * 服务器相关
     */

    public static final int SERVER_ERROR = 500;  //  | 服务器内部异常 |
    public static final int SERVER_ERROR_CPU = 501;  //  | 服务器CPU异常 |
    public static final int SERVER_ERROR_MEM = 502;  //  | 服务器MEM异常 |
    public static final int SERVER_ERROR_DISK = 503;  //  | 服务器DISK异常 |
    public static final int SERVER_DB_ERROR = 510;  //  | 数据库请求异常 |
    public static final int SERVER_DB_UNCONN = 511;  //  | 数据库连接不可用 |
    public static final int SERVER_DB_TIMEOUT = 512;  //  | 数据库请求超时 |
    public static final int SERVER_MD_ERROR = 520;  //  | 中间件异常 |
    public static final int SERVER_MD_ZOO = 521;  //  | 中间件异常(ZOOKEPER) |
    public static final int SERVER_MD_MQ = 522;  //  | 中间件异常(MQ) |
    public static final int SERVER_MD_LOCK = 523;  //  | 中间件异常(LOCK) |


    /**
     * 内部服务器异常.
     */

    public static final int INNER_RESULT_ERROR = 610; // 返回数据异常.
    public static final int INNER_RESULT_JSON_ERROR = 611; // 返回JSON解析异常.
    public static final int INNER_RESULT_CODE_ERROR = 612; // 返回Code 不存在.

    public static final int INNER_RESULT_DATA_ERROR = 630;    //返回data异常.
    public static final int INNER_JSON_ERROR = 631;    //返回data不存在

    public static final int INNER_CHECK_LIST_ERROR = 640;    //commentMongo中要检查的List不存在
    public static final int INNER__CONTENT_PICTURE_GREEN_ERROR = 660;    //没有配置图片检测类型
    public static final int INNER__CONTENT_SCAN_ERROR = 661;    //没有配置图片检测类型
}
