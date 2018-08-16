package com.liyi.xlib.constant;

/**
 * HTTP 状态码
 */
public final class HttpStatusCode {
    /**
     * 错误请求
     */
    public static final int ERROR_REQUEST = 400;
    /**
     * 服务器未授权访问
     */
    public static final int UNAUTHORIZED = 401;
    /**
     * 服务器拒绝请求
     */
    public static final int FORBIDDEN = 403;
    /**
     * 服务器找不到请求的资源
     */
    public static final int NOT_FOUND = 404;
    /**
     * 请求方法不支持
     */
    public static final int METHOD_NOT_SUPPORT = 405;
    /**
     * 请求超时，网络信号不稳定
     */
    public static final int REQUEST_TIMEOUT = 408;
    /**
     * 请求实体过大
     */
    public static final int REQUEST_LARGE = 413;
    /**
     * 请求的URI过长
     */
    public static final int REQUEST_URI_LONG = 414;
    /**
     * 服务器错误
     */
    public static final int SERVER_ERROR = 500;
    /**
     * 网关错误
     */
    public static final int GATEWAY_ERROR = 502;
    /**
     * 服务暂不可用
     */
    public static final int SERVICE_UNAVAILABLE = 503;
    /**
     * 网关超时
     */
    public static final int GATEWAY_TIMEOUT = 504;
    /**
     * HTTP协议版本不支持
     */
    public static final int HTTP_NOT_SUPPORT = 505;
}
