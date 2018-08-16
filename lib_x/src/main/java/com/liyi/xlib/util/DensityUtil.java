package com.liyi.xlib.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;

/**
 * 单位转换工具类
 */
public final class DensityUtil {

    private DensityUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp 转 px
     *
     * @param dpVal dp 值
     * @return px 值
     */
    public static float dp2px(@NonNull Context context, float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp 转 px
     *
     * @param spVal
     * @return
     */
    public static float sp2px(@NonNull Context context, float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * pt 转 px
     *
     * @param ptVal
     * @return
     */
    public static float pt2px(@NonNull Context context, float ptVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, ptVal, context.getResources().getDisplayMetrics());
    }


    /**
     * px 转 dp
     *
     * @param pxVal px 值
     * @return dp 值
     */
    public static float px2dp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxVal / scale + 0.5f;
    }

    /**
     * px 转 sp
     *
     * @param pxVal px 值
     * @return dp 值
     */
    public static float px2sp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxVal / scale + 0.5f;
    }

    /**
     * px 转 pt
     *
     * @param pxVal px 值
     * @return dp 值
     */
    public static float pt2sp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return pxVal * 72f / scale + 0.5f;
    }
}
