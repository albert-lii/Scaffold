package indi.liyi.scaffold.http.interceptor;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import indi.liyi.scaffold.http.HttpServer;
import indi.liyi.scaffold.http.response.DownloadResponseBody;
import indi.liyi.scaffold.http.response.OnProgressListener;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 带进度的下载拦截器
 */
public class DownloadInterceptor implements Interceptor {
    private String mListenerKey;

    public DownloadInterceptor(String key) {
        this.mListenerKey = key;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Response response = chain.proceed(originalRequest);
        // 判断是否是下载响应体
        if (response.body() instanceof DownloadResponseBody) {
            // 获取指定的头信息的集合
            List<String> listenerTagList = originalRequest.headers(mListenerKey);
            if (listenerTagList != null && listenerTagList.size() > 0) {
                // 获取头信息中配置的 value
                String listenerTag = listenerTagList.get(0);
                // 获取监听器集合
                HashMap<Object, OnProgressListener> listenerSite = HttpServer.getInstance().getDownloadProgressListenerSite();
                if (listenerSite != null && !listenerSite.isEmpty()) {
                    OnProgressListener progressListener = listenerSite.get(listenerTag);
                    if (progressListener != null) {
                        return response.newBuilder().body(
                                new DownloadResponseBody(response.body(), progressListener)).build();
                    }
                }
            }
        }
        return response;
    }
}
