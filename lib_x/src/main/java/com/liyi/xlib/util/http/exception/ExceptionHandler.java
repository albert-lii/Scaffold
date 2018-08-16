package com.liyi.xlib.util.http.exception;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.liyi.xlib.R;
import com.liyi.xlib.constant.HttpStatusCode;
import com.liyi.xlib.constant.NetworkErrorType;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * 网络请求异常处理类
 */
public class ExceptionHandler {

    /**
     * 异常解析
     *
     * @param e
     * @return
     */
    public static HttpFailure parseError(Throwable e) {
        HttpFailure failure = new HttpFailure();
        failure.setThrowable(e);
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int errorMsgResId;
            switch (httpException.code()) {
                case HttpStatusCode.ERROR_REQUEST:
                    errorMsgResId = R.string.network_error_request;
                    break;
                case HttpStatusCode.UNAUTHORIZED:
                    errorMsgResId = R.string.network_unauthorized;
                    break;
                case HttpStatusCode.FORBIDDEN:
                    errorMsgResId = R.string.network_forbidden;
                    break;
                case HttpStatusCode.NOT_FOUND:
                    errorMsgResId = R.string.network_not_found;
                    break;
                case HttpStatusCode.METHOD_NOT_SUPPORT:
                    errorMsgResId = R.string.network_method_not_support;
                    break;
                case HttpStatusCode.REQUEST_TIMEOUT:
                    errorMsgResId = R.string.network_timeout;
                    break;
                case HttpStatusCode.REQUEST_LARGE:
                    errorMsgResId = R.string.network_request_large;
                    break;
                case HttpStatusCode.REQUEST_URI_LONG:
                    errorMsgResId = R.string.network_uri_long;
                    break;
                case HttpStatusCode.SERVER_ERROR:
                    errorMsgResId = R.string.network_server_error;
                    break;
                case HttpStatusCode.GATEWAY_ERROR:
                    errorMsgResId = R.string.network_gateway_error;
                    break;
                case HttpStatusCode.SERVICE_UNAVAILABLE:
                    errorMsgResId = R.string.network_service_unavailable;
                    break;
                case HttpStatusCode.GATEWAY_TIMEOUT:
                    errorMsgResId = R.string.network_gateway_timeout;
                    break;
                case HttpStatusCode.HTTP_NOT_SUPPORT:
                    errorMsgResId = R.string.network_http_not_support;
                    break;
                default:
                    errorMsgResId = R.string.network_error;
            }
            failure.setErrorCode(httpException.code());
            failure.setErrorMsgId(errorMsgResId);
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
            failure.setErrorCode(NetworkErrorType.UNCONNECT_ERROR);
            failure.setErrorMsgId(R.string.unconnect_error);
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            failure.setErrorCode(NetworkErrorType.SSL_ERROR);
            failure.setErrorMsgId(R.string.ssl_error);
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            failure.setErrorCode(NetworkErrorType.PARSE_ERROR);
            failure.setErrorMsgId(R.string.parse_error);
        } else {
            failure.setErrorCode(NetworkErrorType.UNKNOWN_ERROR);
            failure.setErrorMsgId(R.string.unknown_error);
        }
        return failure;
    }
}
