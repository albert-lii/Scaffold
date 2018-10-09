package indi.liyi.scaffold.mvvm;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 用于和 ViewPager 结合使用时的懒加载模式
 */
public abstract class BaseXLazyFragment extends Fragment {
    private View mContentView;
    // 当前 Fragment 是否可见
    private boolean isVisible = false;
    // 是否与 View 建立起映射关系
    private boolean isInitView = false;
    // 是否是第一次加载数据
    public boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免重复加载 UI
        if (mContentView == null) {
            mContentView = inflater.inflate(getLayoutId(), container, false);
        }
        ViewGroup parent = (ViewGroup) mContentView.getParent();
        if (parent != null) {
            parent.removeView(mContentView);
        }
        initView();
        isInitView = true;
        lazyLoadData();
        return mContentView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    public <VM extends ViewModel> VM createViewModel(Class<VM> clz) {
        return ViewModelProviders.of(this).get(clz);
    }

    public View getContentView() {
        return mContentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 懒加载，针对 ViewPager 的预加载
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();
        } else {
            isVisible = false;
        }
    }

    private void lazyLoadData() {
        if (!isFirstLoad || !isVisible || !isInitView) return;
        initData();
        isFirstLoad = false;
    }
}
