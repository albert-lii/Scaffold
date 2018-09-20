package indi.liyi.scaffold.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;


public final class ToastUtil {
    private static final int INVALID_VAL = -1;
    private static final int DEFAULT_COLOR = 0xFEFFFFFF;
    private static final String NULL = "null";
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());


    private static IToast sToast;
    // The position of toast
    private static int sGravity = INVALID_VAL;
    // The offset of toast on x axis
    private static int sXOffset = INVALID_VAL;
    // The offset of toast on y axis
    private static int sYOffset = INVALID_VAL;
    // The background color of toast
    private static int sBgColor = DEFAULT_COLOR;
    // The background picture of toast
    private static int sBgResource = INVALID_VAL;
    // The text color of toast
    private static int sTextColor = DEFAULT_COLOR;
    // The text size of toast
    private static int sTextSize = INVALID_VAL;

    /**
     * Set the display position of toast
     *
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public static void setGravity(int gravity, int xOffset, int yOffset) {
        sGravity = gravity;
        sXOffset = xOffset;
        sYOffset = yOffset;
    }

    /**
     * Set the background color of the toast
     *
     * @param backgroundColor
     */
    public static void setBgColor(@ColorInt int backgroundColor) {
        sBgColor = backgroundColor;
    }

    /**
     * Set the background picture of the toast
     *
     * @param bgResource
     */
    public static void setBgResource(@DrawableRes int bgResource) {
        sBgResource = bgResource;
    }

    /**
     * Set the text color of the toast
     *
     * @param textColor
     */
    public static void setTextColor(@ColorInt int textColor) {
        sTextColor = textColor;
    }

    /**
     * Set the text size of the toast
     *
     * @param textSize
     */
    public static void setTextSize(int textSize) {
        sTextSize = textSize;
    }

    /**
     * Show toast for a short time
     *
     * @param context
     * @param text
     */
    public static void show(@NonNull Context context, CharSequence text) {
        show(context, text == null ? NULL : text, Toast.LENGTH_SHORT);
    }

    /**
     * Show toast for a short time
     *
     * @param context
     * @param resId
     */
    public static void show(@NonNull Context context, @StringRes int resId) {
        show(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * Show toast
     *
     * @param context
     * @param resId
     * @param duration
     */
    public static void show(@NonNull Context context, @StringRes int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    /**
     * Show toast for a short time
     *
     * @param context
     * @param format
     * @param args
     */
    public static void show(@NonNull Context context, @Nullable String format, Object... args) {
        show(context, format, Toast.LENGTH_SHORT, args);
    }

    /**
     * Show toast
     *
     * @param context
     * @param format
     * @param duration
     * @param args
     */
    public static void show(@NonNull Context context, String format, int duration, Object... args) {
        String text;
        if (format == null) {
            text = NULL;
        } else {
            text = String.format(format, args);
            if (text == null) {
                text = NULL;
            }
        }
        show(context, text, duration);
    }

    /**
     * Show toast for a short time
     *
     * @param context
     * @param resId
     * @param args
     */
    public static void show(@NonNull Context context, @StringRes int resId, Object... args) {
        show(context, resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * Show toast
     *
     * @param context
     * @param resId
     * @param duration
     * @param args
     */
    public static void show(@NonNull Context context, @StringRes int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    /**
     * Show custom toast for a short time
     *
     * @param context
     * @param layoutId
     * @return
     */
    public static View showCustom(@NonNull Context context, @LayoutRes int layoutId) {
        return showCustom(context, layoutId, Toast.LENGTH_SHORT);
    }

    /**
     * Show custom toast
     *
     * @param context
     * @param layoutId
     * @param duration
     * @return
     */
    public static View showCustom(@NonNull Context context, @LayoutRes int layoutId, int duration) {
        final View view = getView(context, layoutId);
        show(context, view, duration);
        return view;
    }

    /**
     * Show custom toast for a short time
     *
     * @param context
     * @param view
     * @return The view of toast
     */
    public static View showCustom(@NonNull Context context, View view) {
        return showCustom(context, view, Toast.LENGTH_SHORT);
    }

    /**
     * Show custom toast
     *
     * @param context
     * @param view
     * @param duration
     * @return The view of toast
     */
    public static View showCustom(@NonNull Context context, View view, int duration) {
        show(context, view, duration);
        return view;
    }

    /**
     * Cancel toast display
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
        }
    }

    /**
     * Show toast
     *
     * @param context
     * @param text
     * @param duration
     */
    public static void show(@NonNull final Context context, final CharSequence text, final int duration) {
        HANDLER.post(new Runnable() {
            @SuppressLint("ShowToast")
            @Override
            public void run() {
                cancel();
                final CharSequence msg = (text == null ? NULL : text);
                sToast = ToastFactory.makeToast(context, msg, duration);
                final TextView tvMessage = (TextView) sToast.getView().findViewById(android.R.id.message);
                if (sTextColor != INVALID_VAL) {
                    tvMessage.setTextColor(sTextColor);
                }
                if (sTextSize != INVALID_VAL) {
                    tvMessage.setTextSize(sTextSize);
                }
                if (sGravity != INVALID_VAL && sXOffset != INVALID_VAL && sYOffset != INVALID_VAL) {
                    sToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setToastBg(tvMessage);
                sToast.show();
            }
        });
    }

    /**
     * Show toast
     *
     * @param context
     * @param view     The view of toast
     * @param duration
     */
    private static void show(@NonNull final Context context, final View view, final int duration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                cancel();
                sToast = ToastFactory.newToast(context);
                sToast.setView(view);
                sToast.setDuration(duration);
                if (sGravity != INVALID_VAL && sXOffset != INVALID_VAL && sYOffset != INVALID_VAL) {
                    sToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setToastBg();
                sToast.show();
            }
        });
    }

    /**
     * Set the background of the toast
     * <p>Execute this method only when toast uses the default layout </p>
     *
     * @param tvMessage The textView in the toast's default layout
     */
    private static void setToastBg(TextView tvMessage) {
        if (sToast == null) return;
        View toastView = sToast.getView();
        if (sBgResource != INVALID_VAL) {
            toastView.setBackgroundResource(sBgResource);
            tvMessage.setBackgroundColor(Color.TRANSPARENT);
        } else if (sBgColor != DEFAULT_COLOR) {
            Drawable tvBg = toastView.getBackground();
            Drawable messageBg = tvMessage.getBackground();
            if (tvBg != null && messageBg != null) {
                tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
                tvMessage.setBackgroundColor(Color.TRANSPARENT);
            } else if (tvBg != null) {
                tvBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else if (messageBg != null) {
                messageBg.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else {
                toastView.setBackgroundColor(sBgColor);
            }
        }
    }

    /**
     * Set the background of the toast
     * <p>Execute this method only when toast uses the custom layout </p>
     */
    private static void setToastBg() {
        if (sToast == null) return;
        View toastView = sToast.getView();
        if (sBgResource != INVALID_VAL) {
            toastView.setBackgroundResource(sBgResource);
        } else if (sBgColor != DEFAULT_COLOR) {
            Drawable background = toastView.getBackground();
            if (background != null) {
                background.setColorFilter(new PorterDuffColorFilter(sBgColor, PorterDuff.Mode.SRC_IN));
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    toastView.setBackground(new ColorDrawable(sBgColor));
                } else {
                    toastView.setBackgroundDrawable(new ColorDrawable(sBgColor));
                }
            }
        }
    }

    /**
     * Get the view of the toast
     *
     * @param context
     * @param layoutId
     * @return The view of the toast
     */
    private static View getView(@NonNull Context context, @LayoutRes int layoutId) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflate != null ? inflate.inflate(layoutId, null) : null;
    }

    public static class ToastFactory {

        public static IToast makeToast(@NonNull Context context, CharSequence text, int duration) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                return new SystemToast(makeNormalToast(context, text, duration));
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                return new ToastWithoutNotification(makeNormalToast(context, text, duration));
            }
            LogUtil.e("ToastUtil", "Toast is GG. In fact, next step is useless.");
            return new SystemToast(makeNormalToast(context, text, duration));
        }

        public static IToast newToast(@NonNull Context context) {
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                return new SystemToast(new Toast(context));
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
                return new ToastWithoutNotification(new Toast(context));
            }
            LogUtil.e("ToastUtil", "Toast is GG. In fact, next step is useless.");
            return new SystemToast(new Toast(context));
        }

        private static Toast makeNormalToast(@NonNull Context context, CharSequence text, int duration) {
            if ("Xiaomi".equals(Build.MANUFACTURER)) {
                Toast toast = new Toast(context);
                int identifier = Resources.getSystem()
                        .getIdentifier("transient_notification", "layout", "android");
                View view = getView(context, identifier);
                toast.setView(view);
                toast.setDuration(duration);
                TextView tv = view.findViewById(android.R.id.message);
                tv.setText(text);
                return toast;
            }
            return Toast.makeText(context, text, duration);
        }
    }

    public static class SystemToast implements IToast {
        Toast mToast;
        private static Field sField_mTN;
        private static Field sField_TN_Handler;

        SystemToast(@NonNull Toast toast) {
            mToast = toast;
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                try {
                    //noinspection JavaReflectionMemberAccess
                    sField_mTN = Toast.class.getDeclaredField("mTN");
                    sField_mTN.setAccessible(true);
                    Object mTN = sField_mTN.get(toast);
                    sField_TN_Handler = sField_mTN.getType().getDeclaredField("mHandler");
                    sField_TN_Handler.setAccessible(true);
                    Handler tnHandler = (Handler) sField_TN_Handler.get(mTN);
                    sField_TN_Handler.set(mTN, new SafeHandler(tnHandler));
                } catch (Exception ignored) { /**/ }
            }
        }

        @Override
        public void show() {
            mToast.show();
        }

        @Override
        public void cancel() {
            mToast.cancel();
        }

        @Override
        public void setView(View view) {
            mToast.setView(view);
        }

        @Override
        public View getView() {
            return mToast.getView();
        }

        @Override
        public void setDuration(int duration) {
            mToast.setDuration(duration);
        }

        @Override
        public void setGravity(int gravity, int xOffset, int yOffset) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }

        @Override
        public void setText(int resId) {
            mToast.setText(resId);
        }

        @Override
        public void setText(CharSequence s) {
            mToast.setText(s);
        }

        static class SafeHandler extends Handler {
            private Handler impl;

            SafeHandler(Handler impl) {
                this.impl = impl;
            }

            @Override
            public void handleMessage(Message msg) {
                try {
                    impl.handleMessage(msg);
                } catch (Exception e) {
                    LogUtil.e("ToastUtil", e.getMessage());
                }
            }

            @Override
            public void dispatchMessage(Message msg) {
                impl.dispatchMessage(msg);
            }
        }
    }

    public static class ToastWithoutNotification implements IToast {
        private Toast mToast;
        private WindowManager mWM;
        private View mView;
        private WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        private Handler mHandler = new Handler(Looper.myLooper());

        ToastWithoutNotification(@NonNull Toast toast) {
            mToast = toast;

            mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mParams.format = PixelFormat.TRANSLUCENT;
            mParams.windowAnimations = android.R.style.Animation_Toast;
            mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mParams.setTitle("ToastWithoutNotification");
            mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        }

        @Override
        public void show() {
            mView = mToast.getView();
            if (mView == null) return;
            Context context = mToast.getView().getContext();
            mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            final Configuration config = context.getResources().getConfiguration();
            final int gravity;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                gravity = Gravity.getAbsoluteGravity(mToast.getGravity(), config.getLayoutDirection());
            } else {
                gravity = mToast.getGravity();
            }
            mParams.gravity = gravity;
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
                mParams.horizontalWeight = 1.0f;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
                mParams.verticalWeight = 1.0f;
            }
            mParams.x = mToast.getXOffset();
            mParams.y = mToast.getYOffset();

            mParams.packageName = context.getPackageName();

            try {
                mWM.addView(mView, mParams);
            } catch (Exception ignored) { /**/ }

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, mToast.getDuration() == Toast.LENGTH_SHORT ? 2000 : 3500);
        }

        @Override
        public void cancel() {
            try {
                mWM.removeView(mView);
            } catch (IllegalArgumentException ignored) { /**/ }
            mView = null;
            mHandler = null;
            mToast = null;
        }

        @Override
        public void setView(View view) {
            mToast.setView(view);
        }

        @Override
        public View getView() {
            return mToast.getView();
        }

        @Override
        public void setDuration(int duration) {
            mToast.setDuration(duration);
        }

        @Override
        public void setGravity(int gravity, int xOffset, int yOffset) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }

        @Override
        public void setText(int resId) {
            mToast.setText(resId);
        }

        @Override
        public void setText(CharSequence s) {
            mToast.setText(s);
        }
    }

    public interface IToast {

        void show();

        void cancel();

        void setView(View view);

        View getView();

        void setDuration(int duration);

        void setGravity(int gravity, int xOffset, int yOffset);

        void setText(@StringRes int resId);

        void setText(CharSequence s);
    }
}

