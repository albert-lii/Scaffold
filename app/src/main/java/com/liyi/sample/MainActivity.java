package com.liyi.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.liyi.sample.mvvm.LoginAty;
import com.liyi.xlib.util.ScreenAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdapter.match(this,360);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sample1:
                startActivity(new Intent(this, Sample1Activity.class));
                break;
            case R.id.btn_sample2:
                startActivity(new Intent(this, Sample2Activity.class));
                break;
            case R.id.btn_login:
                startActivity(new Intent(this, LoginAty.class));
                break;
        }
    }
}
