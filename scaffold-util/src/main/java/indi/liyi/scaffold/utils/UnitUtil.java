package indi.liyi.scaffold.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;


public class UnitUtil {

    /**
     * Dp to px
     *
     * @param dpVal Value of dp
     * @return Value of px
     */
    public static float dpToPx(@NonNull Context context, float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * Sp to px
     *
     * @param spVal Value of sp
     * @return Value of px
     */
    public static float spToPx(@NonNull Context context, float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * Pt to px
     *
     * @param ptVal Value of pt
     * @return Value of dp
     */
    public static float ptToPx(@NonNull Context context, float ptVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, ptVal, context.getResources().getDisplayMetrics());
    }


    /**
     * Px to dp
     *
     * @param pxVal Value of px
     * @return Value of dp
     */
    public static float pxToDp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxVal / scale + 0.5f;
    }

    /**
     * Px to sp
     *
     * @param pxVal Value of px
     * @return Value of sp
     */
    public static float pxToSp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxVal / scale + 0.5f;
    }

    /**
     * Px to pt
     *
     * @param pxVal Value of px
     * @return Value of sp
     */
    public static float ptToSp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return pxVal * 72f / scale + 0.5f;
    }
}
