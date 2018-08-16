package com.liyi.sample;

import android.app.Application;

import com.liyi.xlib.util.ScreenAdapter;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenAdapter.setup(this);
        ScreenAdapter.register(this, 720, ScreenAdapter.MATCH_BASE_WIDTH, ScreenAdapter.MATCH_UNIT_DP);
    }
}
