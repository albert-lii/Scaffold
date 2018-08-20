package com.liyi.xlib.util.http.response;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 带进度的下载响应体
 */
public class DownloadResponseBody extends ResponseBody {
    private ResponseBody mResponseBody;
    //  BufferedSource 是 okio 库中的输入流，这里就当作 inputStream 来使用。
    private BufferedSource mBufferedSource;
    // 下载进度监听
    private OnProgressListener mProgressListener;

    public DownloadResponseBody(ResponseBody responseBody, OnProgressListener listener) {
        this.mResponseBody = responseBody;
        this.mProgressListener = listener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0;
            long contentLength = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead == -1 ? 0 : bytesRead;
                // 实时发送读取进度、当前已读取的字节和总字节
                if (mProgressListener != null) {
                    mProgressListener.onProgressUpdate(totalBytesRead * 100f / contentLength, contentLength,null);
                }
                return bytesRead;
            }
        };
    }
}
