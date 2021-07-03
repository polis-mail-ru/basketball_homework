package ru.ok.technopolis.basketball.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

import ru.ok.technopolis.basketball.R;

public class ScoreCounter extends View implements TallyCounter {

    private static final int MAX_COUNT = 9999;
    private static final String MAX_COUNT_STRING = String.valueOf(MAX_COUNT);

    private int count;
    private String displayedCount;

    private TextPaint numberPaint;

    public ScoreCounter(Context context) {
        this(context, null);
    }

    public ScoreCounter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        numberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        TypedArray typedArray = context
                .obtainStyledAttributes(attrs, R.styleable.ScoreCounter, 0, 0);

        int textColor = typedArray.getColor(R.styleable.ScoreCounter_android_textColor,
                ContextCompat.getColor(context, android.R.color.black));
        float textSize = Math.round(
                typedArray.getDimensionPixelSize(R.styleable.ScoreCounter_android_textSize,
                        Math.round(64f * getResources().getDisplayMetrics().scaledDensity)));
        numberPaint.setColor(textColor);
        numberPaint.setTextSize(textSize);
        numberPaint.setTypeface(Typeface.SANS_SERIF);

        typedArray.recycle();
        setCount(0);
    }

    @Override
    public void reset() {
        setCount(0);
    }

    @Override
    public void increment() {
        setCount(count + 1);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        count = Math.min(count, MAX_COUNT);
        this.count = count;
        this.displayedCount = String.format(Locale.getDefault(), "%04d", count);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();

        float maxTextWidth = numberPaint.measureText(MAX_COUNT_STRING);
        float maxTextHeight = fontMetrics.bottom - fontMetrics.top;

        int desiredWidth = Math.round(maxTextWidth + getPaddingLeft() + getPaddingRight());
        int desiredHeight = Math.round(maxTextHeight * 2f + getPaddingTop() + getPaddingBottom());

        int measuredWidth = resolveSize(desiredWidth, widthMeasureSpec);
        int measuredHeight = resolveSize(desiredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();
        float centerX = viewWidth * 0.5f;

        float baselineY = Math.round(viewHeight * 0.6f);
        float textWidth = numberPaint.measureText(displayedCount);
        float textX = Math.round(centerX - textWidth * 0.5f);
        canvas.drawText(displayedCount, textX, baselineY, numberPaint);
    }
}
