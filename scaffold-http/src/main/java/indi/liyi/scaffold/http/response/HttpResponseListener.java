package indi.liyi.scaffold.http.response;

import indi.liyi.scaffold.http.exception.HttpFailure;

/**
 * 网络请求回调响应接口
 */
public interface HttpResponseListener<T> {
    /**
     * 网络请求成功
     *
     * @param tag  请求的标记
     * @param data 返回的数据
     */
    void onSuccess(Object tag, T data);

    /**
     * 网络请求失败
     *
     * @param tag     请求的标记
     * @param failure 请求失败时，返回的信息类
     */
    void onFailure(Object tag, HttpFailure failure);
}
