package com.liyi.xlib.util.http.response;

import android.support.annotation.NonNull;

import com.liyi.xlib.util.http.exception.ExceptionHandler;

import io.reactivex.observers.DisposableObserver;

/**
 * Retrofit2.0 中的网络请求响应基类
 */
public class HttpResponse<T> extends DisposableObserver<T> {
    // 请求标记
    private Object mTag;
    // 网路请求响应监听
    private HttpResponseListener<T> mResponseListener;

    public HttpResponse(HttpResponseListener<T> listener) {
        this.mResponseListener = listener;
    }

    public HttpResponse(@NonNull Object tag, HttpResponseListener<T> listener) {
        this.mTag = tag;
        this.mResponseListener = listener;
    }

    /**
     * 设置请求标记
     *
     * @param tag
     */
    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public Object getTag() {
        return mTag;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

//    @Override
//    public void onSubscribe(Disposable d) {
//        this.mDisposable = d;
//    }

    @Override
    public void onNext(T t) {
        if (mResponseListener != null) {
            mResponseListener.onSuccess(mTag, t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mResponseListener != null) {
            mResponseListener.onFailure(mTag, ExceptionHandler.parseError(e));
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 取消请求
     */
    public void cancel() {
        // 如果处于订阅状态，则取消订阅
        if (!isDisposed()) {
            dispose();
        }
    }
}
