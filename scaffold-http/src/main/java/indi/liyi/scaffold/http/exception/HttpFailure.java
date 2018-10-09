package indi.liyi.scaffold.http.exception;

import android.content.Context;
import android.text.TextUtils;

import io.reactivex.annotations.NonNull;

/**
 * 网络请求异常类
 */
public class HttpFailure {
    // 请求错误码
    private int errorCode;
    // 请求错误信息资源 id
    private int errorMsgId;
    // 请求错误信息
    private String errorMsg;
    // 抛出的异常
    private Throwable throwable;


    public HttpFailure() {

    }

    public HttpFailure(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public HttpFailure(int errorCode, int errorMsgId) {
        this.errorCode = errorCode;
        this.errorMsgId = errorMsgId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorMsgId() {
        return errorMsgId;
    }

    public void setErrorMsgId(int errorMsgId) {
        this.errorMsgId = errorMsgId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getErrorMsg(@NonNull Context context) {
        if (!TextUtils.isEmpty(errorMsg)) {
            return errorMsg;
        }
        if (errorMsgId != 0) {
            return context.getResources().getString(errorMsgId);
        }
        return null;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
