package indi.liyi.scaffold.mvvm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


public abstract class BaseXActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    public <VM extends ViewModel> VM createViewModel(Class<VM> clz) {
        return ViewModelProviders.of(this).get(clz);
    }
}
