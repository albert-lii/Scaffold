package com.liyi.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.liyi.xlib.util.ScreenAdapter;

public class Sample1Activity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ScreenAdapter.cancelMatch(this);
    }
}