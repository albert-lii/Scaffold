package indi.liyi.scaffold.http.constant;

/**
 * 网络异常错误类型
 */
public class NetworkErrorType {
    /**
     * 未知错误
     */
    public static final int UNKNOWN_ERROR = 1000;
    /**
     * 网络错误
     */
    public static final int HTTP_ERROR = 1001;
    /**
     * 连接失败
     */
    public static final int UNCONNECT_ERROR = 1002;
    /**
     * 证书验证失败
     */
    public static final int SSL_ERROR = 1003;
    /**
     * 数据解析错误
     */
    public static final int PARSE_ERROR = 1004;
}
