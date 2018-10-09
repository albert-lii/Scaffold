package indi.liyi.scaffold.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import indi.liyi.scaffold.http.response.HttpResponse;
import indi.liyi.scaffold.http.response.OnProgressListener;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class HttpServer {
    private static volatile HttpServer INSTANCE;
    // 订阅统一管理器
    private static HashMap<Object, HttpResponse> sDisposableSite;
    // 下载进度监听管理器
    private static HashMap<Object, OnProgressListener> sDownloadProgressListenerSite;
    // Retrofit2.0 客户端
    private static RetrofitClient sRetrofitClient;

    private HttpServer() {
        sDisposableSite = new HashMap<>();
    }

    public static HttpServer getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpServer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpServer();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(@NonNull Context context, @NonNull String baseUrl) {
        sRetrofitClient = new RetrofitClient(context);
        sRetrofitClient.setBaseUrl(baseUrl);
    }

    public void init(@NonNull Context context, @NonNull String urlKey, Map<String, String> hostMap) {
        sRetrofitClient = new RetrofitClient(context);
        sRetrofitClient.setBaseUrls(urlKey, hostMap);
    }

    public void init(@NonNull Context context, Retrofit retrofit) {
        sRetrofitClient = new RetrofitClient(context);
        sRetrofitClient.setRetrofit(retrofit);
    }

    /**
     * 设置下载监听的标记的 key，与 @Header 中的 key 对应，例如：@Header("downloadKey","apk")，key="downloadKey"
     *
     * @param key
     */
    public void setDownloadProgressListenerKey(@NonNull String key) {
        if (sRetrofitClient == null) {
            throw new NullPointerException("Please initialize RetrofitClient first...");
        }
        sRetrofitClient.setDownloadProgressListenerKey(key);
    }

    /**
     * 创建服务
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T createService(@NonNull Class<T> cls) {
        if (sRetrofitClient == null) {
            throw new NullPointerException("Please initialize RetrofitClient first...");
        }
        return sRetrofitClient.createService(cls);
    }

    /**
     * 添加下载进度监听
     *
     * @param tag      监听器的标记
     * @param listener 下载监听
     */
    public void addDownloadProgressListener(@NonNull String tag, OnProgressListener listener) {
        if (listener != null && !TextUtils.isEmpty(tag)) {
            if (sDownloadProgressListenerSite == null) {
                sDownloadProgressListenerSite = new HashMap<>();
            }
            sDownloadProgressListenerSite.put(tag, listener);
        }
    }

    /**
     * 获取下载进度监听集合
     *
     * @return
     */
    public HashMap<Object, OnProgressListener> getDownloadProgressListenerSite() {
        return sDownloadProgressListenerSite;
    }

    /**
     * 移除指定的下载进度监听
     *
     * @param tag
     */
    public void removeDownloadProgressListener(Object tag) {
        if (sDownloadProgressListenerSite != null && !sDownloadProgressListenerSite.isEmpty() && tag != null) {
            sDownloadProgressListenerSite.remove(tag);
        }
    }

    /**
     * 订阅 Api 请求
     *
     * @param observable 被观察者
     * @param observer   观察者
     */
    public <T> void toSubscribe(@NonNull Observable<T> observable, @NonNull HttpResponse<T> observer) {
        add(observable
                // 在 io 线程中进行网络请求
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                // 回到主线程处理返回结果
                .observeOn(AndroidSchedulers.mainThread())
                // 订阅
                .subscribeWith(observer));
    }

    /**
     * 将请求添加到管理类中
     *
     * @param observer
     */
    public void add(HttpResponse observer) {
        if (observer == null) return;
        if (sDisposableSite == null) {
            sDisposableSite = new HashMap<>();
        }
        Object tag = observer.getTag();
        // 如果已经存在相同的请求，则先取消原来的请求，再将新的请求加入管理类
        if (sDisposableSite.containsKey(tag)) {
            cancel(tag);
        }
        sDisposableSite.put(tag, observer);
    }

    /**
     * 取消指定的请求
     *
     * @param tag 请求标记
     */
    public void cancel(@NonNull Object tag) {
        if (sDisposableSite != null && !sDisposableSite.isEmpty()) {
            HttpResponse disposable = sDisposableSite.get(tag);
            if (disposable != null) {
                disposable.cancel();
            }
            sDisposableSite.remove(tag);
        }
    }

    /**
     * 取消所有的请求
     */
    public void cancelAll() {
        if (sDisposableSite != null && !sDisposableSite.isEmpty()) {
            for (Map.Entry<Object, HttpResponse> entry : sDisposableSite.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().cancel();
                }
            }
            sDisposableSite.clear();
        }
        if (sDownloadProgressListenerSite != null && !sDownloadProgressListenerSite.isEmpty()) {
            sDownloadProgressListenerSite.clear();
        }
    }
}
