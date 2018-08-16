package com.liyi.common.mvp;

import android.support.annotation.NonNull;

import com.liyi.xlib.mvp.BaseXPresenterNet;


public abstract class BasePresenter<V extends IBaseView, T> extends BaseXPresenterNet<V, T> {

    public BasePresenter(@NonNull V view) {
        super(view);
    }
}
