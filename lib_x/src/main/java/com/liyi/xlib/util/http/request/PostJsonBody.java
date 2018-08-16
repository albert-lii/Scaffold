package com.liyi.xlib.util.http.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

/**
 * POST 方式提交 Json 格式的 RequestBody
 */
public class PostJsonBody extends RequestBody {
    private final MediaType JSON = MediaType.parse("application/json; CHARSET=utf-8");
    private final Charset CHARSET = Util.UTF_8;

    private String content;

    public PostJsonBody(@NonNull String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return JSON;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        byte[] bytes = content.getBytes(CHARSET);
        if (bytes == null) throw new NullPointerException("content == null");
        Util.checkOffsetAndCount(bytes.length, 0, bytes.length);
        sink.write(bytes, 0, bytes.length);
    }

    public static RequestBody create(@NonNull String content) {
        return new PostJsonBody(content);
    }
}
