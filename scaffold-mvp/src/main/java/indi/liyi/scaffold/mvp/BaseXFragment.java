package indi.liyi.scaffold.mvp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.support.annotation.RequiresApi;

@RequiresApi(Build.VERSION_CODES.HONEYCOMB)
public abstract class BaseXFragment<P extends IBaseXPresenter> extends Fragment implements IBaseXView {
    private P mPresenter;

    public abstract P onBindPresenter();

    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = onBindPresenter();
        }
        return mPresenter;
    }

    @Override
    public Activity getSelfActivity() {
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
