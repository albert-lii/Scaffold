package com.liyi.sample.mvvm;

import android.text.TextUtils;

public class LoginLgc {

    public void login(String phoneNo, String password, ApiListener callback) {
        if (!TextUtils.isEmpty(phoneNo) && !TextUtils.isEmpty(password)) {
            if (phoneNo.equals("18013863380") && password.equals("123456")) {
                callback.onSuccess();
            } else {
                callback.onFailure();
            }
        }
    }
}
