package com.liyi.xlib.ui.adapter;


import android.support.annotation.IdRes;
import android.view.View;


public class BaseXHolder {
    public View convertView;

    public BaseXHolder(final View convertView) {
        this.convertView = convertView;
    }

    public View getConvertView() {
        return convertView;
    }

    public <T extends View> T findViewById(@IdRes int viewId) {
        T view = convertView.findViewById(viewId);
        return view;
    }
}
