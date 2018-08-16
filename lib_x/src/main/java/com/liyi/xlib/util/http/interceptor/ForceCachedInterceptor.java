package com.liyi.xlib.util.http.interceptor;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 读取请求头的 cacheControl ，否则执行默认 cacheControl
 * <p>
 * <p>有网络和没网络都先读缓存，统一缓存策略，降低服务器压力</p>
 * <p>只对设置了请求头的请求有效，不影响其他请求的缓存策略</p>
 */
public class ForceCachedInterceptor implements Interceptor {

    private static final int MAX_AGE = 60;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        /**
         * 读取请求头中配置的 CacheControl
         * <p>
         *  例：
         *  @Headers("Cache-Control: max-age=640000")
         *  @GET("widget/list")
         *  Call<List<Widget>> widgetList();
         *  </p>
         */
        String cacheControl = request.cacheControl().toString();
        if (TextUtils.isEmpty(cacheControl)) {
            // 设置缓存的最大失效时间
            cacheControl = "public, max-age=" + MAX_AGE;
        }
        Response response = chain.proceed(request);
        // 将缓存设置到响应中
        return response.newBuilder()
                // 移除干扰信息
                .removeHeader("Pragma")
                .header("Cache-Control", cacheControl)
                .build();
    }
}

