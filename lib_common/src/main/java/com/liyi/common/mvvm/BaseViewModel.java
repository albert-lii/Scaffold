package com.liyi.common.mvvm;

import android.arch.lifecycle.ViewModel;

import com.liyi.common.util.http.HttpCallback;
import com.liyi.common.util.http.HttpUtils;
import com.liyi.xlib.util.http.exception.HttpFailure;


public abstract class BaseViewModel<T> extends ViewModel implements HttpCallback<T> {

    /**
     * 取消网络请求
     *
     * @param tag 请求标记
     */
    public void cancel(Object tag) {
        HttpUtils.cancel(tag);
    }

    /**
     * 取消所有的网络请求
     */
    public void cancelAll() {
        HttpUtils.cancelAll();
    }

    @Override
    public void onSuccess(Object tag, T data) {

    }

    @Override
    public void onFailure(Object tag, HttpFailure failure) {

    }
}
