package indi.liyi.scaffold.utils.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;


public class ConvertUtil {

    /**
     * Value of dp to value of px
     *
     * @param dpVal Value of dp
     * @return Value of px
     */
    public static float dpToPx(@NonNull Context context, float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * Value of sp to value of px
     *
     * @param spVal Value of sp
     * @return Value of px
     */
    public static float spToPx(@NonNull Context context, float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * Value of pt to value of px
     *
     * @param ptVal Value of pt
     * @return Value of px
     */
    public static float ptToPx(@NonNull Context context, float ptVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, ptVal, context.getResources().getDisplayMetrics());
    }


    /**
     * Value of px to value of dp
     *
     * @param pxVal Value of px
     * @return Value of dp
     */
    public static float pxToDp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxVal / scale + 0.5f;
    }

    /**
     * Value of px to value of sp
     *
     * @param pxVal Value of px
     * @return Value of sp
     */
    public static float pxToSp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxVal / scale + 0.5f;
    }

    /**
     * Value of px to value of pt
     *
     * @param pxVal Value of pt
     * @return Value of sp
     */
    public static float pxToPt(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return pxVal * 72f / scale + 0.5f;
    }
}
