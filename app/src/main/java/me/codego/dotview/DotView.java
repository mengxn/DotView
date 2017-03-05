package me.codego.dotview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;


/**
 * Created by mengxn on 17-3-3.
 */
public class DotView extends TextView {

    private Paint mPaint;
    private int mDotPadding;

    private static final String DEFAULT_TEXT = "...";

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DotView, 0, 0);
        int color = typedArray.getColor(R.styleable.DotView_dotColor, Color.RED);
        float density = getResources().getDisplayMetrics().density;
        mDotPadding = typedArray.getDimensionPixelSize(R.styleable.DotView_dotPadding, (int) (3 * density));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);

        setPadding(0, 0, 0, 0);
        setGravity(Gravity.CENTER);
//        setSingleLine();
//        setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
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
        int width;
        if (getText().length() == 0) {
            width = 10;
        } else {
            validate();
            float textWidth = getPaint().measureText("88");
            Paint.FontMetrics fontMetrics = getPaint().getFontMetrics();
            float textHeight = fontMetrics.descent - fontMetrics.ascent;
            width = (int) (textWidth > textHeight ? textWidth : textHeight);
        }
        width += mDotPadding * 2;

        int newMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        super.onMeasure(newMeasureSpec, newMeasureSpec);
    }

    private void validate() {
        String text = getText().toString();
        if (!isNumber(text) || !isLegalNumber(Integer.valueOf(text))) {
            setText(DEFAULT_TEXT);
        }
    }

    private boolean isNumber(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        return text.matches("\\d+");
    }

    private boolean isLegalNumber(int number) {
        return number >= 0 && number < 100;
    }
}
