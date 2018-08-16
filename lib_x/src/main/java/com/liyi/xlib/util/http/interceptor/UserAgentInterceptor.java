package com.liyi.xlib.util.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 设置 User-Agent
 */
public final class UserAgentInterceptor implements Interceptor {
    private static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private final String userAgentHeaderValue;

    public UserAgentInterceptor(String userAgentHeaderValue) {
        this.userAgentHeaderValue = userAgentHeaderValue;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final Request requestWithUserAgent = originalRequest.newBuilder()
                // 移除先前默认的 UA
                .removeHeader(USER_AGENT_HEADER_NAME)
                // 设置 UA
                .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                .build();
        return chain.proceed(requestWithUserAgent);
    }

    /**
     * User-Agent 信息类
     */
    public class UAInfo {

        private String userAgent = "UA";

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }
    }
}
