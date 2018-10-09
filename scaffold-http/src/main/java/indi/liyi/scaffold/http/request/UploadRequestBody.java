package indi.liyi.scaffold.http.request;


import java.io.IOException;

import indi.liyi.scaffold.http.response.OnProgressListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 带进度的上传文件请求体
 */
public class UploadRequestBody extends RequestBody {
    private RequestBody mRequestBody;
    private OnProgressListener mProgressListener;
    private BufferedSink mBufferedSink;
    private Object mTag;

    public UploadRequestBody(RequestBody requestBody, OnProgressListener progressListener) {
        this.mRequestBody = requestBody;
        this.mProgressListener = progressListener;
    }

    public UploadRequestBody(RequestBody requestBody, OnProgressListener progressListener, Object tag) {
        this.mRequestBody = requestBody;
        this.mProgressListener = progressListener;
        this.mTag = tag;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        try {
            return mRequestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        // 判断是否是 Log 拦截器，因为 Log 拦截器的调用，会使本方法被重复调用
        if (sink instanceof Buffer) {
            mRequestBody.writeTo(sink);
            return;
        }
        if (mBufferedSink == null) {
            mBufferedSink = Okio.buffer(new CountingSink(sink));
        }
        // 写入数据
        mRequestBody.writeTo(mBufferedSink);
        // 刷新，必须调用 flush，否则最后一部分数据可能不会被写入
        mBufferedSink.flush();
    }

    private final class CountingSink extends ForwardingSink {
        // 当前写入字节数
        private long bytesWriten = 0;
        // 总字节长度，避免多次调用 contentLength() 方法
        private long contentLength = 0;

        public CountingSink(Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            if (contentLength == 0) {
                // 获得 contentLength 的值，后续不再调用
                contentLength = contentLength();
            }
            bytesWriten += byteCount;
            // 注意此处并不是发生在主线程，如果有 UI 操作，需做处理
            if (mProgressListener != null) {
                mProgressListener.onProgressUpdate(bytesWriten * 100f / contentLength, contentLength, mTag);
            }
        }
    }
}
