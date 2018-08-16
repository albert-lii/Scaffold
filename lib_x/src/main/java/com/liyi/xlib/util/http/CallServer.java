package com.liyi.xlib.util.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.liyi.xlib.util.http.response.HttpObserver;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CallServer {
    private static volatile CallServer INSTANCE;
    // 订阅统一管理类
    private static HashMap<Object, HttpObserver> sDisposableSite;
    // Retrofit2.0 客户端
    private static Retrofit sRetrofit;

    private CallServer() {
        sDisposableSite = new HashMap<>();
    }

    public static CallServer getInstance() {
        if (INSTANCE == null) {
            synchronized (CallServer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CallServer();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 设置域名
     *
     * @param baseUrl
     * @param context
     */
    public void createRetrofit(@NonNull Context context, @NonNull String baseUrl) {
        sRetrofit = new RetrofitClient().getRetrofit(context, baseUrl);
    }

    /**
     * 创建服务
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T createService(@NonNull Class<T> cls) {
        if (sRetrofit == null) {
            throw new NullPointerException("Please initialize sRetrofit first...");
        }
        return sRetrofit.create(cls);
    }

    /**
     * 订阅 Api 请求
     *
     * @param observable 被观察者
     * @param observer   观察者
     */
    public <T> void toSubscribe(@NonNull Observable<T> observable, @NonNull HttpObserver<T> observer) {
        add(observable
                // 在 io 线程中进行网络请求
                .subscribeOn(Schedulers.io())
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
    public void add(HttpObserver observer) {
        if (observer == null) return;
        if (sDisposableSite == null) {
            sDisposableSite = new HashMap<>();
        }
        Object tag = observer.getTag();
        // 如果已经存在相同的请求，则先取消原来的请求，再将新的请求加入管理类
        if (sDisposableSite.containsKey(tag)) {
            remove(tag);
        }
        sDisposableSite.put(tag, observer);
    }

    /**
     * 移除指定的订阅关系
     *
     * @param tag
     */
    public void remove(Object tag) {
        if (tag != null && sDisposableSite != null && !sDisposableSite.isEmpty()) {
            HttpObserver disposable = sDisposableSite.get(tag);
            if (disposable != null) {
                disposable.cancel();
            }
            sDisposableSite.remove(tag);
        }
    }

    /**
     * 取消指定的请求
     *
     * @param tag 请求标记
     */
    public void cancel(Object tag) {
        if (tag != null && sDisposableSite != null && !sDisposableSite.isEmpty()) {
            HttpObserver disposable = sDisposableSite.get(tag);
            if (disposable != null) {
                disposable.cancel();
            }
        }
    }

    /**
     * 取消所有的请求
     */
    public void cancelAll() {
        if (sDisposableSite != null && !sDisposableSite.isEmpty()) {
            for (Map.Entry<Object, HttpObserver> entry : sDisposableSite.entrySet()) {
                if (entry.getValue() != null) {
                    entry.getValue().cancel();
                }
            }
            sDisposableSite.clear();
        }
    }
}
