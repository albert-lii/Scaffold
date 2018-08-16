package com.liyi.xlib.util.http;

import android.content.Context;

import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit2.0 配置类
 */
public class RetrofitClient {

    public RetrofitClient() {

    }

    /**
     * 使用默认的 OkHttpClient
     *
     * @param context
     * @return
     */
    public Retrofit.Builder getRetrofitBuilder(@NonNull Context context) {
        final Retrofit.Builder builder = new Retrofit.Builder()
                // 设置 OkHttpClient
                .client(new OkHttpConfig(context.getApplicationContext()).getOkHttpClient());
        return builder;
    }

    /**
     * 使用自定义 OkHttpClient
     *
     * @param okHttpClient
     * @return
     */
    public Retrofit.Builder getRetrofitBuilder(@NonNull OkHttpClient okHttpClient) {
        final Retrofit.Builder builder = new Retrofit.Builder()
                // 设置 OkHttpClient
                .client(okHttpClient);
        return builder;
    }

    private Retrofit.Builder configRetrofitBuilder(Retrofit.Builder builder) {
        // 添加 String 转换器
        builder.addConverterFactory(ScalarsConverterFactory.create())
                // 添加 Gson 转化器
                .addConverterFactory(GsonConverterFactory.create())
                // 配合 RxJava2 使用
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder;
    }

    public Retrofit getRetrofit(@NonNull Context context, String baseUrl) {
        final Retrofit.Builder builder = getRetrofitBuilder(context);
        configRetrofitBuilder(builder);
        return builder.baseUrl(baseUrl).build();
    }

    public Retrofit getRetrofit(@NonNull OkHttpClient okHttpClient, String baseUrl) {
        final Retrofit.Builder builder = getRetrofitBuilder(okHttpClient);
        configRetrofitBuilder(builder);
        return builder.baseUrl(baseUrl).build();
    }
}
