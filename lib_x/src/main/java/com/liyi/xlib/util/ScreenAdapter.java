package com.liyi.xlib.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;

/**
 * 屏幕适配方案
 * <br>
 * <p>PS: 提供 dp、sp 以及 pt 作为适配单位，建议开发中以 dp、sp 适配为主，pt 可作为 dp、sp 的适配补充</p>
 * <p>PS: 由今日头条适配方案修改而来: https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA</p>
 */
public final class ScreenAdapter {
    /**
     * 屏幕适配的基准
     */
    public static final int MATCH_BASE_WIDTH = 0;
    public static final int MATCH_BASE_HEIGHT = 1;
    /**
     * 适配单位
     */
    public static final int MATCH_UNIT_DP = 0;
    public static final int MATCH_UNIT_PT = 1;

    // 适配信息
    private static MatchInfo sMatchInfo;
    private static MatchInfo sMatchInfoOnMiui;
    // Activity 的生命周期监测
    private static Application.ActivityLifecycleCallbacks mActivityLifecycleCallback;

    private ScreenAdapter() {
        throw new UnsupportedOperationException("you can't instantiate me...");
    }

    /**
     * 初始化
     *
     * @param context
     */
    public static void setup(@NonNull final Context context) {
        final DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
        if (displayMetrics != null && sMatchInfo == null) {
            // 记录系统的原始值
            sMatchInfo = new MatchInfo();
            sMatchInfo.setScreenWidth(displayMetrics.widthPixels);
            sMatchInfo.setScreenHeight(displayMetrics.heightPixels);
            sMatchInfo.setAppDensity(displayMetrics.density);
            sMatchInfo.setAppDensityDpi(displayMetrics.densityDpi);
            sMatchInfo.setAppScaledDensity(displayMetrics.scaledDensity);
            sMatchInfo.setAppXdpi(displayMetrics.xdpi);
        }
        // 用于适配部分 MIUI
        final DisplayMetrics displayMetricsOnMiui = getMetricsOnMiui(context.getApplicationContext().getResources());
        if (displayMetricsOnMiui != null && sMatchInfoOnMiui == null) {
            sMatchInfoOnMiui = new MatchInfo();
            sMatchInfoOnMiui.setScreenWidth(displayMetricsOnMiui.widthPixels);
            sMatchInfoOnMiui.setScreenHeight(displayMetricsOnMiui.heightPixels);
            sMatchInfoOnMiui.setAppDensity(displayMetricsOnMiui.density);
            sMatchInfoOnMiui.setAppDensityDpi(displayMetricsOnMiui.densityDpi);
            sMatchInfoOnMiui.setAppScaledDensity(displayMetricsOnMiui.scaledDensity);
            sMatchInfoOnMiui.setAppXdpi(displayMetricsOnMiui.xdpi);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // 添加字体变化的监听
            context.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    // 字体改变后,将 appScaledDensity 重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sMatchInfo.setAppScaledDensity(displayMetrics.scaledDensity);
                        if (displayMetricsOnMiui != null && sMatchInfoOnMiui != null) {
                            sMatchInfoOnMiui.setAppScaledDensity(displayMetricsOnMiui.scaledDensity);
                        }
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }
    }

    /**
     * 在 application 中全局激活适配（也可单独使用 match() 方法在指定页面中配置适配）
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void register(@NonNull final Application application, final float designSize, final int matchBase, final int matchUnit) {
        if (mActivityLifecycleCallback == null) {
            mActivityLifecycleCallback = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    if (activity != null) {
                        match(activity, designSize, matchBase, matchUnit);
                    }
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            };
            application.registerActivityLifecycleCallbacks(mActivityLifecycleCallback);
        }
    }

    /**
     * 全局取消所有的适配
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void unregister(@NonNull final Application application, @NonNull int... matchUnit) {
        if (mActivityLifecycleCallback != null) {
            application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallback);
            mActivityLifecycleCallback = null;
        }
        for (int unit : matchUnit) {
            cancelMatch(application, unit);
        }
    }

    /**
     * 适配屏幕（放在 Activity 的 setContentView() 之前执行）
     *
     * @param context
     * @param designSize
     */
    public static void match(@NonNull final Context context, final float designSize) {
        match(context, designSize, MATCH_BASE_WIDTH, MATCH_UNIT_DP);
    }

    /**
     * 适配屏幕（放在 Activity 的 setContentView() 之前执行）
     *
     * @param context
     * @param designSize
     * @param matchBase
     */
    public static void match(@NonNull final Context context, final float designSize, int matchBase) {
        match(context, designSize, matchBase, MATCH_UNIT_DP);
    }

    /**
     * 适配屏幕（放在 Activity 的 setContentView() 之前执行）
     *
     * @param context
     * @param designSize 设计图的尺寸
     * @param matchBase  适配基准
     * @param matchUnit  使用的适配单位
     */
    public static void match(@NonNull final Context context, final float designSize, int matchBase, int matchUnit) {
        if (designSize == 0) {
            throw new UnsupportedOperationException("The designSize cannot be equal to 0");
        }
        if (matchUnit == MATCH_UNIT_DP) {
            matchByDP(context, designSize, matchBase);
        } else if (matchUnit == MATCH_UNIT_PT) {
            matchByPT(context, designSize, matchBase);
        }
    }

    /**
     * 使用 dp 作为适配单位（适合在新项目中使用，在老项目中使用会对原来既有的 dp 值产生影响）
     * <br>
     * <ul>
     * dp 与 px 之间的换算:
     * <li> px = density * dp </li>
     * <li> density = dpi / 160 </li>
     * <li> px = dp * (dpi / 160) </li>
     * </ul>
     *
     * @param context
     * @param designSize 设计图的宽/高（单位: dp）
     * @param base       适配基准
     */
    private static void matchByDP(@NonNull final Context context, final float designSize, int base) {
        if (sMatchInfo != null) {
            final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            matchByDP(sMatchInfo, displayMetrics, designSize, base);
        }
        // 适配部分 MIUI
        if (sMatchInfoOnMiui != null) {
            final DisplayMetrics displayMetricsOnMiui = getMetricsOnMiui(context.getResources());
            if (displayMetricsOnMiui != null) {
                matchByDP(sMatchInfoOnMiui, displayMetricsOnMiui, designSize, base);
            }
        }
    }

    private static void matchByDP(final MatchInfo matchInfo, final DisplayMetrics displayMetrics, final float designSize, final int base) {
        final float targetDensity;
        if (base == MATCH_BASE_WIDTH) {
            targetDensity = matchInfo.getScreenWidth() * 1f / designSize;
        } else if (base == MATCH_BASE_HEIGHT) {
            targetDensity = matchInfo.getScreenHeight() * 1f / designSize;
        } else {
            targetDensity = matchInfo.getScreenWidth() * 1f / designSize;
        }
        final int targetDensityDpi = (int) (targetDensity * 160);
        final float targetScaledDensity = targetDensity * (matchInfo.getAppScaledDensity() / matchInfo.getAppDensity());
        displayMetrics.density = targetDensity;
        displayMetrics.densityDpi = targetDensityDpi;
        displayMetrics.scaledDensity = targetScaledDensity;
    }

    /**
     * 使用 pt 作为适配单位（因为 pt 比较冷门，新老项目皆适合使用；也可作为 dp 适配的补充，
     * 在需要同时适配宽度和高度时，使用 pt 来适配 dp 未适配的宽度或高度）
     * <br/>
     * <p> pt 转 px 算法: pt * metrics.xdpi * (1.0f/72) </p>
     *
     * @param context
     * @param designSize 设计图的宽/高（单位: pt）
     * @param base       适配基准
     */
    private static void matchByPT(@NonNull final Context context, final float designSize, int base) {
        if (sMatchInfo != null) {
            final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            matchByPt(sMatchInfo, displayMetrics, designSize, base);
        }
        // 适配部分 MIUI
        if (sMatchInfoOnMiui != null) {
            final DisplayMetrics displayMetricsOnMiui = getMetricsOnMiui(context.getResources());
            if (displayMetricsOnMiui != null) {
                matchByPt(sMatchInfoOnMiui, displayMetricsOnMiui, designSize, base);
            }
        }
    }

    private static void matchByPt(final MatchInfo matchInfo, final DisplayMetrics displayMetrics, final float designSize, final int base) {
        final float targetXdpi;
        if (base == MATCH_BASE_WIDTH) {
            targetXdpi = matchInfo.getScreenWidth() * 72f / designSize;
        } else if (base == MATCH_BASE_HEIGHT) {
            targetXdpi = matchInfo.getScreenHeight() * 72f / designSize;
        } else {
            targetXdpi = matchInfo.getScreenWidth() * 72f / designSize;
        }
        displayMetrics.xdpi = targetXdpi;
    }

    /**
     * 重置适配信息，取消适配
     */
    public static void cancelMatch(@NonNull final Context context) {
        cancelMatch(context, MATCH_UNIT_DP);
        cancelMatch(context, MATCH_UNIT_PT);
    }

    /**
     * 重置适配信息，取消适配
     *
     * @param context
     * @param matchUnit 需要取消适配的单位
     */
    public static void cancelMatch(@NonNull final Context context, int matchUnit) {
        if (sMatchInfo != null) {
            final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            cancelMatch(matchUnit, displayMetrics, sMatchInfo);
        }
        // 适配部分 MIUI
        if (sMatchInfoOnMiui != null) {
            final DisplayMetrics displayMetricsOnMiui = getMetricsOnMiui(context.getResources());
            if (displayMetricsOnMiui != null) {
                cancelMatch(matchUnit, displayMetricsOnMiui, sMatchInfoOnMiui);
            }
        }
    }

    private static void cancelMatch(final int matchUnit, final DisplayMetrics displayMetrics, final MatchInfo matchInfo) {
        if (matchUnit == MATCH_UNIT_DP) {
            if (matchInfo.getAppDensity() != 0 && displayMetrics.density != matchInfo.getAppDensity()) {
                displayMetrics.density = matchInfo.getAppDensity();
            }
            if (matchInfo.getAppDensityDpi() != 0 && displayMetrics.densityDpi != matchInfo.getAppDensityDpi()) {
                displayMetrics.densityDpi = (int) matchInfo.getAppDensityDpi();
            }
            if (matchInfo.getAppScaledDensity() != 0 && displayMetrics.scaledDensity != matchInfo.getAppScaledDensity()) {
                displayMetrics.scaledDensity = matchInfo.getAppScaledDensity();
            }
        } else if (matchUnit == MATCH_UNIT_PT) {
            if (matchInfo.getAppXdpi() != 0 && displayMetrics.xdpi != matchInfo.getAppXdpi()) {
                displayMetrics.xdpi = matchInfo.getAppXdpi();
            }
        }
    }

    public static MatchInfo getMatchInfo() {
        return sMatchInfo;
    }

    public static MatchInfo getMatchInfoOnMiui() {
        return sMatchInfoOnMiui;
    }

    /**
     * 解决 MIUI 更改框架导致的 MIUI7 + Android5.1.1 上出现的失效问题 (以及极少数基于这部分 MIUI 去掉 ART 然后置入 XPosed 的手机)
     * 来源于: https://github.com/Firedamp/Rudeness/blob/master/rudeness-sdk/src/main/java/com/bulong/rudeness/RudenessScreenHelper.java#L61:5
     *
     * @param resources {@link Resources}
     * @return {@link DisplayMetrics}, 可能为 {@code null}
     */
    private static DisplayMetrics getMetricsOnMiui(Resources resources) {
        if ("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 适配信息
     */
    public static class MatchInfo {
        private int screenWidth;
        private int screenHeight;
        private float appDensity;
        private float appDensityDpi;
        private float appScaledDensity;
        private float appXdpi;

        public int getScreenWidth() {
            return screenWidth;
        }

        public void setScreenWidth(int screenWidth) {
            this.screenWidth = screenWidth;
        }

        public int getScreenHeight() {
            return screenHeight;
        }

        public void setScreenHeight(int screenHeight) {
            this.screenHeight = screenHeight;
        }

        public float getAppDensity() {
            return appDensity;
        }

        public void setAppDensity(float appDensity) {
            this.appDensity = appDensity;
        }

        public float getAppDensityDpi() {
            return appDensityDpi;
        }

        public void setAppDensityDpi(float appDensityDpi) {
            this.appDensityDpi = appDensityDpi;
        }

        public float getAppScaledDensity() {
            return appScaledDensity;
        }

        public void setAppScaledDensity(float appScaledDensity) {
            this.appScaledDensity = appScaledDensity;
        }

        public float getAppXdpi() {
            return appXdpi;
        }

        public void setAppXdpi(float appXdpi) {
            this.appXdpi = appXdpi;
        }
    }
}
