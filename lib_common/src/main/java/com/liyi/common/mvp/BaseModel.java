package com.liyi.common.mvp;

import android.support.annotation.NonNull;

import com.liyi.common.util.http.ApiResponse;
import com.liyi.common.util.http.HttpCallback;
import com.liyi.xlib.mvp.BaseXModelNet;

import io.reactivex.Observable;


public abstract class BaseModel extends BaseXModelNet {

    /**
     * 发送网络请求
     *
     * @param observable
     * @param callback
     * @param <T>
     */
    protected <T> void sendRequest(@NonNull Observable<T> observable, HttpCallback<T> callback) {
        sendRequest(0, observable, callback);
    }

    /**
     * 发送网络请求
     *
     * @param tag
     * @param observable
     * @param callback
     * @param <T>
     */
    private <T> void sendRequest(@NonNull Object tag, @NonNull Observable<T> observable, HttpCallback callback) {
        sendRequest(tag, observable, new ApiResponse<T>(callback));
    }
}
