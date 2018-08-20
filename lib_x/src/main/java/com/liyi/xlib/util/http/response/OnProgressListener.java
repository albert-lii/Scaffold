package com.liyi.xlib.util.http.response;


public interface OnProgressListener {
    /**
     * 上传/下载进度监听
     *
     * @param progress  当前进度
     * @param totalSize 文件字节流的总大小
     * @param tag       多文件上传时，标记不同的请求
     */
    void onProgressUpdate(float progress, long totalSize, Object tag);
}
