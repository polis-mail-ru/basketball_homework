package ru.ok.technopolis.basketball.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;


public class CounterView extends View implements Counter{

    private static final int MAX_COUNT = 100;
    private int count;
    private String displayedCount;
    private TextPaint numberPaint = new TextPaint(Paint.HINTING_ON);

    public CounterView(Context context) {
        super(context);
    }

    public CounterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        numberPaint.setColor(ContextCompat.getColor(context, android.R.color.black));
        numberPaint.setTextSize(Math.round(64f * getResources().getDisplayMetrics().scaledDensity));

        setCount(0);
    }

    public CounterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        displayedCount = String.format(Locale.getDefault(), "%03d", count);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Grab canvas dimensions.
        final int canvasWidth = getWidth();
        final int canvasHeight = getHeight();

        // Calculate horizontal center.
        final float centerX = canvasWidth * 0.5f;

        // Draw baseline.
        final float baselineY = Math.round(canvasHeight * 0.6f);

        // Draw text.
        final float textWidth = numberPaint.measureText(displayedCount);
        final float textX = Math.round(centerX - textWidth * 0.5f);
        canvas.drawText(displayedCount, textX, baselineY, numberPaint);
    }
}
