package com.liyi.xlib.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;


/**
 * PopupWindow 的基类
 */
public abstract class BaseXPopuWindow extends PopupWindow {
    private Context mContext;

    public BaseXPopuWindow(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public BaseXPopuWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public BaseXPopuWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        setVisibleAlpha(0.5f);
    }

    /**
     * 设置弹窗宽高的占屏百分比
     *
     * @param widthper
     * @param heightper
     */
    public void setPopSizePercent(float widthper, float heightper) {
        if (widthper < 0) {
            widthper = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if (heightper < 0) {
            heightper = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setPopSize(
                widthper == ViewGroup.LayoutParams.WRAP_CONTENT ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (metrics.widthPixels * widthper),
                heightper == ViewGroup.LayoutParams.WRAP_CONTENT ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (metrics.heightPixels * heightper));
    }

    /**
     * 设置弹窗宽高
     *
     * @param width  弹窗的宽度
     * @param height 弹窗的高度
     */
    public void setPopSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * 点击空白处消失
     *
     * @param enabled {@code true}: 点击空白处消失<br>{@code false}: 点击空白处不消失
     */
    public void touchOutSideDismiss(boolean enabled) {
        setTouchable(enabled);
        setOutsideTouchable(enabled);
        setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 设置屏幕显示时的背景透明度
     *
     * @param alpha 背景透明度
     */
    public void setVisibleAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        Window window = ((Activity) mContext).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = alpha;
        lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }

    public Context getContext() {
        return mContext;
    }
}
