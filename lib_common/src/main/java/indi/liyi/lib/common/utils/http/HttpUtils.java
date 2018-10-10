package indi.liyi.lib.common.utils.http;

import android.support.annotation.NonNull;

import indi.liyi.lib.common.constant.ApiService;
import indi.liyi.lib.common.utils.Utils;

import indi.liyi.scaffold.http.HttpServer;
import io.reactivex.Observable;

public class HttpUtils {
    private static ApiService sApiService;

    private static void checkNotNull() {
        if (sApiService == null) {
            HttpServer.getInstance().init(Utils.getApp(), "");
            sApiService = HttpServer.getInstance().createService(ApiService.class);
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
        HttpServer.getInstance().toSubscribe(observable, observer);
    }

    /**
     * 取消指定的请求
     *
     * @param tag
     */
    public static void cancel(@NonNull Object tag) {
        HttpServer.getInstance().cancel(tag);
    }

    /**
     * 取消全部的请求
     */
    public static void cancelAll() {
        HttpServer.getInstance().cancelAll();
    }
}
