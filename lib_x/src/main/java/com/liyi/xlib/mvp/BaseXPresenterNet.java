package com.liyi.xlib.mvp;

import android.support.annotation.NonNull;

import com.liyi.xlib.util.http.CallServer;
import com.liyi.xlib.util.http.response.HttpResponseListener;

import java.lang.ref.WeakReference;

/**
 * 对网络请求做处理的 Presenter 基类
 *
 * @param <V>
 * @param <T>
 */
public abstract class BaseXPresenterNet<V extends IBaseXView, T> implements IBaseXPresenterNet, HttpResponseListener<T> {
    /**
     * 防止 Activity 不走 onDestory() 方法，所以采用弱引用来防止内存泄漏
     */
    private WeakReference<V> mViewRef;

    public BaseXPresenterNet(@NonNull V view) {
        attachView(view);
    }

    private void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public V getView() {
        return mViewRef.get();
    }

    @Override
    public boolean isViewAttach() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
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
