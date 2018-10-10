package indi.liyi.module.login

import android.arch.lifecycle.MutableLiveData
import indi.liyi.common.mvvm.BaseViewModelNet
import indi.liyi.scaffold.http.exception.HttpFailure
import org.json.JSONObject

/**
 * 只负责数据的处理，不引入 UI（ViewModel 与 LiveData 都是与View 的生命周期绑定，不会出现内存泄露）
 */
class LoginVm : BaseViewModelNet<JSONObject> {
    private var mLoginState: MutableLiveData<String>
    private var mLoginLgc: LoginLgc

    constructor() {
        mLoginState = MutableLiveData()
        mLoginLgc = LoginLgc()
    }

    fun login(phoneNo: String, password: String) {
        mLoginLgc.login(phoneNo, password, this)
    }

    override fun onSuccess(tag: Any?, data: JSONObject?) {
        super.onSuccess(tag, data)
        mLoginState.value = "登录成功"
    }

    override fun onFailure(tag: Any?, failure: HttpFailure?) {
        super.onFailure(tag, failure)
        mLoginState.value = "登录失败"
    }

    fun getLoginState(): MutableLiveData<String> {
        return mLoginState
    }
}