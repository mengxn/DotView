package me.codego.view;

import static me.codego.view.DotType.PLUS;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import me.codego.dotview.R;


/**
 * Created by mengxn on 17-3-3.
 */
public class DotView extends TextView {

    private Paint mPaint;
    /**
     * 圆点半径
     */
    private int mDotRadius;
    /**
     * 数值超大时（>99）显示类型
     */
    private DotType mDotType = PLUS;
    private final RectF mDotRect = new RectF();

    /**
     * 默认圆点半径
     */
    private static final int DEFAULT_RADIUS = 3;
    /**
     * 最大显示数值
     */
    private static final int NUMBER_LIMIT = 99;

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotView, 0, 0);
        int color = typedArray.getColor(R.styleable.DotView_dotColor, Color.RED);
        float density = getResources().getDisplayMetrics().density;
        mDotRadius = typedArray.getDimensionPixelSize(R.styleable.DotView_dotRadius, (int) (DEFAULT_RADIUS * density));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);

        setGravity(Gravity.CENTER);

        if (isInEditMode()) {
            mDotRadius = ((int) (3 * density));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(mDotRect, mDotRect.height(), mDotRect.height(), mPaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getText().length() > 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (getMeasuredWidth() < getMeasuredHeight()) {
                int newMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
                super.onMeasure(newMeasureSpec, heightMeasureSpec);
            }
        } else {
            int newMeasureSpec = MeasureSpec.makeMeasureSpec(mDotRadius * 2, MeasureSpec.EXACTLY);
            super.onMeasure(newMeasureSpec, newMeasureSpec);
        }
        mDotRect.right = getMeasuredWidth();
        mDotRect.bottom = getMeasuredHeight();
    }

    public void setDotType(DotType type) {
        mDotType = type;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(getNumberText(text), type);
    }

    private String getNumberText(CharSequence text) {
        final String textStr = text.toString();
        if (TextUtils.isEmpty(text) || !textStr.matches("\\d+")) {
            return "";
        }
        if (isLegalNumber(textStr)) {
            return textStr;
        }
        switch (mDotType != null ? mDotType : PLUS) {
            case ELLIPSIS:
                return "...";
            case PLUS:
            default:
                return "99+";
        }
    }

    private boolean isLegalNumber(String text) {
        try {
            int number = Integer.parseInt(text);
            if (number >= 0 && number <= NUMBER_LIMIT) {
                return true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

}
