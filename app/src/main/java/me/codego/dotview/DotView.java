package me.codego.dotview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;


/**
 * Created by mengxn on 17-3-3.
 */
public class DotView extends TextView {

    private Paint mPaint;
    private int mDotPadding;

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotView, 0, 0);
        int color = typedArray.getColor(R.styleable.DotView_dotColor, 0xffff0000);
        mDotPadding = typedArray.getDimensionPixelSize(R.styleable.DotView_dotPadding, 10);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);

        setPadding(0, 0, 0, 0);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        int radius = getWidth()/2;
        canvas.drawCircle(radius, radius, radius, mPaint);
        canvas.restore();
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        if (getText().length() == 0) {
            width = 30;
        } else {
            float textWidth = getPaint().measureText("88");

            Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
            float textHeight = fontMetrics.descent - fontMetrics.ascent;

            if (textWidth > textHeight) {
                width = (int) (textWidth + mDotPadding * 2);
            } else {
                width = (int) (textHeight + mDotPadding * 2);
            }
        }

        int newMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        setMeasuredDimension(newMeasureSpec, newMeasureSpec);
    }
}
