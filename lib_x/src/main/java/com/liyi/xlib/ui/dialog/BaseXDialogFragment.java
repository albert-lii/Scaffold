package com.liyi.xlib.ui.dialog;


import android.app.Dialog;
import android.support.annotation.FloatRange;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * DialogFragment 的基类
 */
public class BaseXDialogFragment extends DialogFragment {
    // 默认的背景透明度
    private final float DEF_VISIBLE_ALPHA = 0.5f;
    // 是否使用 dialog 的百分比尺寸
    private final boolean DEF_USE_SIZE_PERCENT = false;
    // 是否设置 dialog 的尺寸
    private final boolean DEF_SET_SIZE = false;
    // 默认的 dialog 的宽占屏宽的比
    private final float DEF_WIDTH_PERCENT = 0.75f;
    // 默认的 dialog 的高占屏高的比，此处表示自适应
    private final float DEF_HEIGHT_PERCENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    // 显示时的屏幕背景透明度
    private float mVisibleAlpha = DEF_VISIBLE_ALPHA;
    // 是否使用 dialog 的宽高占屏百分比
    private boolean isUseSizePercent = DEF_USE_SIZE_PERCENT;
    // 是否设置 dialog 的尺寸
    private boolean isSetSize = DEF_SET_SIZE;
    // dialog 的宽所占屏幕的百分比
    private float mWidhtPercent = DEF_WIDTH_PERCENT;
    // dialog 的高所占屏幕的百分比
    private float mHeightPercent = DEF_HEIGHT_PERCENT;
    // dialog 的宽高
    private int mWidth;
    private int mHeight;


    /**
     * 设置 dialog 的显示消失动画
     *
     * @param dialog    dialog
     * @param animStyle 动画样式
     */
    public void setWindowAnim(Dialog dialog, int animStyle) {
        Window window = dialog.getWindow();
        window.setWindowAnimations(animStyle);
    }

    /**
     * 设置是否使用 dialog 的宽高占屏百分比
     *
     * @param enable {@code true}: 使用<br>{@code false}: 不使用
     */
    public void setUseSizePercentEnabled(boolean enable) {
        this.isUseSizePercent = enable;
    }

    /**
     * 设置自定义 dialog 的宽高
     *
     * @param enable {@code true}: 是<br>{@code false}: 否
     */
    public void setSizeEnabled(boolean enable) {
        this.isSetSize = enable;
    }

    /**
     * 设置 dialog 的宽度占屏百分比
     *
     * @param percnet 宽度占屏百分比
     */
    public void setWidthPercent(float percnet) {
        mWidhtPercent = percnet;
    }

    /**
     * 设置 dialog 的高度占屏百分比
     *
     * @param percnet 高度占屏百分比
     */
    public void setHeightPercent(float percnet) {
        mHeightPercent = percnet;
    }

    /**
     * 设置 dialog 的宽
     *
     * @param width
     */
    public void setWidth(int width) {
        this.mWidth = width;
    }

    /**
     * 设置 dialog 的高
     *
     * @param height
     */
    public void setHeight(int height) {
        this.mHeight = height;
    }

    /**
     * 设置屏幕的背景透明度
     *
     * @param alpha 透明度
     */
    public void setVisibleAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        this.mVisibleAlpha = alpha;
    }

    @Override
    public void onStart() {
        super.onStart();
        setBgAlpha(mVisibleAlpha);
        if (isSetSize) {
            setDialogSize(mWidth, mHeight);
        } else {
            if (isUseSizePercent) {
                setDialogSizePercent(mWidhtPercent, mHeightPercent);
            }
        }
    }

    /**
     * 设置 dialog 的宽高占屏比
     *
     * @param widthper  dialog 宽度的占屏比
     * @param heightper dialog 高度的占屏比
     */
    private void setDialogSizePercent(float widthper, float heightper) {
        if (widthper < 0) {
            widthper = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        if (heightper < 0) {
            heightper = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setDialogSize(widthper == ViewGroup.LayoutParams.WRAP_CONTENT ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (metrics.widthPixels * widthper),
                heightper == ViewGroup.LayoutParams.WRAP_CONTENT ? ViewGroup.LayoutParams.WRAP_CONTENT : (int) (metrics.heightPixels * heightper));
    }

    /**
     * 设置 dialog 的宽高
     *
     * @param width
     * @param height
     */
    private void setDialogSize(int width, int height) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = width;
            lp.height = height;
            dialog.getWindow().setAttributes(lp);
        }
    }

    /**
     * 设置屏幕的背景透明度
     *
     * @param alpha 0-1（0：屏幕完全透明，1：背景最暗）
     */
    private void setBgAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (alpha < 0) {
            alpha = 0;
        }
        if (alpha > 1) {
            alpha = 1;
        }
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = alpha;
        lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }
}
