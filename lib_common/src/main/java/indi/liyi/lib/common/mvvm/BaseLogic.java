package indi.liyi.lib.common.mvvm;

import indi.liyi.lib.common.utils.http.ApiResponse;
import indi.liyi.lib.common.utils.http.HttpCallback;
import indi.liyi.lib.common.utils.http.HttpUtils;
import io.reactivex.Observable;


public class BaseLogic {

    /**
     * 发送网络请求
     *
     * @param observable
     * @param callback
     */
    public void sendRequest(Observable observable, HttpCallback callback) {
        HttpUtils.toSubscribe(observable, new ApiResponse(0, callback));
    }

    /**
     * 发送网络请求
     *
     * @param tag        网络请求标记
     * @param observable
     * @param callback
     */
    public void sendRequest(Object tag, Observable observable, HttpCallback callback) {
        HttpUtils.toSubscribe(observable, new ApiResponse(tag, callback));
    }
}
