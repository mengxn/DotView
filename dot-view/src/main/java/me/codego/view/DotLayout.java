package me.codego.view;

import static me.codego.view.DotType.PLUS;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
    private int mDotPaddingHorizontal;
    private int mDotPaddingVertical;
    private int mDotMarginTop;
    private int mDotMarginEnd;
    private int mDotLocation;
    private float mTextSize;
    private int mDotRadius;
    private DotType mDotType = PLUS;
    private int mNumber;
    private boolean isShow;

    private static final int DEFAULT_PADDING = 3;
    private static final int DEFAULT_OVER_PADDING = 3;
    private static final int DEFAULT_TEXT_SIZE = 10;

    private static final int LOCATION_RIGHT = 0;
    private static final int LOCATION_LEFT = 1;

    public DotLayout(Context context) {
        this(context, null);
    }

    public DotLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotLayout, 0, 0);
        int color = typedArray.getColor(R.styleable.DotLayout_dotColor, Color.RED);
        int textColor = typedArray.getColor(R.styleable.DotLayout_dotTextColor, Color.WHITE);
        float density = getResources().getDisplayMetrics().density;
        mDotPaddingHorizontal = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotPaddingHorizontal, (int) (DEFAULT_PADDING * density));
        mDotPaddingVertical = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotPaddingVertical, (int) (DEFAULT_PADDING * density));
        mDotMarginTop = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotMarginTop, 0);
        mDotMarginEnd = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotMarginEnd, 0);
        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotTextSize, (int) (DEFAULT_TEXT_SIZE * density));
        mDotRadius = typedArray.getDimensionPixelOffset(R.styleable.DotLayout_dotRadius, (int) (DEFAULT_OVER_PADDING * density));
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
            final int padding = mDotRadius * 2 + mDotPaddingHorizontal;
            if (getPaddingLeft() < padding) {
                setPadding(padding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            }
        }

        if (isInEditMode()) {
            isShow = true;
            mNumber = 35;
        }
    }

    /**
     * 设置背景颜色
     */
    public void setDotColor(int color) {
        mPaint.setColor(color);
        postInvalidate();
    }

    /**
     * 设置字体颜色
     */
    public void setDotTextColor(int color) {
        mTextPaint.setColor(color);
        postInvalidate();
    }

    /**
     * 设置字体大小
     */
    public void setDotTextSize(float textSize) {
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics()));
        postInvalidate();
    }

    /**
     * 设置圆点半径
     */
    public void setDotRadius(float radius) {
        mDotRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, getResources().getDisplayMetrics());
        postInvalidate();
    }

    /**
     * 设置圆点padding
     */
    public void setDotPadding(int horizontal, int vertical) {
        mDotPaddingHorizontal = horizontal;
        mDotPaddingVertical = vertical;
        postInvalidate();
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
        final int radius = mDotRadius;
        final View childView = getChildAt(0);
        canvas.translate(childView.getLeft() - radius * 2 - mDotPaddingHorizontal, (childView.getTop() + childView.getBottom()) / 2f - radius);
        canvas.drawCircle(radius, radius, radius, mPaint);
        canvas.restore();
    }

    private void drawRight(Canvas canvas) {
        canvas.save();
        final View childView = getChildAt(0);

        // 画点
        RectF rect = getDotRect();
        canvas.translate(childView.getRight() - rect.width() - mDotMarginEnd, childView.getTop() + mDotMarginTop);
        canvas.drawRoundRect(rect, rect.height(), rect.height(), mPaint);

        // 如果 mNumber > 0, 将数据画出来
        if (isShowNumber()) {
            String text = getNumberText();
            final float textWidth = mTextPaint.measureText(text);
            final Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            final float textHeight = fontMetrics.descent - fontMetrics.ascent;
            final float translateY = rect.height() - (rect.height() - textHeight) / 2 - fontMetrics.descent;
            canvas.translate((rect.width() - textWidth) / 2, translateY);
            canvas.drawText(text, 0, 0, mTextPaint);
        }

        canvas.restore();
    }

    private RectF getDotRect() {
        if (isShowNumber()) {
            float textWidth = mTextPaint.measureText(getNumberText()) + mDotPaddingHorizontal * 2;
            final Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            final float textHeight = fontMetrics.descent - fontMetrics.ascent + mDotPaddingVertical * 2;
            if (textWidth < textHeight) {
                textWidth = textHeight;
            }
            return new RectF(0, 0, textWidth, textHeight);
        } else {
            return new RectF(0, 0, mDotRadius * 2, mDotRadius * 2);
        }
    }


    public void setDotType(DotType type) {
        mDotType = type;
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

    private boolean isShowNumber() {
        return mNumber > 0;
    }

    private String getNumberText() {
        if (mNumber <= 99) {
            return String.valueOf(mNumber);
        } else {
            switch (mDotType != null ? mDotType : PLUS) {
                case ELLIPSIS:
                    return "...";
                case PLUS:
                default:
                    return "99+";
            }
        }
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

    /**
     * 是否已显示提示
     *
     * @return
     */
    public boolean isShowing() {
        return this.isShow;
    }

}
