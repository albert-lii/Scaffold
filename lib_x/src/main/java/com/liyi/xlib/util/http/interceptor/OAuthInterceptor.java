
package com.liyi.xlib.util.http.interceptor;


import com.liyi.xlib.util.http.request.PostJsonBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 授权拦截器
 */
public class OAuthInterceptor implements Interceptor {

    public OAuthInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder()
                .header("Content-Type", "application/json");
        // POST 请求添加公共参数
        if (originalRequest.method().equals("POST")) {
            RequestBody requestBody = originalRequest.body();
            if (requestBody instanceof PostJsonBody) {
                String content = ((PostJsonBody) requestBody).getContent();
//                HashMap<String, Object> jsonParams = (HashMap<String, Object>) GsonUtil.json2Map(content);
//                jsonParams.put("access_token", "5TyL8PmU7cX49YtUI3QwPmTw530vbYnF");
//                requestBuilder.post(PostJsonBody.create(GsonUtil.obj2String(jsonParams)));
            }
        }

        Response originalResponse = chain.proceed(requestBuilder.build());
        String cacheControl = originalRequest.cacheControl().toString();
        Response.Builder responseBuilder = originalResponse.newBuilder()
                .header("Cache-Control", cacheControl);
        return responseBuilder.build();
    }
}
