package com.liyi.xlib.util.http;

import android.content.Context;

import com.liyi.xlib.BuildConfig;
import com.liyi.xlib.util.http.interceptor.LoggingInterceptor;
import com.liyi.xlib.util.http.interceptor.OAuthInterceptor;
import com.liyi.xlib.util.http.interceptor.OfflineCacheControlInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * okhttp 配置类
 */
public class OkHttpConfig {
    // 超时时间
    private final int TIMEOUT_CONNECT = 25;
    private final int TIMEOUT_READ = 25;
    private final int TIMEOUT_WRITE = 25;
    // 最大缓存空间
    private final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    // 缓存文件夹的名字
    private final String CACHE_DIR_NAME = "OKCache";

    private OkHttpClient.Builder mHttpClientBuilder;

    public OkHttpConfig(Context context) {
        init(context);
    }

    private void init(Context context) {
        // 缓存目录
        File cacheDir = new File(context.getCacheDir(), CACHE_DIR_NAME);
        Cache cache = new Cache(cacheDir, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);
        mHttpClientBuilder = new OkHttpClient.Builder()
                // 超时时间
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                // 失败重连
                .retryOnConnectionFailure(true)
                // 开放授权
                .addInterceptor(new OAuthInterceptor())
                // 添加缓存
                .addNetworkInterceptor( new OfflineCacheControlInterceptor(context))
                // 设置缓存路径
                .cache(cache);
        if (BuildConfig.DEBUG) {
            // 调试模式下，添加日志打印
            mHttpClientBuilder.addInterceptor(new LoggingInterceptor());
        }
    }

    public OkHttpClient getOkHttpClient() {
        if (mHttpClientBuilder == null) return null;
        return mHttpClientBuilder.build();
    }


    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return mHttpClientBuilder;
    }
}
