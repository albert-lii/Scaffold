package com.liyi.xlib.mvp;

import android.support.annotation.NonNull;

import com.liyi.xlib.util.http.CallServer;
import com.liyi.xlib.util.http.response.HttpResponseListener;

/**
 * 对网络请求做处理的 Presenter 基类
 *
 * @param <V>
 * @param <T>
 */
public abstract class BaseXPresenterNet<V extends IBaseXView, T> extends BaseXPresenter<V> implements IBaseXPresenterNet, HttpResponseListener<T> {

    public BaseXPresenterNet(@NonNull V view) {
        super(view);
    }

    @Override
    public void cancel(@NonNull Object tag) {
        CallServer.getInstance().cancel(tag);
    }

    @Override
    public void cancelAll() {
        CallServer.getInstance().cancelAll();
    }
}
