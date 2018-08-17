package com.liyi.xlib.util.http.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 使用拦截器在代码中动态更换 BaseUrl，原理如下：
 * <p>
 * 1. 在请求前拦截 BaseUrl，并更改
 * 2. 自定义 @Header 作为请求标记，根据标记判断应该使用哪个 BaseUrl
 */
public class BaseUrlInterceptor implements Interceptor {
    // Header 自定义字段的 key，用于鉴别当前请求所用的 baseUrl
    private String mUrlKey;
    // baseUrl 存储器
    private Map<String, String> mHostMap;

    public BaseUrlInterceptor(String urlKey, Map<String, String> hostMap) {
        this.mUrlKey = urlKey;
        this.mHostMap = hostMap;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 获取原始的 originalRequest
        Request originalRequest = chain.request();
        // 获取老的 url
        HttpUrl oldUrl = originalRequest.url();
        // 获取 originalRequest 的创建者 builder
        Request.Builder builder = originalRequest.newBuilder();
        // 获取指定的头信息的集合
        List<String> urlnameList = originalRequest.headers(mUrlKey);
        if (urlnameList != null && urlnameList.size() > 0) {
            // 删除原有配置中的值,就是 namesAndValues 集合里的值
            builder.removeHeader(mUrlKey);
            // 获取头信息中配置的 value
            String urlname = urlnameList.get(0);
            HttpUrl baseURL = null;
            // 根据头信息中配置的 value，来匹配新的 baseUrl 地址
            for (Map.Entry<String, String> entry : mHostMap.entrySet()) {
                String key = entry.getKey();
                String host = entry.getValue();
                if (key.equals(urlname)) {
                    baseURL = HttpUrl.parse(host);
                    break;
                }
            }
            // 重建新的 HttpUrl，需要重新设置的 url 部分
            HttpUrl newHttpUrl = oldUrl.newBuilder()
                    // http 协议如：http 或者 https
                    .scheme(baseURL.scheme())
                    // 主机地址
                    .host(baseURL.host())
                    // 端口
                    .port(baseURL.port())
                    .build();
            // 获取处理后的新 newRequest
            Request newRequest = builder.url(newHttpUrl).build();
            return chain.proceed(newRequest);
        } else {
            return chain.proceed(originalRequest);
        }
    }
}
