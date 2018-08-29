package com.liyi.sample.mvvm;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * 只负责数据的处理，不引入 UI（ViewModel 与 LiveData 都是与View 的生命周期绑定，不会出现内存泄露）
 */
public class LoginVM extends ViewModel implements ApiListener {
    private MutableLiveData<String> mLoginState;

    private LoginLgc mLoginLgc;

    public LoginVM() {
        mLoginLgc = new LoginLgc();
        mLoginState = new MutableLiveData<>();
    }

    public void login(String phoneNo, String password) {
        mLoginLgc.login(phoneNo, password, this);
    }

    @Override
    public void onSuccess() {
        mLoginState.setValue("登录成功");
    }

    @Override
    public void onFailure() {
        mLoginState.setValue("登录失败");
    }

    public MutableLiveData<String> getLoginState() {
        return mLoginState;
    }
}
