package com.liyi.xlib.util.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.liyi.xlib.BuildConfig;
import com.liyi.xlib.util.http.interceptor.BaseUrlInterceptor;
import com.liyi.xlib.util.http.interceptor.DownloadInterceptor;
import com.liyi.xlib.util.http.interceptor.LoggingInterceptor;
import com.liyi.xlib.util.http.interceptor.OfflineCacheControlInterceptor;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit2.0 配置类
 */
public class RetrofitClient {
    // 默认的超时时间（25s）
    private static final int DEF_TIMEOUT_CONNECT = 25;
    private static final int DEF_TIMEOUT_READ = 25;
    private static final int DEF_TIMEOUT_WRITE = 25;
    // 默认的最大缓存空间（10M）
    private static final int DEF_CACHE_MAX_SIZE = 10 * 1024 * 1024;
    // 默认的缓存文件夹的名字
    private static final String DEF_CACHE_DIR_NAME = "ReClientCache";

    // 域名
    private String mBaseUrl;
    // 多个域名时，在 Header 中设置的标识 Key
    private String mUrlKey;
    // 用于具有多个 BaseUrl 时，在代码中动态更改 BaseUrl
    private Map<String, String> mHostMap;
    // 下载文件时，多个不同 OnProgressListener，在 Header 中设置的标识 Key
    private String mDownloadListenerKey;
    private Retrofit mRetrofit;
    private Context mContext;

    public RetrofitClient(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * 设置域名
     *
     * @param url
     */
    public void setBaseUrl(String url) {
        this.mBaseUrl = url;
    }

    /**
     * 设置多个域名
     *
     * @param urlKey
     * @param hostMap
     */
    public void setBaseUrls(String urlKey, Map<String, String> hostMap) {
        this.mUrlKey = urlKey;
        this.mHostMap = hostMap;
    }

    /**
     * 设置下载 OnProgressListener 的标识的 key
     *
     * @param downloadListenerKey
     */
    public void setDownloadProgressListenerKey(String downloadListenerKey) {
        this.mDownloadListenerKey = downloadListenerKey;
    }

    /**
     * 设置 Retrofit
     *
     * @param retrofit
     */
    public void setRetrofit(Retrofit retrofit) {
        this.mRetrofit = retrofit;
    }

    /**
     * 获取 Retrofit
     *
     * @return
     */
    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = getDefaultRetrofit();
        }
        return mRetrofit;
    }

    /**
     * 创建服务
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T createService(@NonNull Class<T> cls) {
        return getRetrofit().create(cls);
    }

    /**
     * 获取默认的 Retrofit
     */
    private Retrofit getDefaultRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(createOkHttpClient())
                // 添加 String 转换器
//                .addConverterFactory(ScalarsConverterFactory.create())
                // 添加 Gson 转化器
                .addConverterFactory(GsonConverterFactory.create())
                // 配合 RxJava2 使用
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 创建默认的 OkHttpClient
     */
    private OkHttpClient createOkHttpClient() {
        // 缓存目录
        File cacheDir = new File(mContext.getCacheDir(), DEF_CACHE_DIR_NAME);
        Cache cache = new Cache(cacheDir, DEF_CACHE_MAX_SIZE);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                // 超时时间
                .connectTimeout(DEF_TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(DEF_TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(DEF_TIMEOUT_WRITE, TimeUnit.SECONDS)
                // 失败重连
                .retryOnConnectionFailure(true)
                // 添加离线缓存
                .addNetworkInterceptor(new OfflineCacheControlInterceptor(mContext))
                // 设置缓存路径
                .cache(cache);
        // 当有多个域名需求时添加
        if (mHostMap != null && !mHostMap.isEmpty()) {
            builder.addInterceptor(new BaseUrlInterceptor(mUrlKey, mHostMap));
        }
        if (!TextUtils.isEmpty(mDownloadListenerKey)) {
            builder.addInterceptor(new DownloadInterceptor(mDownloadListenerKey));
        }
        // 调试模式下，添加日志打印
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LoggingInterceptor());
        }
        return builder.build();
    }
}
