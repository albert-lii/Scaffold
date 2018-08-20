package com.liyi.common.util.http;

import android.support.annotation.NonNull;

import com.liyi.xlib.util.http.response.HttpResponse;
import com.liyi.xlib.util.http.response.HttpResponseListener;

/**
 * 请求响应类
 *
 * @param <T>
 */
public class ApiResponse<T> extends HttpResponse<T> {

    public ApiResponse(HttpResponseListener<T> listener) {
        super(listener);
    }

    public ApiResponse(@NonNull Object tag, HttpResponseListener<T> listener) {
        super(tag, listener);
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
