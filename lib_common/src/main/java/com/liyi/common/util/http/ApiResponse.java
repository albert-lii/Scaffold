package com.liyi.common.util.http;

import android.support.annotation.NonNull;

import com.liyi.xlib.util.http.response.HttpResponse;

/**
 * 请求响应类
 *
 * @param <T>
 */
public class ApiResponse<T> extends HttpResponse<T> {

    public ApiResponse(HttpCallback<T> callback) {
        super(callback);
    }

    public ApiResponse(@NonNull Object tag, HttpCallback<T> callback) {
        super(tag, callback);
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
    }
}
