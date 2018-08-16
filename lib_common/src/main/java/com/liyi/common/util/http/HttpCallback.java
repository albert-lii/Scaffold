package com.liyi.common.util.http;

import com.liyi.xlib.util.http.response.HttpResponseListener;

/**
 * 网络请求回调
 *
 * @param <T>
 */
public interface HttpCallback<T> extends HttpResponseListener<T> {

}
