package indi.liyi.scaffold.http.exception;

import android.content.Context;
import android.net.ParseException;
import android.support.annotation.NonNull;

import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import indi.liyi.scaffold.http.R;
import indi.liyi.scaffold.http.constant.HttpStatusCode;
import indi.liyi.scaffold.http.constant.NetworkErrorType;
import indi.liyi.scaffold.utils.util.LogUtil;
import retrofit2.HttpException;

/**
 * 网络请求异常处理类
 */
public class ExceptionHandler {

    public static HttpFailure parseError(@NonNull Context context, Throwable e) {
        HttpFailure failure = parseError(e);
        failure.setErrorMsg(failure.getErrorMsg(context));
        return failure;
    }

    /**
     * 异常解析
     *
     * @param e
     * @return {@link HttpFailure}
     */
    public static HttpFailure parseError(Throwable e) {
        HttpFailure failure = new HttpFailure();
        failure.setThrowable(e);
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int errorMsgResId;
            switch (httpException.code()) {
                case HttpStatusCode.ERROR_REQUEST:
                    errorMsgResId = R.string.scaffold_http_status_err_request_error;
                    break;
                case HttpStatusCode.UNAUTHORIZED:
                    errorMsgResId = R.string.scaffold_http_status_err_unauthorized;
                    break;
                case HttpStatusCode.FORBIDDEN:
                    errorMsgResId = R.string.scaffold_http_status_err_forbidden;
                    break;
                case HttpStatusCode.NOT_FOUND:
                    errorMsgResId = R.string.scaffold_http_status_err_not_found;
                    break;
                case HttpStatusCode.METHOD_NOT_SUPPORT:
                    errorMsgResId = R.string.scaffold_http_status_err_method_not_support;
                    break;
                case HttpStatusCode.REQUEST_TIMEOUT:
                    errorMsgResId = R.string.scaffold_http_status_err_timeout;
                    break;
                case HttpStatusCode.REQUEST_LARGE:
                    errorMsgResId = R.string.scaffold_http_status_err_request_large;
                    break;
                case HttpStatusCode.REQUEST_URI_LONG:
                    errorMsgResId = R.string.scaffold_http_status_err_uri_long;
                    break;
                case HttpStatusCode.SERVER_ERROR:
                    errorMsgResId = R.string.scaffold_http_status_err_server_error;
                    break;
                case HttpStatusCode.GATEWAY_ERROR:
                    errorMsgResId = R.string.scaffold_http_status_err_gateway_error;
                    break;
                case HttpStatusCode.SERVICE_UNAVAILABLE:
                    errorMsgResId = R.string.scaffold_http_status_err_service_unavailable;
                    break;
                case HttpStatusCode.GATEWAY_TIMEOUT:
                    errorMsgResId = R.string.scaffold_http_status_err_gateway_timeout;
                    break;
                case HttpStatusCode.HTTP_NOT_SUPPORT:
                    errorMsgResId = R.string.scaffold_http_status_err_http_not_support;
                    break;
                default:
                    errorMsgResId = R.string.scaffold_http_status_err_error;
            }
            failure.setErrorCode(httpException.code());
            failure.setErrorMsgId(errorMsgResId);
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
            failure.setErrorCode(NetworkErrorType.UNCONNECT_ERROR);
            failure.setErrorMsgId(R.string.scaffold_http_network_err_unconnect);
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            failure.setErrorCode(NetworkErrorType.SSL_ERROR);
            failure.setErrorMsgId(R.string.scaffold_http_network_err_ssl_error);
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            failure.setErrorCode(NetworkErrorType.PARSE_ERROR);
            failure.setErrorMsgId(R.string.scaffold_http_network_err_parse_error);
        } else {
            failure.setErrorCode(NetworkErrorType.UNKNOWN_ERROR);
            failure.setErrorMsgId(R.string.scaffold_http_network_err_unknown);
        }
        LogUtil.e("Scaffold-ExceptionHandler", "Http Error Code ======> " + failure.getErrorCode());
        return failure;
    }
}
