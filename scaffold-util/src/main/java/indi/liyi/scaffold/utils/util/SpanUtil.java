package indi.liyi.scaffold.utils.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM;


public class SpanUtil {
    private final int DEF_SPAN_FLAG = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
    private int mSpanFlag;
    private SpannableStringBuilder mBuilder;
    private Context mContext;

    private SpanUtil(@NonNull Context context) {
        super();
        this.mContext = context;
        mSpanFlag = DEF_SPAN_FLAG;
        mBuilder = new SpannableStringBuilder();
    }

    /**
     * Create a spanutil instance
     *
     * @return {@link SpanUtil}
     */
    public static SpanUtil newInstance(@NonNull Context context) {
        return new SpanUtil(context);
    }

    /**
     * @param flag <ul>
     *             <li>{@link Spannable#SPAN_EXCLUSIVE_EXCLUSIVE}:
     *             <p>前后不包括在内，也就是说，在前面和后面插入的新字符不会应用新样式</p>
     *             </li>
     *             <li>{@link Spannable#SPAN_EXCLUSIVE_INCLUSIVE}:
     *             <p>不包括前面，但是包括后面，在后面插入的新字符会应用新样式</p>
     *             </li>
     *             <li>{@link Spannable#SPAN_INCLUSIVE_EXCLUSIVE}:
     *             <p>包括前面，不包括后面</p>
     *             </li>
     *             <li>{@link Spannable#SPAN_INCLUSIVE_INCLUSIVE}:
     *             <p>同时包括前面和后面</p>
     *             </li>
     *             </ul>
     * @return {@link SpanUtil}
     */
    public SpanUtil setFlag(int flag) {
        this.mSpanFlag = flag;
        return this;
    }

    /**
     * Add text content
     *
     * @param text The text content
     * @return {@link SpanUtil}
     */
    public SpanUtil appendText(CharSequence text) {
        mBuilder.append(text);
        return this;
    }

    /**
     * Set font size
     *
     * @param size  The font size
     * @param start The start position
     * @param end   The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontSize(int size, int start, int end) {
        mBuilder.setSpan(new AbsoluteSizeSpan(size), start, end, mSpanFlag);
        return this;
    }

    /**
     * Set font size
     *
     * @param size The font size
     * @param key  The specified string in the text
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontSizeByKey(int size, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] item : list) {
            setFontSize(size, item[0], item[1]);
        }
        return this;
    }

    /**
     * Set font size
     *
     * @param size       The font size
     * @param key        The specified string in the text
     * @param indexArray The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontSizeByKey(int size, @NonNull String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setFontSize(size, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }


    /**
     * Set font color
     *
     * @param color The font color
     * @param start The start position
     * @param end   The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontColor(@ColorInt int color, int start, int end) {
        mBuilder.setSpan(new ForegroundColorSpan(color), start, end, mSpanFlag);
        return this;
    }

    /**
     * Set font color
     *
     * @param color The font color
     * @param key   The specified string in the text
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontColorByKey(@ColorInt int color, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] item : list) {
            setFontColor(color, item[0], item[1]);
        }
        return this;
    }

    /**
     * Set font color
     *
     * @param color      The font color
     * @param key        The specified string in the text
     * @param indexArray The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setFontColorByKey(@ColorInt int color, String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setFontColor(color, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Set the background color
     *
     * @param color The background color
     * @param start The start position
     * @param end   The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setBgColor(@ColorInt int color, int start, int end) {
        mBuilder.setSpan(new BackgroundColorSpan(color), start, end, mSpanFlag);
        return this;
    }

    /**
     * Set the background color
     *
     * @param color The background color
     * @param key   The specified string in the text
     * @return {@link SpanUtil}
     */
    public SpanUtil setBgColorByKey(@ColorInt int color, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] item : list) {
            setBgColor(color, item[0], item[1]);
        }
        return this;
    }

    /**
     * Set the background color
     *
     * @param color      The background color
     * @param key        The specified string in the text
     * @param indexArray The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setBgColorByKey(@ColorInt int color, @NonNull String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setBgColor(color, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Add url links to text
     *
     * @param url
     * @param start The start position
     * @param end   The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setURL(@Nullable String url, int start, int end) {
        mBuilder.setSpan(new URLSpan(url), start, end, mSpanFlag);
        return this;
    }

    /**
     * Add url links to text
     *
     * @param url
     * @param key The specified string in the text
     * @return {@link SpanUtil}
     */
    public SpanUtil setURLByKey(@Nullable String url, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] item : list) {
            setURL(url, item[0], item[1]);
        }
        return this;
    }

    /**
     * Add url links to text
     *
     * @param url
     * @param key        The specified string in the text
     * @param indexArray The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setURLByKey(@Nullable String url, @NonNull String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setURL(url, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Set font styles, such as italics
     *
     * @param style The font style
     *              <ul>
     *              <li>{@link Typeface#NORMAL}</li>
     *              <li>{@link Typeface#BOLD}</li>
     *              <li>{@link Typeface#ITALIC}</li>
     *              <li>{@link Typeface#BOLD_ITALIC}</li>
     *              </ul>
     * @param start The start position
     * @param end   The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setTypeface(int style, int start, int end) {
        mBuilder.setSpan(new StyleSpan(style), start, end, mSpanFlag);
        return this;
    }

    /**
     * Set font styles, such as italics
     *
     * @param style The font style
     *              <ul>
     *              <li>{@link Typeface#NORMAL}</li>
     *              <li>{@link Typeface#BOLD}</li>
     *              <li>{@link Typeface#ITALIC}</li>
     *              <li>{@link Typeface#BOLD_ITALIC}</li>
     *              </ul>
     * @param key   The specified string in the text
     * @return {@link SpanUtil}
     */
    public SpanUtil setTypefaceByKey(int style, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setTypeface(style, index[0], index[1]);
        }
        return this;
    }

    /**
     * Set font styles, such as italics
     *
     * @param style      The font style <ul>
     *                   <li>{@link Typeface#NORMAL}</li>
     *                   <li>{@link Typeface#BOLD}</li>
     *                   <li>{@link Typeface#ITALIC}</li>
     *                   <li>{@link Typeface#BOLD_ITALIC}</li>
     *                   </ul>
     * @param key        The specified string in the text
     * @param indexArray The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setTypefaceByKey(int style, @NonNull String key, int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setTypeface(style, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Add deletion lines to text
     *
     * @param start The start position
     * @param end   The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setStrikethrough(int start, int end) {
        mBuilder.setSpan(new StrikethroughSpan(), start, end, mSpanFlag);
        return this;
    }

    /**
     * Add deletion lines to text
     *
     * @param key The specified string in the text
     * @return {@link SpanUtil}
     */
    public SpanUtil setStrikethroughByKey(@NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setStrikethrough(index[0], index[1]);
        }
        return this;
    }

    /**
     * Add deletion lines to text
     *
     * @param key        The specified string in the text
     * @param indexArray The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setStrikethroughByKey(@NonNull String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setStrikethrough(list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param span
     * @param start The start position
     * @param end   The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(ImageSpan span, int start, int end) {
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param span
     * @param key  The specified string in the text
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(ImageSpan span, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setImage(span, index[0], index[1]);
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param span
     * @param key        The specified string in the text
     * @param indexArray The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(ImageSpan span, @NonNull String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setImage(span, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param bitmap
     * @param start  The start position
     * @param end    The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(Bitmap bitmap, int start, int end) {
        return setImage(bitmap, ALIGN_BOTTOM, start, end);
    }

    /**
     * Replace the specified text with an image
     *
     * @param bitmap
     * @param verticalAlignment Image alignment in the text
     *                          <ul>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BOTTOM}: default</li>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BASELINE}</li>
     *                          </ul>
     * @param start             The start position
     * @param end               The end position
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(Bitmap bitmap, int verticalAlignment, int start, int end) {
        ImageSpan span = new ImageSpan(mContext, bitmap, verticalAlignment);
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param bitmap
     * @param key    The specified string in the text
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Bitmap bitmap, @NonNull String key) {
        return setImageByKey(bitmap, ALIGN_BOTTOM, key);
    }

    /**
     * Replace the specified text with an image
     *
     * @param bitmap
     * @param verticalAlignment Image alignment in the text
     *                          <ul>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BOTTOM}: default</li>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BASELINE}</li>
     *                          </ul>
     * @param key
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Bitmap bitmap, int verticalAlignment, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setImage(bitmap, verticalAlignment, index[0], index[1]);
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param bitmap
     * @param key
     * @param indexArray
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Bitmap bitmap, @NonNull String key, @IntRange(from = 0) int... indexArray) {
        return setImageByKey(bitmap, ALIGN_BOTTOM, key, indexArray);
    }

    /**
     * Replace the specified text with an image
     *
     * @param bitmap
     * @param verticalAlignment Image alignment in the text
     *                          <ul>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BOTTOM}: default</li>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BASELINE}</li>
     *                          </ul>
     * @param key               The specified string in the text
     * @param indexArray        The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Bitmap bitmap, int verticalAlignment, @NonNull String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setImage(bitmap, verticalAlignment, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param drawable
     * @param start
     * @param end
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(Drawable drawable, int start, int end) {
        return setImage(drawable, ALIGN_BOTTOM, start, end);
    }

    /**
     * Replace the specified text with an image
     *
     * @param drawable
     * @param verticalAlignment
     * @param start
     * @param end
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(Drawable drawable, int verticalAlignment, int start, int end) {
        ImageSpan span = new ImageSpan(drawable, verticalAlignment);
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param drawable
     * @param key
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Drawable drawable, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setImage(drawable, ALIGN_BOTTOM, index[0], index[1]);
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param drawable
     * @param verticalAlignment
     * @param key
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Drawable drawable, int verticalAlignment, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setImage(drawable, verticalAlignment, index[0], index[1]);
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param drawable
     * @param key
     * @param indexArray
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Drawable drawable, @NonNull String key, @IntRange(from = 0) int... indexArray) {
        return setImageByKey(drawable, ALIGN_BOTTOM, key, indexArray);
    }

    /**
     * Replace the specified text with an image
     *
     * @param drawable
     * @param verticalAlignment Image alignment in the text
     *                          <ul>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BOTTOM}: default</li>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BASELINE}</li>
     *                          </ul>
     * @param key               The specified string in the text
     * @param indexArray        The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Drawable drawable, int verticalAlignment, String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setImage(drawable, verticalAlignment, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param resourceId
     * @param start
     * @param end
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(@DrawableRes int resourceId, int start, int end) {
        return setImage(resourceId, ALIGN_BOTTOM, start, end);
    }

    /**
     * Replace the specified text with an image
     *
     * @param resourceId
     * @param verticalAlignment
     * @param start
     * @param end
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(@DrawableRes int resourceId, int verticalAlignment, int start, int end) {
        ImageSpan span = new ImageSpan(mContext, resourceId, verticalAlignment);
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param resourceId
     * @param key
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(@DrawableRes int resourceId, @NonNull String key) {
        return setImageByKey(resourceId, ALIGN_BOTTOM, key);
    }

    /**
     * Replace the specified text with an image
     *
     * @param resourceId
     * @param verticalAlignment
     * @param key
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(@DrawableRes int resourceId, int verticalAlignment, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setImage(resourceId, verticalAlignment, index[0], index[1]);
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param resourceId
     * @param key
     * @param indexArray
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(@DrawableRes int resourceId, String key, @IntRange(from = 0) int... indexArray) {
        return setImageByKey(resourceId, ALIGN_BOTTOM, key, indexArray);
    }

    /**
     * Replace the specified text with an image
     *
     * @param resourceId
     * @param verticalAlignment Image alignment in the text
     *                          <ul>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BOTTOM}: default</li>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BASELINE}</li>
     *                          </ul>
     * @param key               The specified string in the text
     * @param indexArray        The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(@DrawableRes int resourceId, int verticalAlignment, String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                setImage(resourceId, verticalAlignment, list.get(index)[0], list.get(index)[1]);
            }
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param uri
     * @param start
     * @param end
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(Uri uri, int start, int end) {
        return setImage(uri, ALIGN_BOTTOM, start, end);
    }

    /**
     * Replace the specified text with an image
     *
     * @param uri
     * @param verticalAlignment
     * @param start
     * @param end
     * @return {@link SpanUtil}
     */
    public SpanUtil setImage(Uri uri, int verticalAlignment, int start, int end) {
        ImageSpan span = new ImageSpan(mContext, uri, verticalAlignment);
        mBuilder.setSpan(span, start, end, mSpanFlag);
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param uri
     * @param key
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Uri uri, @NonNull String key) {
        return setImageByKey(uri, ALIGN_BOTTOM, key);
    }

    /**
     * Replace the specified text with an image
     *
     * @param uri
     * @param verticalAlignment
     * @param key
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Uri uri, int verticalAlignment, @NonNull String key) {
        List<int[]> list = searchAllIndex(key);
        for (int[] index : list) {
            setImage(uri, verticalAlignment, index[0], index[1]);
        }
        return this;
    }

    /**
     * Replace the specified text with an image
     *
     * @param uri
     * @param key
     * @param indexArray
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Uri uri, String key, @IntRange(from = 0) int... indexArray) {
        return setImageByKey(uri, ALIGN_BOTTOM, key, indexArray);
    }

    /**
     * Replace the specified text with an image
     *
     * @param uri
     * @param verticalAlignment Image alignment in the text
     *                          <ul>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BOTTOM}: default</li>
     *                          <li>{@link DynamicDrawableSpan#ALIGN_BASELINE}</li>
     *                          </ul>
     * @param key               The specified string in the text
     * @param indexArray        The sequential location of The specified string in the text. (Start from 0)
     * @return {@link SpanUtil}
     */
    public SpanUtil setImageByKey(Uri uri, int verticalAlignment, String key, @IntRange(from = 0) int... indexArray) {
        List<int[]> list = searchAllIndex(key);
        for (int index : indexArray) {
            if (index >= 0 && index < list.size()) {
                ImageSpan span = new ImageSpan(mContext, uri, verticalAlignment);
                mBuilder.setSpan(span, list.get(index)[0], list.get(index)[1], mSpanFlag);
            }
        }
        return this;
    }

    /**
     * Add a click event to the specified text
     *
     * @param start
     * @param end
     * @param listener
     * @return {@link SpanUtil}
     */
    public SpanUtil setClick(int start, int end, OnTextClickListener listener) {
        return setClick(start, end, true, listener);
    }

    /**
     * Add a click event to the specified text
     *
     * @param start           The start position
     * @param end             The end position
     * @param isNeedUnderLine True to add the underline ,false otherwise
     * @param listener        The click event
     * @return {@link SpanUtil}
     */
    public SpanUtil setClick(int start, int end, final boolean isNeedUnderLine, final OnTextClickListener listener) {
        mBuilder.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(isNeedUnderLine);
            }

            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    listener.onTextClick(-1, widget);
                }
            }
        }, start, end, mSpanFlag);
        return this;
    }


    /**
     * Add a click event to the specified text
     *
     * @param start
     * @param end
     * @param lineColor
     * @param listener
     * @return {@link SpanUtil}
     */
    public SpanUtil setClick(int start, int end, @ColorInt final int lineColor, final OnTextClickListener listener) {
        return setClick(start, end, true, lineColor, listener);
    }

    /**
     * Add a click event to the specified text
     *
     * @param start           The start position
     * @param end             The end position
     * @param isNeedUnderLine True to add the underline, false otherwise
     * @param lineColor       The underline color
     * @param listener        The click event
     * @return {@link SpanUtil}
     */
    public SpanUtil setClick(int start, int end, final boolean isNeedUnderLine, @ColorInt final int lineColor, final OnTextClickListener listener) {
        mBuilder.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(isNeedUnderLine);
                if (isNeedUnderLine) {
                    ds.setColor(lineColor);
                }
            }

            @Override
            public void onClick(View widget) {
                if (listener != null) {
                    listener.onTextClick(-1, widget);
                }
            }
        }, start, end, mSpanFlag);
        return this;
    }

    /**
     * Add a click event to the specified text
     *
     * @param key             The specified string in the text
     * @param isNeedUnderLine True to add the underline, false otherwise
     * @param listener        The click event
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(@NonNull String key, final boolean isNeedUnderLine, final OnTextClickListener listener) {
        List<int[]> list = searchAllIndex(key);
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isNeedUnderLine);
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(finalI, widget);
                    }
                }
            }, list.get(i)[0], list.get(i)[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Add a click event to the specified text
     *
     * @param key       The specified string in the text
     * @param lineColor The underline color
     * @param listener  The click event
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(@NonNull String key, @ColorInt final int lineColor, final OnTextClickListener listener) {
        return setClickByKey(key, true, lineColor, listener);
    }

    /**
     * Add a click event to the specified text
     *
     * @param key             The specified string in the text
     * @param isNeedUnderLine True to add the underline, false otherwise
     * @param lineColor       The underline color
     * @param listener        The click event
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(@NonNull String key, final boolean isNeedUnderLine, @ColorInt final int lineColor, final OnTextClickListener listener) {
        List<int[]> list = searchAllIndex(key);
        for (int i = 0; i < list.size(); i++) {
            final int finalI = i;
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isNeedUnderLine);
                    if (isNeedUnderLine) {
                        ds.setColor(lineColor);
                    }
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(finalI, widget);
                    }
                }
            }, list.get(i)[0], list.get(i)[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Add a click event to the specified text
     *
     * @param key             The specified string in the text
     * @param index           The sequential location of The specified string in the text. (Start from 0)
     * @param isNeedUnderLine True to add the underline, false otherwise
     * @param listener        The click event
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, final int index, final boolean isNeedUnderLine, final OnTextClickListener listener) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isNeedUnderLine);
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(index, widget);
                    }
                }
            }, list.get(index)[0], list.get(index)[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Add a click event to the specified text
     *
     * @param key       The specified string in the text
     * @param index     The sequential location of The specified string in the text. (Start from 0)
     * @param lineColor The underline color
     * @param listener  The click event
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, final int index, @ColorInt final int lineColor, final OnTextClickListener listener) {
        return setClickByKey(key, index, true, lineColor, listener);
    }

    /**
     * Add a click event to the specified text
     *
     * @param key             The specified string in the text
     * @param index           The sequential location of The specified string in the text. (Start from 0)
     * @param isNeedUnderLine True to add the underline, false otherwise
     * @param lineColor       The underline color
     * @param listener        The click event
     * @return {@link SpanUtil}
     */
    public SpanUtil setClickByKey(String key, final int index, final boolean isNeedUnderLine, @ColorInt final int lineColor, final OnTextClickListener listener) {
        List<int[]> list = searchAllIndex(key);
        if (index >= 0 && index < list.size()) {
            mBuilder.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(isNeedUnderLine);
                    if (isNeedUnderLine) {
                        ds.setColor(lineColor);
                    }
                }

                @Override
                public void onClick(View widget) {
                    if (listener != null) {
                        listener.onTextClick(index, widget);
                    }
                }
            }, list.get(index)[0], list.get(index)[1], mSpanFlag);
        }
        return this;
    }

    /**
     * Set the spannableString to be displayed
     *
     * @param textView
     */
    public void build(TextView textView) {
        if (textView != null) {
            textView.setText(mBuilder);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /**
     * Return a SpannableStringBuilder
     *
     * @return SpannableStringBuilder
     */
    public SpannableStringBuilder getBuilder() {
        if (mBuilder == null) return null;
        return mBuilder;
    }

    /**
     * Query the location of all keywords in a string
     *
     * @param key The specified string in the text
     * @return Collection of keyword locations
     */
    public List<int[]> searchAllIndex(@NonNull String key) {
        List<int[]> indexList = new ArrayList<int[]>();
        String text = mBuilder.toString();
        if (TextUtils.isEmpty(text)) {
            return indexList;
        }
        int a = text.indexOf(key);
        while (a != -1) {
            int[] index = new int[]{a, a + key.length()};
            indexList.add(index);
            a = text.indexOf(key, a + 1);
        }
        return indexList;
    }


    public interface OnTextClickListener {
        /**
         * @param position The position of the clicked text in the string
         * @param view
         */
        void onTextClick(int position, View view);
    }
}
