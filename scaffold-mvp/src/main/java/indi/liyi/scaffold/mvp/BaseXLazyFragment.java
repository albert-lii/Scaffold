package indi.liyi.scaffold.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseXLazyFragment<P extends IBaseXPresenter> extends Fragment implements IBaseXView {
    private View mContentView;
    // 当前 Fragment 是否可见
    private boolean isVisible = false;
    // 是否与 View 建立起映射关系
    private boolean isInitView = false;
    // 是否是第一次加载数据
    public boolean isFirstLoad = true;

    private P mPresenter;

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

    public abstract P onBindPresenter();

    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = onBindPresenter();
        }
        return mPresenter;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

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

    @Override
    public FragmentActivity getSelfActivity() {
        return getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * 在生命周期结束时，将 presenter 与 view 之间的联系断开，防止出现内存泄露
         */
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
