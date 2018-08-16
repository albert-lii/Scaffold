package com.liyi.xlib.mvp;

import android.support.annotation.NonNull;

import com.liyi.xlib.util.http.CallServer;
import com.liyi.xlib.util.http.response.HttpObserver;

import io.reactivex.Observable;

/**
 * 对网络请求做处理的 Model 基类
 */
public class BaseXModelNet {

    /**
     * 发送网络请求
     *
     * @param observable 被观察者
     * @param observer   观察者
     * @param <T>
     */
    protected <T> void sendRequest(@NonNull Observable<T> observable, @NonNull HttpObserver<T> observer) {
        CallServer.getInstance().toSubscribe(observable, observer);
    }

    /**
     * 发送网络请求
     *
     * @param tag        请求标记
     * @param observable 被观察者
     * @param observer   观察者
     * @param <T>
     */
    protected <T> void sendRequest(@NonNull Object tag, @NonNull Observable<T> observable, @NonNull HttpObserver<T> observer) {
        observer.setTag(tag);
        CallServer.getInstance().toSubscribe(observable, observer);
    }
}
