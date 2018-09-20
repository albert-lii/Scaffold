package com.liyi.xlib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

/**
 * Toast 相关工具类
 */
public final class ToastUtil {
    // 默认颜色
    private static final int COLOR_DEFAULT = 0xFEFFFFFF;
    // 无效的值
    private static final int INVALID_VAL = -1;
    // 获取了主线程消息循环的 handler
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    // Toast 实例
    private static Toast sToast;
    // Toast 位置
    private static int sGravity = INVALID_VAL;
    // x 偏移
    private static int sXOffset = INVALID_VAL;
    // y 偏移
    private static int sYOffset = INVALID_VAL;
    // Toast 的背景色
    private static int sBgColor = COLOR_DEFAULT;
    // Toast 的背景资源
    private static int sBgResource = INVALID_VAL;
    // Toast 的文字颜色
    private static int sTextColor = COLOR_DEFAULT;
    // Toast 的文字大小
    private static int sTextSize = INVALID_VAL;

    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 设置 Toast 显示位置
     *
     * @param gravity 位置
     * @param xOffset x 偏移
     * @param yOffset y 偏移
     */
    public static void setGravity(int gravity, int xOffset, int yOffset) {
        ToastUtil.sGravity = gravity;
        ToastUtil.sXOffset = xOffset;
        ToastUtil.sYOffset = yOffset;
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor 背景色
     */
    public static void setBgColor(@ColorInt int backgroundColor) {
        ToastUtil.sBgColor = backgroundColor;
    }

    /**
     * 设置背景资源
     *
     * @param bgResource 背景资源
     */
    public static void setBgResource(@DrawableRes int bgResource) {
        ToastUtil.sBgResource = bgResource;
    }

    /**
     * 设置文字颜色
     *
     * @param textColor 颜色
     */
    public static void setTextColor(@ColorInt int textColor) {
        ToastUtil.sTextColor = textColor;
    }

    /**
     * 设置文字大小
     *
     * @param textSize 文字大小
     */
    public static void setTextSize(int textSize) {
        ToastUtil.sTextSize = textSize;
    }

    /**
     * 短时显示 Toast
     *
     * @param text 文本
     */
    public static void show(@NonNull Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 短时显示 Toast
     *
     * @param resId 资源 Id
     */
    public static void show(@NonNull Context context, @StringRes int resId) {
        show(context, resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示 Toast
     *
     * @param resId    资源 Id
     * @param duration 显示时间
     */
    public static void show(@NonNull Context context, @StringRes int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    /**
     * 短时显示 Toast
     *
     * @param format 格式字符串
     * @param args   参数
     */
    public static void show(@NonNull Context context, @Nullable String format, Object... args) {
        show(context, format, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示 Toast
     *
     * @param format   格式字符串
     * @param duration 显示时间
     * @param args     格式字符串中格式指定符引用的参数
     */
    public static void show(@NonNull Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }

    /**
     * 短时显示 Toast
     *
     * @param resId 资源 Id
     * @param args  参数
     */
    public static void show(@NonNull Context context, @StringRes int resId, Object... args) {
        show(context, resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示 Toast
     *
     * @param resId    资源 Id
     * @param duration 显示时间
     * @param args     格式化字符串中格式指定符引用的参数
     */
    public static void show(@NonNull Context context, @StringRes int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    /**
     * 短时显示自定义 Toast
     *
     * @param layoutId 布局资源 Id
     * @return Toast 的自定义布局
     */
    public static View showCustom(@NonNull Context context, @LayoutRes int layoutId) {
        return showCustom(context, layoutId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示自定义布局的 Toast
     *
     * @param layoutId 布局资源 Id
     * @param duration 显示时间
     * @return Toast 的自定义布局
     */
    public static View showCustom(@NonNull Context context, @LayoutRes int layoutId, int duration) {
        final View view = getView(context, layoutId);
        show(context, view, duration);
        return view;
    }

    /**
     * 短时显示自定义 Toast
     *
     * @param view 自定义布局
     * @return Toast 的自定义布局
     */
    public static View showCustom(@NonNull Context context, View view) {
        return showCustom(context, view, Toast.LENGTH_SHORT);
    }

    /**
     * 显示自定义布局的 Toast
     *
     * @param view     自定义布局
     * @param duration 显示时间
     * @return Toast 的自定义布局
     */
    public static View showCustom(@NonNull Context context, View view, int duration) {
        show(context, view, duration);
        return view;
    }

    /**
     * 取消 Toast 显示
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
        }
    }

    /**
     * 显示 Toast
     *
     * @param text     消息文本
     * @param duration 显示时间
     */
    public static void show(@NonNull final Context context, final CharSequence text,final int duration) {
        HANDLER.post(new Runnable() {
            @SuppressLint("ShowToast")
            @Override
            public void run() {
                cancel();
                sToast = Toast.makeText(context, text, duration);
                TextView tvMessage = (TextView) sToast.getView().findViewById(android.R.id.message);
                TextViewCompat.setTextAppearance(tvMessage, android.R.style.TextAppearance);
                if (sTextColor != INVALID_VAL) {
                    tvMessage.setTextColor(sTextColor);
                }
                if (sTextSize != INVALID_VAL) {
                    tvMessage.setTextSize(sTextSize);
                }
                if (sGravity != INVALID_VAL && sXOffset != INVALID_VAL && sYOffset != INVALID_VAL) {
                    sToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setBg(tvMessage);
                showToast(context);
            }
        });
    }

    /**
     * 显示自定义布局 Toast
     *
     * @param view     Toast 布局
     * @param duration 显示时间
     */
    private static void show(@NonNull final Context context, final View view, final int duration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                cancel();
                sToast = new Toast(context);
                sToast.setView(view);
                sToast.setDuration(duration);
                if (sGravity != INVALID_VAL && sXOffset != INVALID_VAL && sYOffset != INVALID_VAL) {
                    sToast.setGravity(sGravity, sXOffset, sYOffset);
                }
                setBg();
                showToast(context);
            }
        });
    }

    private static void showToast(@NonNull Context context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {
                Field field = View.class.getDeclaredField("mContext");
                field.setAccessible(true);
                field.set(sToast.getView(), new ApplicationContextWrapperForApi25(context.getApplicationContext()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        sToast.show();
    }

    /**
     * 设置背景
     * <p>Toast 使用的是自定义布局</p>
     */
    private static void setBg() {
        if (sToast == null) return;
        View toastView = sToast.getView();
        if (sBgResource != INVALID_VAL) {
            toastView.setBackgroundResource(sBgResource);
        } else if (sBgColor != COLOR_DEFAULT) {
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
     * 设置背景
     * <p>Toast 使用的是默认布局</p>
     *
     * @param tvMessage Toast 默认布局中的 textview
     */
    private static void setBg(TextView tvMessage) {
        if (sToast == null) return;
        View toastView = sToast.getView();
        if (sBgResource != INVALID_VAL) {
            toastView.setBackgroundResource(sBgResource);
            tvMessage.setBackgroundColor(Color.TRANSPARENT);
        } else if (sBgColor != COLOR_DEFAULT) {
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
     * 获取布局 view
     *
     * @param layoutId 布局资源 id
     * @return view
     */
    private static View getView(@NonNull Context context, @LayoutRes int layoutId) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflate != null ? inflate.inflate(layoutId, null) : null;
    }

    private static final class ApplicationContextWrapperForApi25 extends ContextWrapper {

        ApplicationContextWrapperForApi25(@NonNull Context context) {
            super(context);
        }

        @Override
        public Context getApplicationContext() {
            return this;
        }

        @Override
        public Object getSystemService(@NonNull String name) {
            if (Context.WINDOW_SERVICE.equals(name)) {
                // noinspection ConstantConditions
                return new WindowManagerWrapper((WindowManager) getBaseContext().getSystemService(name));
            }
            return super.getSystemService(name);
        }

        private static final class WindowManagerWrapper implements WindowManager {

            private final WindowManager base;

            private WindowManagerWrapper(@NonNull WindowManager base) {
                this.base = base;
            }

            @Override
            public Display getDefaultDisplay() {
                return base.getDefaultDisplay();
            }

            @Override
            public void removeViewImmediate(View view) {
                base.removeViewImmediate(view);
            }

            @Override
            public void addView(View view, ViewGroup.LayoutParams params) {
                try {
                    base.addView(view, params);
                } catch (BadTokenException e) {
                    Log.e("WindowManagerWrapper", e.getMessage());
                } catch (Throwable throwable) {
                    Log.e("WindowManagerWrapper", "[addView]", throwable);
                }
            }

            @Override
            public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
                base.updateViewLayout(view, params);
            }

            @Override
            public void removeView(View view) {
                base.removeView(view);
            }
        }
    }
}

