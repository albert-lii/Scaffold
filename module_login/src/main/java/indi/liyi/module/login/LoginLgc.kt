package indi.liyi.module.login

import android.text.TextUtils
import indi.liyi.common.utils.http.HttpCallback
import org.json.JSONObject

class LoginLgc {

    fun login(phoneNo: String, password: String, callback: HttpCallback<JSONObject>) {
        if (!TextUtils.isEmpty(phoneNo) && !TextUtils.isEmpty(password)) {
            if (phoneNo == "18013863380" && password == "123456") {
                callback.onSuccess(null, null)
            } else {
                callback.onFailure(null, null)
            }
        }
    }
}