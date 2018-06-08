package me.codego.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import me.codego.dotview.R;

/**
 * 提示布局
 * Created by mengxn on 2017/8/21.
 */

public class DotLayout extends FrameLayout {

    private Paint mPaint;
    private Paint mTextPaint;
    private int mDotPadding;
    private int mDotLocation;
    private float mTextSize;
    private int mDotOverPadding;
    private int mNumber;
    private boolean isShow;

    private static final String DEFAULT_OVER_TEXT = "...";
    private static final int DEFAULT_PADDING = 3;
    private static final int DEFAULT_OVER_PADDING = 3;
    private static final String DEFAULT_TEXT = "88";
    private static final int DEFAULT_TEXT_SIZE = 10;

    private static final int LOCATION_RIGHT = 0;
    private static final int LOCATION_LEFT = 1;


    public DotLayout(@NonNull Context context) {
        this(context, null);
    }

    public DotLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotLayout, 0, 0);
        int color = typedArray.getColor(R.styleable.DotLayout_dotColor, Color.RED);
        int textColor = typedArray.getColor(R.styleable.DotLayout_dotTextColor, Color.WHITE);
        float density = getResources().getDisplayMetrics().density;
        mDotPadding = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotPadding, (int) (DEFAULT_PADDING * density));
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotTextSize, (int) (DEFAULT_TEXT_SIZE * density));
        mDotOverPadding = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotOverPadding, (int) (DEFAULT_OVER_PADDING * density));
        mDotLocation = typedArray.getInt(R.styleable.DotLayout_dotLocation, LOCATION_RIGHT);
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(mTextSize);

        setWillNotDraw(false);

        // 修改padding值以便显示红点
        if (mDotLocation == LOCATION_LEFT) {
            final int padding = mDotOverPadding + mDotPadding * 2;
            if (getPaddingLeft() < padding) {
                setPadding(padding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            }
        } else {
            if (getPaddingTop() < mDotOverPadding || getPaddingRight() < mDotOverPadding) {
                setPadding(getPaddingLeft(), Math.max(getPaddingTop(), mDotOverPadding), Math.max(getPaddingRight(), mDotOverPadding), getPaddingBottom());
            }
        }

        if (isInEditMode()) {
            isShow = true;
            mNumber = 35;
        }
    }

    public void setDotColor(int color) {
        mPaint.setColor(color);
    }

    public void setDotTextColor(int color) {
        mTextPaint.setColor(color);
    }

    public void setDotTextSize(float textSize) {
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics()));
    }

    public void setDotOverPadding(float padding) {
        mDotOverPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, padding, getResources().getDisplayMetrics());
    }

    public void setDotPadding(float padding) {
        mDotPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, padding, getResources().getDisplayMetrics());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // draw dot when isShow is true and has child
        if (isShow && getChildCount() > 0) {
            if (mDotLocation == LOCATION_LEFT) {
                drawLeft(canvas);
            } else {
                drawRight(canvas);
            }
        }
    }

    /**
     * 只支持点显示，不显示具体数据
     *
     * @param canvas
     */
    private void drawLeft(Canvas canvas) {
        canvas.save();
        final int radius = mDotPadding;
        final View childView = getChildAt(0);
        canvas.translate(childView.getLeft() - radius * 2 - mDotOverPadding, (childView.getTop() + childView.getBottom()) / 2 - radius);
        canvas.drawCircle(radius, radius, radius, mPaint);
        canvas.restore();
    }

    private void drawRight(Canvas canvas) {
        canvas.save();
        final View childView = getChildAt(0);

        // 画点
        int radius = getDotRadius();
        canvas.translate(childView.getRight() - radius * 2 + mDotOverPadding, childView.getTop() - mDotOverPadding);
        canvas.drawCircle(radius, radius, radius, mPaint);

        // 如果 mNumber > 0, 将数据画出来
        if (mNumber > 0) {
            String text;
            if (mNumber <= 99) {
                text = String.valueOf(mNumber);
            } else {
                text = DEFAULT_OVER_TEXT;
            }
            final float textWidth = mTextPaint.measureText(text);
            final Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            final float textHeight = fontMetrics.descent - fontMetrics.ascent;
            final float translateY = radius * 2 - (radius * 2 - textHeight) / 2 - fontMetrics.descent;
            canvas.translate(radius - textWidth / 2, translateY);
            canvas.drawText(text, 0, 0, mTextPaint);
        }

        canvas.restore();
    }

    // 计算点半径
    private int getDotRadius() {
        int radius = mDotPadding;
        if (mNumber > 0) {
            final float textWidth = mTextPaint.measureText(DEFAULT_TEXT);
            final Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            final float textHeight = fontMetrics.descent - fontMetrics.ascent;
            radius += Math.max(textWidth, textHeight) / 2;
        }
        return radius;
    }

    /**
     * 设置显示数据
     *
     * @param number (0,99] 显示具体数据
     *               (-, 0] 只显示点
     *               (99,+) ...
     */
    public void setNumber(int number) {
        mNumber = number;
    }

    /**
     * 是否显示提示
     *
     * @param isShow 是否显示
     */
    public void show(boolean isShow) {
        show(isShow, mNumber);
    }

    /**
     * 是否显示提示
     *
     * @param isShow 是否显示
     * @param number 提示数据 {@link #setNumber(int)}
     */
    public void show(boolean isShow, int number) {
        this.isShow = isShow;
        this.mNumber = isShow ? number : 0;
        postInvalidate();
    }
}
