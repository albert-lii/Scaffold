package indi.liyi.scaffold.utils.util;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;


public class EventBusUtil {
    private static final String TAG = "Scaffold-" + EventBusUtil.class.getSimpleName();

    /**
     * Use indexes to improve performance
     * <p>
     * It is recommended to use it in application
     *
     * @param index
     */
    public static void installIndex(SubscriberInfoIndex index) {
        EventBus.builder().addIndex(index).installDefaultEventBus();
    }

    /**
     * Register eventbus
     *
     * @param subscriber
     */
    public static void register(@NonNull Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        } else {
            LogUtil.e(TAG, "Failed to register eventbus");
        }
    }

    /**
     * Unregister eventbus
     *
     * @param subscriber
     */
    public static void unregister(@NonNull Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * Publish a normal subscription event
     * <p>
     * You must register before you can receive a published event, somewhat like the startactivityforresult() method
     */
    public static void post(@NonNull Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * Publish the sticky subscription event
     * <p>
     * 粘性事件将最新的信息保存在内存中，取消原始消息，执行最新的消息；
     * 只有注册后，才能接收消息，如果没有注册，消息将保留在内存中。
     */
    public static void postSticky(@NonNull Object event) {
        EventBus.getDefault().postSticky(event);
    }

    /**
     * Remove the specified sticky subscription event
     *
     * @param eventType The type of event
     */
    public static <T> void removeStickyEvent(Class<T> eventType) {
        T stickyEvent = EventBus.getDefault().getStickyEvent(eventType);
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
    }

    /**
     * Remove all sticky subscription events
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    /**
     * Subscribers with high priority can terminate event delivery down
     * <p>
     * This method can only be called when an event passes (PS: It means that the method can only be called in the event receive method)
     *
     * @param event
     */
    public static void cancelEventDelivery(Object event) {
        EventBus.getDefault().cancelEventDelivery(event);
    }

    /**
     * A single case of eventbus
     *
     * @return
     */
    public static EventBus getEventBus() {
        return EventBus.getDefault();
    }
}
