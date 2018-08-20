package com.liyi.common.util.http;

import android.support.annotation.NonNull;

import com.liyi.common.constant.ApiService;
import com.liyi.common.util.Utils;
import com.liyi.xlib.util.http.CallServer;

import io.reactivex.Observable;

public class HttpUtils {
    private static ApiService sApiService;

    private static void checkNotNull() {
        if (sApiService == null) {
            CallServer.getInstance().init(Utils.getApp(), "");
            sApiService = CallServer.getInstance().createService(ApiService.class);
        }
    }

    public static ApiService getApi() {
        checkNotNull();
        return sApiService;
    }

    /**
     * 发送网络请求
     *
     * @param observable
     * @param callback
     * @param <T>
     */
    public static <T> void toSubscribe(Observable<T> observable, HttpCallback<T> callback) {
        toSubscribe(observable, new ApiResponse<T>(callback));
    }


    /**
     * 发送网络请求
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void toSubscribe(Observable<T> observable, ApiResponse<T> observer) {
        CallServer.getInstance().toSubscribe(observable, observer);
    }

    /**
     * 取消指定的请求
     *
     * @param tag
     */
    public static void cancel(@NonNull Object tag) {
        CallServer.getInstance().cancel(tag);
    }

    /**
     * 取消全部的请求
     */
    public static void cancelAll() {
        CallServer.getInstance().cancelAll();
    }
}
