package indi.liyi.scaffold.http.request;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import indi.liyi.scaffold.http.response.OnProgressListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestUtil {
    private static Gson sGson;
    // 文本类型
    public static final String MEDIATYPE_TEXT = "text/plain";
    // json 类型
    public static final String MEDIATYPE_JSON = "application/json;charset=utf-8";
    // 表单类型
    public static final String MEDIATYPE_FORM = "multipart/form-data";
    // 文件类型
    public static final String MEDIATYPE_FILE = "application/octet-stream";
    // jpg 图片类型
    public static final String MEDIATYPE_JPG = "image/jpg";
    // png 图片类型
    public static final String MEDIATYPE_PNG = "image/png";


    /**
     * 创建表单类型请求体
     *
     * @param object 参数对象
     * @return {@link RequestBody}
     */
    public static RequestBody createFormBody(Object object) {
        if (object == null) return null;
        return RequestBody.create(MediaType.parse(MEDIATYPE_FORM), objToString(object));
    }

    /**
     * 创建 json 类型请求体
     *
     * @param object 参数对象
     * @return {@link RequestBody}
     */
    public static RequestBody createJsonBody(Object object) {
        if (object == null) return null;
        return RequestBody.create(MediaType.parse(MEDIATYPE_JSON), objToString(object));
    }

    /**
     * 创建文件类型请求体
     *
     * @param mediaType 文件类型
     * @param file      文件
     * @return {@link RequestBody}
     */
    public static RequestBody createFileBody(@NonNull String mediaType, File file) {
        if (file == null || !file.exists()) return null;
        return RequestBody.create(MediaType.parse(mediaType), file);
    }

    /**
     * 创建带进度的上传文件类型请求体
     *
     * @param mediaType
     * @param file
     * @param listener
     * @return {@link UploadRequestBody}
     */
    public static UploadRequestBody createFileBody(@NonNull String mediaType, File file, OnProgressListener listener) {
        if (file == null || !file.exists()) return null;
        return new UploadRequestBody(createFileBody(mediaType, file), listener);
    }

    /**
     * 创建带进度的上传文件类型请求体
     *
     * @param mediaType
     * @param file
     * @param listener
     * @param tag       请求标记
     * @return {@link UploadRequestBody}
     */
    public static UploadRequestBody createFileBody(@NonNull String mediaType, File file, OnProgressListener listener, Object tag) {
        if (file == null || !file.exists()) return null;
        return new UploadRequestBody(createFileBody(mediaType, file), listener, tag);
    }

    /**
     * 创建单个文件的请求体
     *
     * @param mediaType 文件类型
     * @param name      与服务器约定的 key
     * @param file      文件
     * @return {@link MultipartBody.Part}
     */
    public static MultipartBody.Part createMultipartBodyPart(@NonNull String mediaType, @NonNull String name, File file) {
        if (file == null || !file.exists()) return null;
        RequestBody requestFile = createFileBody(mediaType, file);
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);
    }

    /**
     * 创建带进度的单个文件的请求体
     *
     * @param mediaType
     * @param name
     * @param file
     * @param listener
     * @return {@link MultipartBody.Part}
     */
    public static MultipartBody.Part createMultipartBodyPart(@NonNull String mediaType, @NonNull String name, File file, OnProgressListener listener) {
        if (file == null || !file.exists()) return null;
        UploadRequestBody requestFile = createFileBody(mediaType, file, listener);
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);
    }

    public static MultipartBody.Part createMultipartBodyPart(@NonNull String mediaType, @NonNull String name, File file, OnProgressListener listener, Object tag) {
        if (file == null || !file.exists()) return null;
        UploadRequestBody requestFile = createFileBody(mediaType, file, listener, tag);
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);
    }

    /**
     * 创建多文件上传请求体
     *
     * @param mediaType 文件类型
     * @param name      与服务器约定的 key
     * @param files     文件集合
     * @return {@link List<MultipartBody.Part>}
     */
    public static List<MultipartBody.Part> createMultipartBodyParts(@NonNull String mediaType, @NonNull String name, List<File> files) {
        if (files == null || files.isEmpty()) return null;
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            MultipartBody.Part part = createMultipartBodyPart(mediaType, name, file);
            parts.add(part);
        }
        return parts;
    }

    public static List<MultipartBody.Part> createMultipartBodyParts(@NonNull String mediaType, @NonNull String name, LinkedHashMap<File, Object[]> files) {
        if (files == null || files.isEmpty()) return null;
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (Map.Entry<File, Object[]> entry : files.entrySet()) {
            MultipartBody.Part part = createMultipartBodyPart(mediaType, name, entry.getKey(), (OnProgressListener) entry.getValue()[0], entry.getValue()[1]);
            parts.add(part);
        }
        return parts;
    }


    /**
     * 创建多文件上传请求体
     *
     * @param mediaType 文件类型
     * @param name      与服务器约定的 key
     * @param files     文件集合
     * @return {@link MultipartBody}
     */
    public static MultipartBody createMultipartBody(@NonNull String mediaType, @NonNull String name, List<File> files) {
        if (files == null || files.isEmpty()) return null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (File file : files) {
            builder.addFormDataPart(name, file.getName(), createFileBody(mediaType, file));
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    public static MultipartBody createMultipartBody(@NonNull String mediaType, @NonNull String name, LinkedHashMap<File, Object[]> files) {
        if (files == null || files.isEmpty()) return null;
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (Map.Entry<File, Object[]> entry : files.entrySet()) {
            File file = entry.getKey();
            builder.addFormDataPart(name, file.getName(), createFileBody(mediaType, file, (OnProgressListener) entry.getValue()[0], entry.getValue()[1]));
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    private static String objToString(Object obj) {
        if (sGson == null) {
            sGson = new Gson();
        }
        return sGson.toJson(obj);
    }
}
