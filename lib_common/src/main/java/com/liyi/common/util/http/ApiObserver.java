package com.liyi.common.util.http;

import android.support.annotation.NonNull;

import com.liyi.xlib.util.http.response.HttpObserver;
import com.liyi.xlib.util.http.response.HttpResponseListener;

/**
 * 请求响应类
 *
 * @param <T>
 */
public class ApiObserver<T> extends HttpObserver<T> {

    public ApiObserver(HttpResponseListener<T> listener) {
        super(listener);
    }

    public ApiObserver(@NonNull Object tag, HttpResponseListener<T> listener) {
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
