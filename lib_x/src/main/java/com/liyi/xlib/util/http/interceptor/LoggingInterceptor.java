package com.liyi.xlib.util.http.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 日志拦截器
 */
public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 这个 chain 里面包含了 request 和 response，所以你要什么都可以从这里拿
        // =========== 发送 ===========
        Request originalRequest = chain.request();
        // 请求发起的时间
        long requestTime = System.currentTimeMillis();
        HttpUrl requestUrl = originalRequest.url();
        Connection requestConnection = chain.connection();
        Headers requestHeaders = originalRequest.headers();
        // 打印发送信息
        Log.d(this.getClass().getSimpleName(), "===LoggingInterceptor===发送==requestUrl= " + requestUrl);
        Log.d(this.getClass().getSimpleName(), "===LoggingInterceptor===发送==requestConnection= " + requestConnection);
        Log.d(this.getClass().getSimpleName(), "===LoggingInterceptor===发送==requestHeaders= " + requestHeaders);

        // =========== 接收 ===========
        // 收到响应的时间
        long responseTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
//        ResponseBody responseBody = response.peekBody(1024 * 1024);
        HttpUrl responseUrl = response.request().url();
        Headers responseHeaders = response.headers();
        String content = response.body().string();
        // 延迟时间
        long delayTime = responseTime - requestTime;
        // 打印接收信息
        Log.d(this.getClass().getSimpleName(), "=====LoggingInterceptor===接收==responseUrl= " + responseUrl);
        Log.d(this.getClass().getSimpleName(), "=====LoggingInterceptor===接收==responseHeaders= " + responseHeaders);
        Log.d(this.getClass().getSimpleName(), "=====LoggingInterceptor===接收==delayTime= " + delayTime);
        Log.d(this.getClass().getSimpleName(), "=====LoggingInterceptor===接收==content= " + content);
        return response;
    }
}
