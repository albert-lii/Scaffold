package com.liyi.xlib.util.http.response;


import com.liyi.xlib.util.http.CallServer;

/**
 * Retrofit2.0 中的下载请求响应基类
 */
public class HttpDownloadResponse<T> extends HttpResponse<T> {
    private OnProgressListener mProgressListener;

    /**
     * @param listener         响应回调
     * @param progressListener 进度监听
     * @param listenerTag      必须与 @Header 中设置的值对应，例如 Header 配置为 @Header("downloadKey","apk")，则 listenerTag="apk"
     */
    public HttpDownloadResponse(HttpResponseListener<T> listener, OnProgressListener progressListener, String listenerTag) {
        super(listener);
        this.mProgressListener = progressListener;
        if (mProgressListener != null) {
            CallServer.getInstance().addDownloadProgressListener(listenerTag, progressListener);
        }
    }
}
