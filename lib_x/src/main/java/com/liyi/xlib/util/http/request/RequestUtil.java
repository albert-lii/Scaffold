package com.liyi.xlib.util.http.request;

import android.support.annotation.NonNull;

import com.liyi.xlib.util.GsonUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestUtil {
    private static final MediaType MEDIATYPE_JSON = MediaType.parse("application/json;charset=utf-8");

    /**
     * 将请求参数转化为 Json 格式提交
     *
     * @param object 参数对象
     * @return RequestBody
     */
    public static RequestBody createJsonBody(@NonNull Object object) {
        if (object == null) return null;
        return RequestBody.create(MEDIATYPE_JSON, GsonUtil.obj2String(object));
    }
}
