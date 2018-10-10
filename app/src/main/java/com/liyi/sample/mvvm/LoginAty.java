package com.liyi.sample.mvvm;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liyi.sample.R;

import indi.liyi.scaffold.utils.util.ScreenAdapter;

public class LoginAty extends FragmentActivity {
    private EditText et_phoneNo, et_password;
    private Button btn_login;
    private TextView tv_hint;

    private LoginVM mLoginVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdapter.cancelMatch(this);
        setContentView(R.layout.activity_login);
        et_phoneNo = findViewById(R.id.et_phoneNo);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_hint = findViewById(R.id.tv_hint);

        // 获取 ViewModel 的实例
        mLoginVM = ViewModelProviders.of(this).get(LoginVM.class);
        // 动态观察数据变化，绑定 UI
        mLoginVM.getLoginState().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_hint.setText(s);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginVM.login(et_phoneNo.getText().toString(), et_password.getText().toString());
            }
        });
    }
}
