package indi.liyi.scaffold.mvp;


public interface IBaseXPresenterNet extends IBaseXPresenter{

    /**
     * 取消网络请求
     *
     * @param tag 网络请求标记
     */
    void cancel(Object tag);

    /**
     * 取消所有的网络请求
     */
    void cancelAll();
}
