package com.liyi.xlib.util;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * 屏幕相关工具类
 */
public final class ScreenUtil {

    private ScreenUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取屏幕的宽高（单位: px）
     *
     * @return 屏幕的尺寸
     */
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
    public static Point getScreenSize(@NonNull Context context) {
        Point screenSize = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            screenSize.set(context.getResources().getDisplayMetrics().widthPixels,
                    context.getResources().getDisplayMetrics().heightPixels);
            return screenSize;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(screenSize);
        } else {
            wm.getDefaultDisplay().getSize(screenSize);
        }
        return screenSize;
    }

    /**
     * 获取屏幕的密度
     *
     * @return 屏幕密度（density）
     */
    public static float getScreenDensity(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕密度（指每平方英寸中的像素数）
     * <p>例如：0.75 / 1 / 1.5 / ...（dpi/160 可得）</p>
     *
     * @return 像素密度
     */
    public static int getScreenDensityDpi(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 获取状态栏的高
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取底部导航栏的高度
     *
     * @return 底部导航栏的高度
     */
    public static int getNavigationBarHeight(@NonNull Context context) {
        int result = 0;
        int resourceId = 0;
        // 判断底部导航栏是否显示
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 判断是否有底部导航栏
     *
     * @return {@code true}: 有底部导航栏<br>{@code false}: 没有底部导航栏
     */
    public static boolean hasNavigationBar(@NonNull Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0)
            hasNavigationBar = rs.getBoolean(id);
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if (navBarOverride.equals("1")) {
                hasNavigationBar = false;
            } else if (navBarOverride.equals("0")) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 设置全屏
     *
     * @param activity The activity.
     */
    public static void setFullScreen(@NonNull Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 设置不全屏
     *
     * @param activity The activity.
     */
    public static void setNonFullScreen(@NonNull Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 切换全屏
     *
     * @param activity
     */
    public static void toggleFullScreen(@NonNull Activity activity) {
        int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = activity.getWindow();
        if ((window.getAttributes().flags & fullScreenFlag) == fullScreenFlag) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 当前是否全屏
     *
     * @param activity
     * @return {@code true}: 全屏<br>{@code false}: 不全屏
     */
    public static boolean isFullScreen(@NonNull final Activity activity) {
        int fullScreenFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        return (activity.getWindow().getAttributes().flags & fullScreenFlag) == fullScreenFlag;
    }

    /**
     * 设置横屏
     * <p>还有一种就是在 Activity 中加属性 android:screenOrientation="landscape"</p>
     * <p>不设置 Activity 的 android:configChanges 时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次</p>
     * <p>设置 Activity 的 android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次</p>
     * <p>设置 Activity 的 android:configChanges="orientation|keyboardHidden|screenSize"（4.0 以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行 onConfigurationChanged 方法</p>
     *
     * @param activity
     */
    public static void setLandscape(@NonNull Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置竖屏
     *
     * @param activity
     */
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 当前是否横屏
     *
     * @return {@code true}: 横屏<br>{@code false}: 不横屏
     */
    public static boolean isLandscape(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 当前是否竖屏
     *
     * @return {@code true}: 竖屏<br>{@code false}: 不竖屏
     */
    public static boolean isPortrait(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕的旋转角度
     *
     * @param activity
     * @return 屏幕的旋转角度
     */
    public static int getScreenRotation(@NonNull Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    /**
     * 截屏
     *
     * @param activity
     * @return 屏幕的 bitmap
     */
    public static Bitmap screenShot(@NonNull Activity activity) {
        return screenShot(activity, false);
    }

    /**
     * 截屏
     *
     * @param activity
     * @param isDeleteStatusBar 是否去除状态栏
     * @return 屏幕的 bitmap
     */
    public static Bitmap screenShot(@NonNull final Activity activity, boolean isDeleteStatusBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.setWillNotCacheDrawing(false);
        Bitmap bmp = decorView.getDrawingCache();
        if (bmp == null) return null;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret;
        if (isDeleteStatusBar) {
            int statusBarHeight = getStatusBarHeight(activity);
            ret = Bitmap.createBitmap(
                    bmp,
                    0, statusBarHeight,
                    dm.widthPixels, dm.heightPixels - statusBarHeight
            );
        } else {
            ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        }
        decorView.destroyDrawingCache();
        return ret;
    }

    /**
     * 判断是否是平板
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isTablet(@NonNull Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
