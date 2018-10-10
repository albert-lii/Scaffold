package indi.liyi.module.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import indi.liyi.common.mvvm.BaseActivity
import kotlinx.android.synthetic.main.aty_login.*

class LoginAty : BaseActivity() {
    private lateinit var mLoginVm: LoginVm;

    override fun getLayoutId(): Int {
        return R.layout.aty_login
    }

    override fun initView() {
        mLoginVm = LoginVm()
        // 获取 ViewModel 的实例
        mLoginVm = ViewModelProviders.of(this).get<LoginVm>(LoginVm::class.java)
        // 动态观察数据变化，绑定 UI
        mLoginVm.getLoginState().observe(this, Observer<String> { s -> tv_hint.setText(s) })

        btn_login.setOnClickListener { mLoginVm.login(et_phoneNo.text.toString(), et_password.text.toString()) }
    }
}